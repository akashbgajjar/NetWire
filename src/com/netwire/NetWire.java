package com.netwire;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

import com.netwire.activities.ProfileActivity;
import com.netwire.activities.TmpDownloadFolderActivity;
import com.netwire.activities.TmpSearchActivity;
import com.netwire.activities.UsersActivity;
import com.netwire.handler.ServerConnectionHandler;
import com.netwire.modal.User;

public class NetWire extends TabActivity {
	
	private static ServerConnectionHandler handler = null;
	
	private static Context ctx;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        ctx = this;
        
        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab
        
        /*intent = new Intent().setClass(this, DownloadActivity.class);
        spec = tabHost.newTabSpec("download").setIndicator("Folder",
                          res.getDrawable(R.drawable.ic_tab_contents))
                      .setContent(intent);
        tabHost.addTab(spec);*/
        
        intent = new Intent().setClass(this, TmpDownloadFolderActivity.class);
        spec = tabHost.newTabSpec("download").setIndicator("Folder",
                          res.getDrawable(R.drawable.ic_tab_folder))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        // Create an Intent to launch an Activity for the tab (to be reused)
        /*intent = new Intent().setClass(this, SearchActivity.class);
        spec = tabHost.newTabSpec("search").setIndicator("Search",
                          res.getDrawable(R.drawable.ic_tab_contents))
                      .setContent(intent);
        tabHost.addTab(spec);*/
        
        intent = new Intent().setClass(this, TmpSearchActivity.class);
        spec = tabHost.newTabSpec("search").setIndicator("Search",
                          res.getDrawable(R.drawable.ic_tab_search))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, UsersActivity.class);
        spec = tabHost.newTabSpec("users").setIndicator("Users",
                          res.getDrawable(R.drawable.ic_tab_user))
                      .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, ProfileActivity.class);
        spec = tabHost.newTabSpec("profile").setIndicator("Profile",
                          res.getDrawable(R.drawable.ic_tab_profile))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        
        tabHost.setCurrentTab(0);
        
        handler = (ServerConnectionHandler) getLastNonConfigurationInstance();
        
        if(handler == null){
	        User user = new User("Akash", true);
	        try{
	        	handler = new ServerConnectionHandler(user);
	        	handler.startStreams();
	        }catch(Exception e){
	        	e.printStackTrace();
	        	//System.exit(0);
	        }
        }

    }
    
    @Override
    public Object onRetainNonConfigurationInstance() {
        return handler;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// TODO Auto-generated method stub
    	super.onCreateOptionsMenu(menu);
    	MenuItem item = menu.add("Exit");
    	
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getTitle().equals("Exit")){
    		closeConnectionsAndExit();
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    public static void closeConnectionsAndExit(){
    	try {
    		Toast.makeText(ctx.getApplicationContext(), "NetWire Closed", Toast.LENGTH_SHORT).show();
			System.out.println("Closing Connection..");
			User user = new User("Akash", false);
			handler.terminateHandlers(user);
			//handler.closeAll();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			System.exit(0);
		}
    }
}