package com.netwire.handler;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.netwire.handler.streams.InputStreamHandler;
import com.netwire.handler.streams.OutputStreamHandler;
import com.netwire.modal.Constants;
import com.netwire.modal.User;
import com.netwire.server.ClientServer;

public class ServerConnectionHandler {
	
	private Socket socket = null;
	
	private InputStreamHandler ipHandler = null;
	
	private OutputStreamHandler opHandler = null;
	
	private User user = null;
	
	public ServerConnectionHandler(User user) throws UnknownHostException, IOException{
		this.user = user;
		makeConnectionToServer();
	}
	
	private void makeConnectionToServer() throws UnknownHostException, IOException{
		
		this.socket = new Socket(Constants.serverIPAddress, Constants.port);
		System.out.println(socket.getLocalPort());
		this.opHandler = new OutputStreamHandler(this.socket, this.user);
		this.ipHandler = new InputStreamHandler(this.socket);
		int port = this.socket.getLocalPort();
		System.out.println("Local Port : " + port);
		new ClientServer().init(7777);
	
	}
	
	public void startStreams(){
		
		synchronized(this){
			opHandler.start();
			ipHandler.start();
		}
		
	}
	
	public void terminateHandlers(User user) throws InterruptedException{
		synchronized(opHandler){
			opHandler.setUser(user);
			opHandler.notify();
		}
	}
	
	public void closeAll() throws IOException{
		socket.close();
	}

}
