package com.datamask.masquerade.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class TableDescription implements Serializable{
	private static final long serialVersionUID = 1L;
	private String tableName;
	private ArrayList<String[]> columns;
	private ArrayList<ArrayList<String>> ruleSuggestionList;
	
	public TableDescription() {}
	public TableDescription(String tableName, ArrayList<String[]> columns, ArrayList<ArrayList<String>> ruleSuggestionList) {
		this.tableName = tableName;
		this.columns = columns;
		this.ruleSuggestionList = ruleSuggestionList;
	}
	public String getTableName() {
		return tableName;
	}
	public ArrayList<String[]> getColumns() {
		return columns;
	}
	public ArrayList<ArrayList<String>> getRuleSuggestionList() {
		return ruleSuggestionList;
	}
}
