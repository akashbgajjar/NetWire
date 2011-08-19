package com.netwire.activities;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.netwire.NetWire;
import com.netwire.R;
import com.netwire.adapter.UserAdapter;
import com.netwire.datastore.DataStore;
import com.netwire.modal.ActiveClient;

public class UsersActivity extends Activity{
	
	private GridView layout;
	
	private static Context ctx;
	
	private ArrayList<ActiveClient> clientArray = new ArrayList<ActiveClient>();
	
	public static Handler handler;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user);
        layout = (GridView) findViewById(R.id.UserGrid);
        ctx = this;
        displayUsers();
        layout.setAdapter(new UserAdapter(ctx, clientArray));
	    
	    layout.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				System.out.println(arg2);
				ActiveClient client = clientArray.get(arg2);
				final Dialog dialog = new Dialog(ctx);
				dialog.setTitle("Client Info");
				dialog.setContentView(R.layout.userdialog);
				TextView clientName = (TextView) dialog.findViewById(R.id.ClientNameValue);
				clientName.setText(client.getClientName());
				TextView clientIP = (TextView) dialog.findViewById(R.id.ClientIPValue);
				clientIP.setText(client.getIPAddress());
				TextView clientPort = (TextView) dialog.findViewById(R.id.ClientPortValue);
				clientPort.setText("" + client.getPort());
				TextView clientStatus = (TextView) dialog.findViewById(R.id.ClientStatusValue);
				clientStatus.setText("" + (client.getStatus() == 1 ? "Connected" : "Not Connected"));
				Button button = (Button) dialog.findViewById(R.id.UserButtonOk);
				button.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
					
				});
				dialog.show();
			}
	    	
	    });
	    
	    handler = new Handler(){
	    	
	    	public void handleMessage(Message msg){
	    		clientArray = null;
				clientArray = new ArrayList<ActiveClient>();
				
				layout.invalidateViews();
				displayUsers();
				layout.setAdapter(new UserAdapter(ctx, clientArray));
	    	}
	    };
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// TODO Auto-generated method stub
    	super.onCreateOptionsMenu(menu);
    	MenuItem item = menu.add("Refresh");
    	return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getTitle().equals("Refresh")){
			clientArray = null;
			clientArray = new ArrayList<ActiveClient>();
			
			layout.invalidateViews();
			displayUsers();
			layout.setAdapter(new UserAdapter(ctx, clientArray));
    	}
    	return super.onOptionsItemSelected(item);
	}
	
	private void displayUsers(){
		if(!DataStore.map.isEmpty()){
			Iterator<String> itr = DataStore.map.keySet().iterator();
			while(itr.hasNext()){
				clientArray.add(DataStore.map.get(itr.next()));
			}
		}
	}

	@Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	System.out.println("Back key pressed.");
    	NetWire.closeConnectionsAndExit();
    	super.onBackPressed();
    }
}
