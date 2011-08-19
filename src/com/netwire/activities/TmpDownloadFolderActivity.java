package com.netwire.activities;

import java.io.File;
import java.text.DecimalFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.netwire.NetWire;
import com.netwire.R;
import com.netwire.modal.Constants;

public class TmpDownloadFolderActivity extends Activity{
	
	private TableLayout tableLayout;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tmp);
        tableLayout = (TableLayout) findViewById(R.id.TmpTableLayout);
        
        displayFilesFromDownloadFolder(this);
        
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
    		tableLayout.removeAllViews();
    		displayFilesFromDownloadFolder(this);
    	}
    	return super.onOptionsItemSelected(item);
    }
	
	private void displayFilesFromDownloadFolder(Context ctx){
		System.out.println("Inside display files folder method ....");
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
							createViewStackOfFiles(tmp, ctx);
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
	
	/**
	 * 
	 * @param size
	 * @return
	 */
	private static String readableFileSize(long size) {
		if(size <= 0 ) {
			return "0";
		} else {
			
			final String[] units = {"B", "KB", "MB", "GB", "TB"};
			int dig = (int) (Math.log10(size)/Math.log10(1024));

			return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, dig)) + " " + units[dig];
		}
		
	}
	
	private void createViewStackOfFiles(final File file, final Context ctx){
		
		String file_size = "";
		
		final RelativeLayout layout = new RelativeLayout(ctx);
		TableRow.LayoutParams rootLayoutParams = new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		rootLayoutParams.setMargins(0, 0, 0, 2);
		layout.setBackgroundColor(Color.TRANSPARENT);
		layout.setLayoutParams(rootLayoutParams);
		
		ImageView imageView = new ImageView(ctx);
		RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(40,40);
		imageView.setLayoutParams(imageParams);
		imageView.setPadding(0, 3, 0, 0);
		
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
		
		TextView view = new TextView(ctx);
		RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		textParams.setMargins(50, 0, 0, 0);
		view.setLayoutParams(textParams);
		view.setPadding(3, 3, 0, 0);
		view.setText("" + (file.getName().length() > 25 ? (file.getName().substring(0,25) + "...") : file.getName()));
		layout.addView(view);
		
		TextView sizeView = new TextView(ctx);
		RelativeLayout.LayoutParams sizeParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		sizeParams.alignWithParent = true;
		sizeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		sizeParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		sizeView.setLayoutParams(sizeParams);
		sizeView.setPadding(0, 0, 5, 0);
		
		file_size = readableFileSize(file.length());
		
		sizeView.setText(" " + file_size);
		layout.addView(sizeView);
		
		final TableRow row = new TableRow(ctx);
		TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		rowParams.setMargins(0, 0, 1, 8);
		row.setLayoutParams(rowParams);
		row.setMinimumHeight(60);
		row.setBackgroundResource(Color.TRANSPARENT);
		row.addView(layout);
		
		// creating a border
		View border = new View(ctx);
		border.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, 1));
		border.setBackgroundColor(Color.WHITE);
		layout.addView(border);
		
		row.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				layout.setBackgroundResource(R.drawable.row_content_background);
				
				final CharSequence[] options = {"View File Info","View File","Delete File"};
				
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
				alertDialog.setTitle("Please Choose:");
				alertDialog.setItems(options, new DialogInterface.OnClickListener(){

					public void onClick(DialogInterface dialog, int item) {
						// TODO Auto-generated method stub
						layout.setBackgroundColor(Color.TRANSPARENT);
						Toast.makeText(getApplicationContext(), item == 2 ? "Deleted" : options[item], Toast.LENGTH_SHORT).show();
						
						if(item == 0){
							
							showFileInfoDialog(layout, file, ctx, row);
							
						}else if(item == 1){
							
							if(file.getName().toLowerCase().contains(".jpg") ||
									file.getName().toLowerCase().contains(".jpeg") ||
									file.getName().toLowerCase().contains(".bmp") ||
									file.getName().toLowerCase().contains(".png")){
								
								Intent intent = new Intent();  
								intent.setAction(android.content.Intent.ACTION_VIEW);  
								intent.setDataAndType(Uri.fromFile(file), "image/*");  
								startActivity(intent);
								
							}else if(file.getName().toLowerCase().contains(".wmv") ||
									file.getName().toLowerCase().contains(".avi") ||
									file.getName().toLowerCase().contains(".mp4") ||
									file.getName().toLowerCase().contains(".mov") ||
									file.getName().toLowerCase().contains(".mpg") ||
									file.getName().toLowerCase().contains(".3gp")){
								
								Intent intent = new Intent();  
								intent.setAction(android.content.Intent.ACTION_VIEW);  
								intent.setDataAndType(Uri.fromFile(file), "video/*");  
								startActivity(intent);
								
							}else if(file.getName().toLowerCase().contains(".mp3")){
								
								Intent intent = new Intent();  
								intent.setAction(android.content.Intent.ACTION_VIEW);  
								intent.setDataAndType(Uri.fromFile(file), "audio/*");  
								startActivity(intent);
								
							}else if(file.getName().toLowerCase().contains(".pdf") ||
									file.getName().toLowerCase().contains(".txt")){
								
								Intent intent = new Intent();  
								intent.setAction(android.content.Intent.ACTION_VIEW);  
								intent.setDataAndType(Uri.fromFile(file), "text/plain");  
								startActivity(intent);
								
							}
							
						}else if(item == 2){
							
							deleteFileFromDownloadFolder(file, row);
							
						}
					}
					
				});
				

				alertDialog.setOnCancelListener(new OnCancelListener(){

					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						layout.setBackgroundColor(Color.TRANSPARENT);
					}
					
				});
				
				alertDialog.show();
				
			}
			
		});
		
		tableLayout.addView(row);
	}
	
	private void deleteFileFromDownloadFolder(File file, TableRow row){
		file.delete();
		tableLayout.removeView(row);
	}
	
	private void showFileInfoDialog(final RelativeLayout layout, File file, Context ctx, final TableRow row){
		
		final Dialog dialog = new Dialog(ctx);
		dialog.setTitle("File Info");
		dialog.setContentView(R.layout.fileinfodialog);
		
		TextView filename = (TextView) dialog.findViewById(R.id.FileNameValue);
		filename.setText(file.getName());
		
		System.out.println(filename.getHeight());
		
		TextView filetype = (TextView) dialog.findViewById(R.id.FileTypeValue);
		if(file.getName().toLowerCase().contains(".pdf"))
			filetype.setText("Adode PDF");
		else if(file.getName().toLowerCase().contains(".wmv") ||
				file.getName().toLowerCase().contains(".avi") ||
				file.getName().toLowerCase().contains(".mp4") ||
				file.getName().toLowerCase().contains(".mov") ||
				file.getName().toLowerCase().contains(".mpg") ||
				file.getName().toLowerCase().contains(".mpeg"))
			filetype.setText("Movie");
		else if(file.getName().toLowerCase().contains(".jpg") ||
				file.getName().toLowerCase().contains(".jpeg") ||
				file.getName().toLowerCase().contains(".bmp") ||
				file.getName().toLowerCase().contains(".png"))
			filetype.setText("Image");
		else if(file.getName().toLowerCase().contains(".mp3"))
			filetype.setText("Audio");
		else
			filetype.setText("Others");
		
		TextView filesize = (TextView) dialog.findViewById(R.id.FileSizeValue);
		filesize.setText("" + file.length());
		
		Button button = (Button) dialog.findViewById(R.id.FileInfoButtonOk);
		button.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				//row.setBackgroundResource(R.drawable.row_background);
				row.setBackgroundColor(Color.TRANSPARENT);
			}
			
		});
		dialog.show();
	}
	
	@Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	System.out.println("Back key pressed.");
    	NetWire.closeConnectionsAndExit();
    	super.onBackPressed();
    }

}
