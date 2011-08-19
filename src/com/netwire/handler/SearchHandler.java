package com.netwire.handler;

import java.io.IOException;
import java.net.Socket;

import android.os.Handler;

import com.netwire.handler.streams.SearchInputHandler;
import com.netwire.handler.streams.SearchOutputHandler;
import com.netwire.modal.Search;

public class SearchHandler implements Runnable{
	
	private Socket socket = null;
	
	private SearchInputHandler sih = null;
	
	private SearchOutputHandler soh = null;
	
	private Search search = null;
	
	public SearchHandler(Socket socket, Search search, Handler handler) throws IOException{
		this.socket = socket;
		this.search = search;
		this.soh = new SearchOutputHandler(socket);
		this.sih = new SearchInputHandler(socket, handler);
	}
	
	public synchronized void run(){
		try{
			this.soh.writeObject(search);
			this.sih.readObject();
			this.socket.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
