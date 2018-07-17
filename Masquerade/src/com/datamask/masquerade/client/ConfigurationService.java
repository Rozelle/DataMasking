package com.datamask.masquerade.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("configurationservice")
public interface ConfigurationService extends RemoteService
{
	 ArrayList<ArrayList<String>> getConList() throws IllegalArgumentException;
	 
	 ArrayList<String> getDestList() throws IllegalArgumentException;

	 boolean removeCon(String removeConName) throws IllegalArgumentException;

	 boolean removeDest(String removeConName) throws IllegalArgumentException;
	 
	 String testNewCon(String newCon[]) throws IllegalArgumentException;
	 
	 String testNewDest(String newCon[]) throws IllegalArgumentException;

	 String addNewCon(String newCon[]) throws IllegalArgumentException;
	 
	 String addDestCon(String newCon[]) throws IllegalArgumentException;
	 
	 String updateCon(String newCon[]) throws IllegalArgumentException;
	 
	 String updateDest(String newCon[]) throws IllegalArgumentException;

}

