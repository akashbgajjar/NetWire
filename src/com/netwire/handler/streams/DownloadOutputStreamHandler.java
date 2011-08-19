package com.netwire.handler.streams;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.netwire.modal.FileContent;

public class DownloadOutputStreamHandler {
	
	private ObjectOutputStream oos;
	
	public DownloadOutputStreamHandler(Socket socket) throws IOException{
		this.oos = new ObjectOutputStream(socket.getOutputStream());
	}

	public void writeFileContent(FileContent fileContent) throws IOException{
		oos.writeObject(fileContent);
		oos.flush();
	}
	
	public void close() throws IOException{
		this.oos.close();
	}
}
