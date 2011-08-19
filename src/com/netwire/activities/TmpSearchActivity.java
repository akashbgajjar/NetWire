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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.netwire.NetWire;
import com.netwire.R;
import com.netwire.datastore.DataStore;
import com.netwire.handler.SearchThreadPool;
import com.netwire.modal.ActiveClient;
import com.netwire.modal.Constants;
import com.netwire.modal.FileContent;
import com.netwire.modal.Search;
import com.netwire.modal.SearchResult;

public class TmpSearchActivity extends Activity{
	
	private Handler handler = null;
	
	private Context ctx;
	
	private TableLayout tableLayout;
	
	public void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.tmpsearch);
        
        tableLayout = (TableLayout) findViewById(R.id.TmpSearchTableLayout);
        
        ctx = this;
        
        runOnUiThread(new Runnable(){
        	public void run(){
        		System.out.println("Handler object created....");
        		handler = new Handler(){
                	@Override
                	public void handleMessage(Message msg) {
                		// TODO Auto-generated method stub
                		super.handleMessage(msg);
                		
                		System.out.println(msg.obj);
                		SearchResult searchResult = (SearchResult) msg.obj;
                		DataStore.vector.add(searchResult);
                		updateTableLayout(searchResult.getSocket(), searchResult.getSearch());
                	}
                };
        	}
        });
        
	}
	
	private void updateTableLayout(final Socket socket, final Search search){
		
		final RelativeLayout layout = new RelativeLayout(ctx);
		TableRow.LayoutParams rootLayoutParams = new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layout.setBackgroundColor(Color.TRANSPARENT);
		layout.setLayoutParams(rootLayoutParams);
		
		ImageView imageView = new ImageView(ctx);
		RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(40,40);
		imageView.setLayoutParams(imageParams);
		imageView.setPadding(0, 3, 0, 0);
		if(search.getFilename().toLowerCase().contains(".pdf"))
			imageView.setImageResource(R.drawable.pdf);
		else if(search.getFilename().toLowerCase().contains(".wmv") ||
				search.getFilename().toLowerCase().contains(".avi") ||
				search.getFilename().toLowerCase().contains(".mp4") ||
				search.getFilename().toLowerCase().contains(".mov") ||
				search.getFilename().toLowerCase().contains(".mpg"))
			imageView.setImageResource(R.drawable.movie);
		else if(search.getFilename().toLowerCase().contains(".jpg") ||
				search.getFilename().toLowerCase().contains(".jpeg") ||
				search.getFilename().toLowerCase().contains(".bmp") ||
				search.getFilename().toLowerCase().contains(".png"))
			imageView.setImageResource(R.drawable.image);
		else if(search.getFilename().toLowerCase().contains(".mp3"))
			imageView.setImageResource(R.drawable.audio);
		else
			imageView.setImageResource(R.drawable.file);
		layout.addView(imageView);
		
		TextView view = new TextView(ctx);
		RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		textParams.setMargins(50, 0, 0, 0);
		view.setLayoutParams(textParams);
		view.setPadding(3, 3, 0, 0);
		view.setText("" + (search.getFilename().length() > 25 ? (search.getFilename().substring(0,25) + "...") : search.getFilename()));
		layout.addView(view);
		
		TextView sizeView = new TextView(ctx);
		RelativeLayout.LayoutParams sizeParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		sizeParams.alignWithParent = true;
		sizeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		sizeParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		sizeView.setLayoutParams(sizeParams);
		sizeView.setPadding(0, 0, 5, 0);
		sizeView.setText(" " + search.getFilesize());
		layout.addView(sizeView);
		
		final TableRow row = new TableRow(ctx);
		TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		rowParams.setMargins(0, 0, 1, 8);
		row.setLayoutParams(rowParams);
		row.setMinimumHeight(60);
		row.setBackgroundResource(R.drawable.row_background);
		row.addView(layout);
		
		row.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				layout.setBackgroundResource(R.drawable.row_content_background);
				
				final CharSequence[] options = {"File Info","Download"};
				
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
				alertDialog.setTitle("File Options.");
				alertDialog.setItems(options, new DialogInterface.OnClickListener(){

					public void onClick(DialogInterface dialog, int item) {
						// TODO Auto-generated method stub
						layout.setBackgroundColor(Color.BLACK);
						Toast.makeText(getApplicationContext(), options[item], Toast.LENGTH_SHORT).show();
						if(item == 0){
							showFileInfoDialog(search, ctx, row);
						}else if(item == 1){
							downloadSelectedFile(socket, search);
						}
					}
					
				});
				
				alertDialog.setOnCancelListener(new OnCancelListener(){

					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						layout.setBackgroundColor(Color.BLACK);
					}
					
				});
				
				alertDialog.show();
				
			}
		});
		
		tableLayout.addView(row);
	}
	
	private void downloadSelectedFile(final Socket socket, final Search search){
		
		final ProgressDialog progressDialog = new ProgressDialog(ctx);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setMessage("Downloading...");
		progressDialog.setCancelable(false);
		progressDialog.show();
		
		new Thread(new Runnable(){
			public void run(){
					System.out.println(socket);
					String[] mixer = socket.getRemoteSocketAddress().toString().split(":");
					System.out.println(DataStore.map);
					System.out.println(socket.getInetAddress().getHostAddress() + ":" + mixer[1]);
					ActiveClient client = DataStore.map.get(socket.getInetAddress().getHostAddress());
					
					Socket _socket;
					ObjectOutputStream oos;
					ObjectInputStream ois;
					
					try{
						progressDialog.setMax(Integer.parseInt(search.getFilesize()));
						//_socket = new Socket(client.getIPAddress(), client.getPort());
						System.out.println(client.getIPAddress());
						_socket = new Socket(client.getIPAddress(), 7777);
						oos = new ObjectOutputStream(_socket.getOutputStream());
						ois = new ObjectInputStream(_socket.getInputStream());
						FileContent fileContent = new FileContent();
						fileContent.setFileName(search.getFilename());
						fileContent.setFilebytes(new byte[0]);
						fileContent.setLen(0);
						fileContent.setOff(0);
						byte[] filename = search.getFilename().getBytes();
						oos.writeObject(filename);
						
						File file = new File(Constants.downloadFolder + search.getFilename());
						FileOutputStream out = new FileOutputStream(file);
						
						byte[] mb = new byte[1024];

					    for(int c = ois.read(mb, 0, 1024); c > 0; c = ois.read(mb, 0, 1024)){
					 	   out.write(mb, 0, c);
					 	   progressDialog.incrementProgressBy(c);
					    }
					    
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
						e.printStackTrace();
					}
					progressDialog.dismiss();
				}
				
		}).start();
	}
	
	private void showFileInfoDialog(Search search, Context ctx, final TableRow row){
		final Dialog dialog = new Dialog(ctx);
		dialog.setTitle("File Info.");
		dialog.setContentView(R.layout.fileinfodialog);
		
		String file = search.getFilename();
		
		TextView filename = (TextView) dialog.findViewById(R.id.FileNameValue);
		filename.setText(file);
		
		System.out.println(filename.getHeight());
		
		TextView filetype = (TextView) dialog.findViewById(R.id.FileTypeValue);
		if(file.toLowerCase().contains(".pdf"))
			filetype.setText("Adode PDF");
		else if(file.toLowerCase().contains(".wmv") ||
				file.toLowerCase().contains(".avi") ||
				file.toLowerCase().contains(".mp4") ||
				file.toLowerCase().contains(".mov") ||
				file.toLowerCase().contains(".mpg") ||
				file.toLowerCase().contains(".mpeg"))
			filetype.setText("Movie");
		else if(file.toLowerCase().contains(".jpg") ||
				file.toLowerCase().contains(".jpeg") ||
				file.toLowerCase().contains(".bmp") ||
				file.toLowerCase().contains(".png"))
			filetype.setText("Image");
		else if(file.toLowerCase().contains(".mp3"))
			filetype.setText("Audio");
		else
			filetype.setText("Others");
		
		TextView filesize = (TextView) dialog.findViewById(R.id.FileSizeValue);
		filesize.setText("" + search.getFilesize());
		
		Button button = (Button) dialog.findViewById(R.id.FileInfoButtonOk);
		button.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				row.setBackgroundResource(R.drawable.row_background);
			}
			
		});
		dialog.show();
	}
	
	

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// TODO Auto-generated method stub
    	super.onCreateOptionsMenu(menu);
    	MenuItem item = menu.add("Search");
    	item = menu.add("Clear Search Cache");
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
		    			
	    				clearSearchCache();
	    	    			
	    				try {
	    					new SearchThreadPool().searchInAvailableClients(search.getText().toString(), handler);
	 					} catch (UnknownHostException e) {
	  		    			e.printStackTrace();
	     				} catch (IOException e) {
	     					e.printStackTrace();
	       				} catch (ClassNotFoundException e) {
	      					e.printStackTrace();
	      				} finally {
	       					dialog.dismiss();
	       				}
	   				}
    	        });
    	   dialog.show();
    	}else if(item.getTitle().equals("Clear Search Cache")){
    		clearSearchCache();
    	}
    	return super.onOptionsItemSelected(item);
    }
	
	@Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	System.out.println("Back key pressed.");
    	NetWire.closeConnectionsAndExit();
    	super.onBackPressed();
    }
	
	private void clearSearchCache(){
		if(!DataStore.vector.isEmpty()){
			for(int i=0; i<DataStore.vector.size(); i++){
				DataStore.vector.remove(i);
				DataStore.vector.trimToSize();
			}
			tableLayout.removeAllViews();
		}
	}
}
