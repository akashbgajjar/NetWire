package com.netwire.handler.streams;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;

import android.os.Handler;
import android.os.Message;

import com.netwire.modal.Search;
import com.netwire.modal.SearchResult;

public class SearchInputHandler {
	
	private Socket socket = null;
	
	private Search search = null;
	
	private ObjectInputStream ois = null;
	
	private Handler handler;
	
	public SearchInputHandler(Socket socket, Handler handler) throws StreamCorruptedException, IOException{
		this.socket = socket;
		this.handler = handler;
		this.ois = new ObjectInputStream(this.socket.getInputStream());
	}

	public void readObject(){
		while(true){
			try{
				if((search = (Search) ois.readObject()) != null){
					System.out.println(search.getFilename());
					System.out.println(search.getFilesize());
					System.out.println(search.isStatus());
					SearchResult searchResult = new SearchResult(socket, search);
					final Message msg = new Message();
					msg.obj = searchResult;
					//DataStore.vector.add(searchResult);
					handler.post(new Runnable(){
						public void run(){
							handler.dispatchMessage(msg);
						}
					});
				}
			}catch(Exception e){
				e.printStackTrace();
				break;
			}
		}
	}
	
	public void close() throws IOException{
		ois.close();
	}
}
