package com.netwire.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.netwire.modal.Constants;
import com.netwire.modal.Search;

public class ClientServer {
	
	public void init(final int port){
		System.out.println("Connection started at port : " + port);
		
		
			new Thread(new Runnable(){
				
				public void run(){
					try{
						ServerSocket server = new ServerSocket(port);
						
						while(true){
							
							System.out.println("Waiting.....");
							Socket _socket = server.accept();
							sendAndReceiveData(_socket);
						
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}).start();
		
	}

	private void sendAndReceiveData(Socket socket) throws IOException, ClassNotFoundException{
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream oois = new ObjectInputStream(socket.getInputStream());
		
		Object obj;
		Search _search = new Search();
		
		if((obj = oois.readObject()) != null){
			
			System.out.println(obj);
			
			if(obj instanceof Search){
				
				_search = (Search)obj;
				System.out.println(_search.getFilename());
				File file = new File(Constants.downloadFolder);
				
				if(file.isDirectory()){
					
					System.out.println("Is Download Directory..");
					File[] filelist = file.listFiles();
					System.out.println(filelist.length);
					
					for(File singlefile : filelist){
						
						File foundfile = searchFileSystem(singlefile, _search.getFilename());
						System.out.println(foundfile);
						
						if(foundfile != null){
							
							Search newsearch = new Search();
							newsearch.setFilename(foundfile.getName());
							newsearch.setFilesize("" + foundfile.length());
							newsearch.setStatus(foundfile.exists());
							System.out.println("Filename : " + newsearch.getFilename());
							System.out.println("File Exists : " + newsearch.isStatus());
							System.out.println("File Size : " + newsearch.getFilesize());
							oos.writeObject(newsearch);
						}
					}
				}
			}else{
				
				String filename = new String((byte[])obj);
				System.out.println(filename);
				File file = new File(Constants.downloadFolder +filename);
				FileInputStream in = new FileInputStream(file);
				System.out.println("Sending file ... " + filename);
				
				byte[] mb = new byte[8 * 1024];
				
				for(int c = in.read(mb); c > -1; c= in.read(mb)){ 
					oos.write(mb, 0, c); 
				}
			}
		}
		
		oos.close();
		oois.close();
		socket.close();
	}
	private File searchFileSystem(File file, String search){
		
		if(file.getName().toLowerCase().contains(search))
			return file;
		else
			return null;
	}
}
