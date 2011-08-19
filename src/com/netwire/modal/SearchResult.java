package com.netwire.modal;

import java.net.Socket;

public class SearchResult {
	
	private Socket socket;
	
	private Search search;
	
	public SearchResult(Socket socket, Search search){
		this.socket = socket;
		this.search = search;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Search getSearch() {
		return search;
	}

	public void setSearch(Search search) {
		this.search = search;
	}

}
