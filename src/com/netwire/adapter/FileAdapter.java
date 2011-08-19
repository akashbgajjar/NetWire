package com.netwire.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;

public class FileAdapter extends BaseAdapter {
	
	private Context ctx;
	private ArrayList<RelativeLayout> viewStack;
	
	public FileAdapter(Context ctx, ArrayList<RelativeLayout> viewStack){
		this.ctx = ctx;
		this.viewStack = viewStack;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return viewStack.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
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

}
