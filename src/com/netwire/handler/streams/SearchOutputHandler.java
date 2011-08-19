package com.netwire.handler.streams;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.netwire.modal.Search;

public class SearchOutputHandler {
	
	private Socket socket = null;
	
	private ObjectOutputStream oos = null;
	
	public SearchOutputHandler(Socket socket) throws IOException{
		this.socket = socket;
		this.oos = new ObjectOutputStream(this.socket.getOutputStream());
	}
	
	public void writeObject(Search search) throws IOException{
		oos.writeObject(search);
		oos.flush();
	}
	
	public void close() throws IOException{
		oos.close();
	}

}
