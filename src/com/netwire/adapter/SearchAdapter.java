package com.netwire.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;

public class SearchAdapter extends BaseAdapter{
	
	private ArrayList<RelativeLayout> searchViewStack;
	
	private Context ctx;
	
	public SearchAdapter(Context ctx, ArrayList<RelativeLayout> searchViewStack){
		this.ctx = ctx;
		this.searchViewStack = searchViewStack;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return searchViewStack.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		RelativeLayout view;
		if(convertView == null){
			view = searchViewStack.get(position);
			view.setLayoutParams(new GridView.LayoutParams(85, 85));
			view.setPadding(8, 8, 8, 8);
		} else {
			view = (RelativeLayout) convertView;
		}
		return view;
	}

}
