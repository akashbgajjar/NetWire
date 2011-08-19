package com.netwire.activities;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netwire.R;
import com.netwire.adapter.FileAdapter;
import com.netwire.modal.Constants;

public class DownloadActivity extends Activity{

	private ArrayList<RelativeLayout> viewStack;
	
	private GridView layout;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download);
        layout = (GridView) findViewById(R.id.DownloadFileGrid);
        viewStack = new ArrayList<RelativeLayout>();
        layout.invalidateViews();
        displayFilesFromDownloadFolder();
        layout.setAdapter(new FileAdapter(this, viewStack));
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
    		viewStack = null;
    		viewStack = new ArrayList<RelativeLayout>();
    		layout = null;
    		layout = (GridView) findViewById(R.id.DownloadFileGrid);
    		//layout.invalidate();
    		displayFilesFromDownloadFolder();
            layout.setAdapter(new FileAdapter(this, viewStack));
    	}
    	return super.onOptionsItemSelected(item);
    }
	
	private void displayFilesFromDownloadFolder(){
		File file = new File(Constants.rootFolder);
		File[] files = file.listFiles();
		boolean flag = false;
		for(int i=0; i<files.length; i++){
			if(files[i].isDirectory()){
				if(files[i].getName().toLowerCase().equals(Constants.downloadFolderName)){
					flag = true;
					System.out.println(files[i]);
					File download = new File(file.getName() + File.separator + files[i].getName());
					if(download.isDirectory()){
						File[] downloadfiles = download.listFiles();
						for(File tmp : downloadfiles){
							createViewStackOfFiles(tmp);
						}
					}
					break;
				}
			}
		}
		
		if(!flag){
			new File(Constants.rootFolder + File.separator + Constants.downloadFolderName).mkdir();
		}
	}
	
	private void createViewStackOfFiles(File file){
		RelativeLayout layout = new RelativeLayout(this);
		ImageView imageView = new ImageView(this);
		RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(40,40);
		imageParams.leftMargin = 15;
		imageView.setLayoutParams(imageParams);
		if(file.getName().toLowerCase().contains(".pdf"))
			imageView.setImageResource(R.drawable.pdf);
		else if(file.getName().toLowerCase().contains(".wmv") ||
				file.getName().toLowerCase().contains(".avi") ||
				file.getName().toLowerCase().contains(".mp4") ||
				file.getName().toLowerCase().contains(".mov") ||
				file.getName().toLowerCase().contains(".mpg"))
			imageView.setImageResource(R.drawable.movie);
		else if(file.getName().toLowerCase().contains(".jpg") ||
				file.getName().toLowerCase().contains(".jpeg") ||
				file.getName().toLowerCase().contains(".bmp") ||
				file.getName().toLowerCase().contains(".png"))
			imageView.setImageResource(R.drawable.image);
		else if(file.getName().toLowerCase().contains(".mp3"))
			imageView.setImageResource(R.drawable.audio);
		else
			imageView.setImageResource(R.drawable.file);
		layout.addView(imageView);
		TextView view = new TextView(this);
		RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		textParams.alignWithParent = true;
		view.setLayoutParams(textParams);
		view.setGravity(Gravity.CENTER_HORIZONTAL);
		view.setText(file.getName().length() > 8 ? (file.getName().substring(0,5) + "...") : file.getName());
		view.setPadding(0, 40, 0, 0);
		layout.addView(view);
		viewStack.add(layout);
	}

}
