package com.netwire.modal;

import java.io.Serializable;

public class FileContent implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8638736701838579811L;
	
	private String fileName;

	private byte[] filebytes;
	
	private int off;
	
	private int len;
	
	public FileContent(){
		
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}



	public byte[] getFilebytes() {
		return filebytes;
	}

	public void setFilebytes(byte[] filebytes) {
		this.filebytes = filebytes;
	}

	public int getOff() {
		return off;
	}

	public void setOff(int off) {
		this.off = off;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}
	
	

}
