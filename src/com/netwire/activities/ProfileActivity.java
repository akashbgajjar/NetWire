package com.netwire.activities;

import com.netwire.NetWire;
import com.netwire.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends Activity{

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.profile);
    }

	@Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	System.out.println("Back key pressed.");
    	NetWire.closeConnectionsAndExit();
    	super.onBackPressed();
    }

}
