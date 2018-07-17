package com.datamask.masquerade.server;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;

public class TableKeys {
	private String tableName;
	private DatabaseMetaData metaData;

	public TableKeys(String tableName, DatabaseMetaData metaData) throws Exception {
		this.tableName = tableName;
		this.metaData = metaData;
	}

	// returns an arraylist of primary keys
	public ArrayList<String> getPrimaryKeys() throws Exception {
		ArrayList<String> primaryKeyList = new ArrayList<String>();
		try {
			ResultSet keys = metaData.getPrimaryKeys(null, null, tableName);
			while (keys.next())
				primaryKeyList.add(keys.getString("COLUMN_NAME"));
		} catch (Exception e) {
			throw e;
		}
		return primaryKeyList;
	}
	
	//returns an arraylist of foreign keys
    public ArrayList<String> getForeignKeys() throws Exception
    {
        ArrayList<String> foreignKeyList=new ArrayList<String>();
        try
        {
           ResultSet keys = metaData.getImportedKeys(null, null, tableName);
           while(keys.next())
                foreignKeyList.add(keys.getString(8));
        }
        catch(Exception e)
        {
            throw e;
        }
        return foreignKeyList;
    }
}
