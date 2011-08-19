package com.netwire.modal;

import java.io.Serializable;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2504040640148060575L;
	
	private String name = null;
	
	private boolean status = false;
	
	public User(){
		
	}
	
	public User(String name, boolean status){
		this.name = name;
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}
