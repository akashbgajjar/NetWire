package com.netwire.adapter;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netwire.R;
import com.netwire.datastore.DataStore;
import com.netwire.modal.ActiveClient;

public class UserAdapter extends BaseAdapter {
	
	private Context ctx;
	
	private ArrayList<ActiveClient> clientArray;
	
	private ArrayList<RelativeLayout> viewStack;
	
	public UserAdapter(Context ctx, ArrayList<ActiveClient> clientArray){
		System.out.println("User Adapter..");
		this.ctx = ctx;
		this.clientArray = clientArray;
		viewStack = new ArrayList<RelativeLayout>();
		displayUsers();
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return viewStack.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return viewStack.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		System.out.println("User View.............");
		RelativeLayout view;
		if(convertView == null){
			view = viewStack.get(position);
			view.setLayoutParams(new GridView.LayoutParams(85, 85));
			view.setPadding(8, 8, 8, 8);
		} else {
			view = (RelativeLayout) convertView;
		}
		
		return view;
	}
	
	private void displayUsers(){
		if(!clientArray.isEmpty()){
			for(ActiveClient client : clientArray){
				createViewStackOfUsers(client);
			}
		}
	}
	
	private void createViewStackOfUsers(ActiveClient client){
		RelativeLayout layout = new RelativeLayout(ctx);
		ImageView imageView = new ImageView(ctx);
		RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(40,40);
		imageParams.leftMargin = 15;
		imageView.setLayoutParams(imageParams);
		imageView.setImageResource(R.drawable.user);
		layout.addView(imageView);
		TextView view = new TextView(ctx);
		RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		textParams.alignWithParent = true;
		view.setLayoutParams(textParams);
		view.setGravity(Gravity.CENTER_HORIZONTAL);
		view.setText(client.getClientName());
		view.setPadding(0, 40, 0, 0);
		layout.addView(view);
		viewStack.add(layout);
	}

}
