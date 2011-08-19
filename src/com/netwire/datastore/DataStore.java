package com.netwire.datastore;

import java.util.HashMap;
import java.util.Vector;

import com.netwire.modal.ActiveClient;
import com.netwire.modal.SearchResult;

public class DataStore {
	
	public static ActiveClient activeClient = null;

	public final static HashMap<String, ActiveClient> map = new HashMap<String, ActiveClient>();
	
	public final static Vector<SearchResult> vector = new Vector<SearchResult>();
	
}
