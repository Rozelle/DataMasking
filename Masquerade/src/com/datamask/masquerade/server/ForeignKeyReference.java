package com.datamask.masquerade.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.mysql.jdbc.DatabaseMetaData;

public class ForeignKeyReference {

	
	    private static Connection con;
	    private static String tableName;
	    private static DatabaseMetaData meta;
	    ForeignKeyReference(Connection c,String tn) throws Exception
	    {
	         con = c;
	         tableName = tn;
	         try
	         {
	             meta= (DatabaseMetaData) con.getMetaData();
	         }
	         catch(Exception e)
	         {
	            throw e;
	         }
	    }
	    
	    //returns an arraylist of foreign keys
	    public ArrayList<String> getForeignKeys() throws Exception
	    {
	        ArrayList<String> fk=new ArrayList<String>();
	        try
	        {
	           ResultSet keys = meta.getImportedKeys(null, null, tableName);
	           while(keys.next())
	                fk.add(keys.getString(8));
	        }
	        catch(Exception e)
	        {
	            throw e;
	        }
	        return fk;
	    }
	    
	    //returns Foreign Key references: tableName(columnName)
	    public String getReferences(String columnName) throws Exception
	    {
	        String ref=new String();
	        try
	        {
	           ResultSet keys = meta.getImportedKeys(null, null, tableName);
	           while(keys.next())
	                if(columnName.equalsIgnoreCase(keys.getString(8)))
	                {
	                    ref=(keys.getString(3)+"("+keys.getString(4)+")");
	                }
	        }
	        catch(Exception e)
	        {
	            throw e;
	        }
	        return ref;
	    }
	}

