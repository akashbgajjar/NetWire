package com.netwire.handler.streams;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.netwire.modal.User;

public class OutputStreamHandler extends Thread{
	
	private Socket socket = null;
	
	private ObjectOutputStream oos = null;
	
	private User user = null;
	
	public OutputStreamHandler(Socket socket, User user) throws IOException{
		this.socket = socket;
		this.oos = new ObjectOutputStream(this.socket.getOutputStream());
		this.user = user;
	}
	
	public synchronized void run(){
		while(true){
			try{
				oos.writeObject(user);
				oos.flush();
				this.wait();
			}catch(Exception e){
				
			}
		}
	}
	
	public void setUser(User user){
		this.user = user;
	}

	public void close() throws IOException{
		oos.close();
	}
}
