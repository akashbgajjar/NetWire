package com.netwire.handler.streams;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;

import com.netwire.modal.FileContent;

public class DownloadInputStreamHandler {
	
	private ObjectInputStream ois;
	
	private FileContent fileContent;
	
	public DownloadInputStreamHandler(Socket socket) throws StreamCorruptedException, IOException{
		this.ois = new ObjectInputStream(socket.getInputStream());
	}
	
	public FileContent readFileContent(){
		try{
			if((fileContent = (FileContent) ois.readObject()) != null){
				return fileContent;
			}
			return null;
		}catch(Exception e){
			return null;
		}
	}
	
	public void close() throws IOException{
		ois.close();
	}

}
