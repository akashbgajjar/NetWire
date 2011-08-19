package com.netwire.activities;

import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.netwire.R;
import com.netwire.adapter.SearchAdapter;
import com.netwire.datastore.DataStore;
import com.netwire.handler.SearchThreadPool;
import com.netwire.modal.ActiveClient;
import com.netwire.modal.Constants;
import com.netwire.modal.FileContent;
import com.netwire.modal.Search;
import com.netwire.modal.SearchResult;

public class SearchActivity extends Activity{

	private Context ctx;
	
	private GridView searchGrid;
	
	public static Handler handler;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search);
        this.ctx = this;
        ArrayList<RelativeLayout> searchViewStack = createSearchFileStack();
        searchGrid = (GridView) findViewById(R.id.SearchMainGrid);
        if(!searchViewStack.isEmpty()){
        	this.searchGrid.setAdapter(new SearchAdapter(ctx, searchViewStack));
        }
        
        handler = new Handler(){
        	@Override
        	public void handleMessage(Message msg) {
        		// TODO Auto-generated method stub
        		super.handleMessage(msg);
        		System.out.println(msg.obj);
        		//searchGrid.invalidateViews();
    			ArrayList<RelativeLayout> searchViewStack = createSearchFileStack();
    			if(!searchViewStack.isEmpty()){
    	        	searchGrid.setAdapter(new SearchAdapter(ctx, searchViewStack));
    	        }
        	}
        };
        
        searchGrid.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, final int index,
					long arg3) {
				System.out.println("Item Selected ....");
				/**/
				final int indexOfGridSelected = index;
				
				final CharSequence[] options = {"File Info","Download"};
				
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
				alertDialog.setTitle("File Options.");
				alertDialog.setItems(options, new DialogInterface.OnClickListener(){

					public void onClick(DialogInterface dialog, int item) {
						// TODO Auto-generated method stub
						Toast.makeText(getApplicationContext(), options[item], Toast.LENGTH_SHORT).show();
						if(item == 0){
							showFileInfoDialog(indexOfGridSelected, options[item]);
						}else if(item == 1){
							showDownloadDialog(indexOfGridSelected);
						}
					}
					
				});
				
				alertDialog.show();
				
				
			}
        	
        });
        
	}
	
	private void showFileInfoDialog(int index, CharSequence option){
		final Dialog dialog = new Dialog(ctx);
		dialog.setTitle(option);
		dialog.setContentView(R.layout.fileinfodialog);
		Button button = (Button) dialog.findViewById(R.id.FileInfoButtonOk);
		button.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
			
		});
		dialog.show();
	}
	
	private void showDownloadDialog(final int index){
		final ProgressDialog progressDialog = new ProgressDialog(ctx);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setMessage("Downloading...");
		progressDialog.setCancelable(false);
		progressDialog.show();
		
		new Thread(new Runnable(){
			public void run(){
					SearchResult result = DataStore.vector.get(index);
					Socket socket = result.getSocket();
					Search search = result.getSearch();
					System.out.println(socket);
					String[] mixer = socket.getRemoteSocketAddress().toString().split(":");
					ActiveClient client = DataStore.map.get(socket.getInetAddress().getHostAddress() + ":" + mixer[1]);
					
					Socket _socket;
					ObjectOutputStream oos;
					ObjectInputStream ois;
					
					try{
						progressDialog.setMax(Integer.parseInt(search.getFilesize()));
						_socket = new Socket(client.getIPAddress(), client.getPort());
						oos = new ObjectOutputStream(_socket.getOutputStream());
						ois = new ObjectInputStream(_socket.getInputStream());
						FileContent fileContent = new FileContent();
						fileContent.setFileName(search.getFilename());
						fileContent.setFilebytes(new byte[0]);
						fileContent.setLen(0);
						fileContent.setOff(0);
						oos.writeObject(fileContent);
						
						File file = new File(Constants.downloadFolder + search.getFilename());
						FileOutputStream out = new FileOutputStream(file);
						int c;
					    
					    do{
					    	FileContent content = (FileContent)ois.readObject();
					    	c = content.getLen();
					    	out.write(content.getFilebytes(), 0, c);
					    	out.flush();
					    	progressDialog.incrementProgressBy(c);
					    }while(c > 0);
					    
					    oos.close();
						ois.close();
						_socket.close();
						out.close();

					}catch(Exception e){
						progressDialog.dismiss();
						if(e instanceof IOException){
							System.out.println("No Space on SDCard...");
						}else if(e instanceof SocketException){
							System.out.println("You are no longer connected...");
						}else if(e instanceof EOFException){
							System.out.println("Connection closed...");
						}
					}
					progressDialog.dismiss();
				}
				
		}).start();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// TODO Auto-generated method stub
    	super.onCreateOptionsMenu(menu);
    	MenuItem item = menu.add("Search");
    	item = menu.add("Refresh");
    	return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getTitle().equals("Search")){
    		
    		final Dialog dialog = new Dialog(this);
    	    dialog.setContentView(R.layout.searchdialog);
    	    dialog.setTitle("Search");
    	        
    	    final EditText search = (EditText) dialog.findViewById(R.id.SearchEditText);
    	    search.setFocusable(true);
    	    
    	    Button button = (Button) dialog.findViewById(R.id.SearchButton);
    	        
    	    button.setOnClickListener(new OnClickListener(){

    			public void onClick(View v) {
	    				
    					for(int i=0; i<DataStore.vector.size(); i++){
    						DataStore.vector.remove(i);
    						DataStore.vector.trimToSize();
    					}
    					
    				
    					/*try {
	    					SearchThreadPool.searchInAvailableClients(search.getText().toString());
	    				} catch (UnknownHostException e) {
	    					e.printStackTrace();
	    				} catch (IOException e) {
	    					e.printStackTrace();
	    				} catch (ClassNotFoundException e) {
	    					e.printStackTrace();
	    				} finally {
	    					dialog.dismiss();
	    				}*/
    				}
    	        });
    	   dialog.show();
			
    	}else if(item.getTitle().equals("Refresh")){
    		searchGrid.invalidateViews();
			ArrayList<RelativeLayout> searchViewStack = createSearchFileStack();
			if(!searchViewStack.isEmpty()){
				searchGrid.setAdapter(new SearchAdapter(ctx, searchViewStack));
	        }
    	}
    	return super.onOptionsItemSelected(item);
    }
	
	private ArrayList<RelativeLayout> createSearchFileStack(){
		if(!DataStore.vector.isEmpty()){
			ArrayList<RelativeLayout> searchViewStack = new ArrayList<RelativeLayout>(DataStore.vector.size());
			for(SearchResult result : DataStore.vector){
				Search search = result.getSearch();
					if(search.isStatus()){
						RelativeLayout layout = new RelativeLayout(this);
						ImageView imageView = new ImageView(this);
						RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(40,40);
						imageParams.leftMargin = 15;
						imageView.setLayoutParams(imageParams);
						String file = search.getFilename();
						if(file.toLowerCase().contains(".pdf"))
							imageView.setImageResource(R.drawable.pdf);
						else if(file.toLowerCase().contains(".wmv") ||
								file.toLowerCase().contains(".avi") ||
								file.toLowerCase().contains(".mp4") ||
								file.toLowerCase().contains(".mov") ||
								file.toLowerCase().contains(".mpg"))
							imageView.setImageResource(R.drawable.movie);
						else if(file.toLowerCase().contains(".jpg") ||
								file.toLowerCase().contains(".jpeg") ||
								file.toLowerCase().contains(".bmp") ||
								file.toLowerCase().contains(".png"))
							imageView.setImageResource(R.drawable.image);
						else if(file.toLowerCase().contains(".mp3"))
							imageView.setImageResource(R.drawable.audio);
						else
							imageView.setImageResource(R.drawable.file);
						layout.addView(imageView);
						TextView view = new TextView(this);
						RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
						textParams.alignWithParent = true;
						view.setLayoutParams(textParams);
						view.setGravity(Gravity.CENTER_HORIZONTAL);
						view.setText(file.length() > 8 ? (file.substring(0,5) + "...") : file);
						view.setPadding(0, 40, 0, 0);
						layout.addView(view);
						searchViewStack.add(layout);
					}
				}
			
			return searchViewStack;
		}
		return new ArrayList<RelativeLayout>();
	}

}
