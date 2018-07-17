package com.datamask.masquerade.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CreditCardNull {
	Connection connection;
	String tableName, columnName;

	public CreditCardNull(Connection connection, String tableName, String columnName) {
		this.connection = connection;
		this.tableName = tableName;
		this.columnName = columnName;
	}

	public ArrayList<String> getList() throws SQLException {
		ArrayList<String> maskedColumn = new ArrayList<String>();
		Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select " + columnName + " from " + tableName);
               
        while(resultSet.next())
        {
        	String originalCardNumber = new String(resultSet.getString(1));
        	
        	if(resultSet.wasNull())
			{
				maskedColumn.add("\\N");
				continue;
			}
        	
            int length = originalCardNumber.length();
            String nulledCreditCardNumber = "";
			int count=0;
			for(int i=0; i<length; i++){
				if(count<4 && Character.isDigit(originalCardNumber.charAt(i)))
				{
					nulledCreditCardNumber+=originalCardNumber.charAt(i);
					count++;
				}
				else if(!Character.isDigit(originalCardNumber.charAt(i))) {
					nulledCreditCardNumber+=originalCardNumber.charAt(i);
				}
				else
					nulledCreditCardNumber+="x";	
			} 
            maskedColumn.add(nulledCreditCardNumber);
        }
		return maskedColumn;
	}

}
