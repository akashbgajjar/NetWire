package com.netwire.modal;

import java.io.Serializable;

public class ActiveClient implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4513624082188299810L;
	
	private String clientName = null;
	
	private int status = 0;
	
	private String IPAddress = null;
	
	private int port = 0;
	
	public ActiveClient(){
		
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getIPAddress() {
		return IPAddress;
	}

	public void setIPAddress(String address) {
		IPAddress = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public boolean isEmpty(){
		if(this.clientName == null &&
				this.IPAddress == null &&
				this.status == 0 &&
				this.port == 0)
			return true;
		return false;
	}
	

}
