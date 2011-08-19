package com.netwire.modal;

import java.io.Serializable;

public class Search implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5741020387737793301L;
	
	private String filename;
	
	private String filesize;
	
	private boolean status;
	
	public Search(){
		
	}
	
	public Search(String filename, boolean status){
		this.filename = filename;
		this.status = status;
	}
	
	public Search(String filename, String filesize, boolean status){
		this.filename = filename;
		this.filesize = filesize;
		this.status = status;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getFilesize() {
		return filesize;
	}

	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}

}
