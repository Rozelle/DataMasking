package com.datamask.masquerade.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class MaskingDetails implements Serializable{
	private static final long serialVersionUID = 1L;
	SchemaConnection schemaConnection;
	String tableName;
	ArrayList<ArrayList<String>> columnDetails;
	
	public MaskingDetails() {}
	public MaskingDetails(SchemaConnection schemaConnection, String tableName, ArrayList<ArrayList<String>> columnDetails)
	{
		this.schemaConnection = schemaConnection;
		this.tableName = tableName;
		this.columnDetails = columnDetails;
	}
	public SchemaConnection getSchemaConnection()
	{
		return schemaConnection;
	}
	public String getTableName() {
		return tableName;
	}
	public ArrayList<ArrayList<String>> getColumnDetails()
	{
		return columnDetails;
	}
}
