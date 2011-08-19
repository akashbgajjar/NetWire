package com.netwire.handler;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;

import android.os.Handler;

import com.netwire.datastore.DataStore;
import com.netwire.modal.ActiveClient;
import com.netwire.modal.Search;

public class SearchThreadPool {
	
	public void searchInAvailableClients(String search, Handler handler) throws UnknownHostException, IOException, ClassNotFoundException{
		if(!DataStore.map.isEmpty()){
			Iterator<String> itr = DataStore.map.keySet().iterator();
			while(itr.hasNext()){
				String ipAddress = itr.next();
				ActiveClient client = DataStore.map.get(ipAddress);
				
				Search _search = new Search();
				_search.setFilename(search);
				_search.setFilesize("");
				_search.setStatus(false);
				
				Socket socket = new Socket(client.getIPAddress(), 7777);
				Thread thread = new Thread(new SearchHandler(socket, _search, handler));
				thread.start();
			}
		}
	}

}
