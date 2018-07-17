package com.datamask.masquerade.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ConfigurationServiceAsync {
	
	void getConList(AsyncCallback<ArrayList<ArrayList<String>>> callback)
			throws IllegalArgumentException;
		  
	void getDestList(AsyncCallback<ArrayList<String>> asyncCallback)
			throws IllegalArgumentException;

	void removeCon(String removeConName, AsyncCallback<Boolean> callback) 
			throws IllegalArgumentException;
	
	void removeDest(String removeName, AsyncCallback<Boolean> asyncCallback)
			throws IllegalArgumentException;

	void testNewCon(String newCoc[], AsyncCallback<String> callback) 
			throws IllegalArgumentException;
	
	void testNewDest(String newCoc[], AsyncCallback<String> callback) 
			throws IllegalArgumentException;	
	
	
	void addNewCon(String newCon[], AsyncCallback<String> callback) 
			throws IllegalArgumentException;
	
	void addDestCon(String newCon[], AsyncCallback<String> callback) 
			throws IllegalArgumentException;

	void updateCon(String[] newCon, AsyncCallback<String> asyncCallback)
			throws IllegalArgumentException;

	void updateDest(String[] newCon, AsyncCallback<String> asyncCallback)
			throws IllegalArgumentException;
}
