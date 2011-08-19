package com.netwire.handler.streams;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;

import android.os.Message;

import com.netwire.activities.UsersActivity;
import com.netwire.datastore.DataStore;
import com.netwire.modal.ActiveClient;
import com.netwire.modal.User;

public class InputStreamHandler extends Thread{
	
	private Socket socket = null;
	
	private ObjectInputStream ois = null;
	
	private Object object = null;
	
	public InputStreamHandler(Socket socket) throws StreamCorruptedException, IOException{
		this.socket = socket;
		ois = new ObjectInputStream(this.socket.getInputStream());
	}
	
	public synchronized void run(){
		while(true){
			try{
				if((object = ois.readObject()) != null){
					if(object instanceof ActiveClient){
						ActiveClient client = (ActiveClient) object;
						System.out.println("ClientName : " + client.getClientName());
						System.out.println("IPAddress : " + client.getIPAddress());
						System.out.println("Port : " + client.getPort());
						System.out.println("Status : " + client.getStatus());
						if(client.getStatus() == 1){
							DataStore.map.put(client.getIPAddress(), client);
						}else{
							DataStore.map.remove(client.getIPAddress());
							System.out.println("Client : " + client.getClientName() + " removed successfully.");
						}
						Thread.sleep(1000);
					}else if(object instanceof User){
						User user = (User) object;
						if(!user.isStatus()){
							System.out.println("Terminating Application.");
							break;
						}
					}
					
					UsersActivity.handler.post(new Runnable(){
						public void run(){
							Message msg = new Message();
							UsersActivity.handler.dispatchMessage(msg);
						}
					});
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void close() throws IOException{
		ois.close();
	}

}
