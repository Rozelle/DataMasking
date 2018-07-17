package com.datamask.masquerade.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

public class Shuffle {
	Connection connection;
	String tableName, columnName;
	
	public Shuffle(Connection connection, String tableName, String columnName) {
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
        		resultSet.getString(1);
        		if(resultSet.wasNull())
			       maskedColumn.add("\\N");
        		else
               maskedColumn.add(resultSet.getString(1));
        }

        Collections.shuffle(maskedColumn);
        Collections.shuffle(maskedColumn);
        return maskedColumn;
	}
}
