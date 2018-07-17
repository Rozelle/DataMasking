package com.datamask.masquerade.server;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import static com.datamask.masquerade.server.ConstantValues.FIRSTNAMES_FILE;

public class FirstNameSubstitution {
	Connection connection;
	String tableName, columnName;

	public FirstNameSubstitution(Connection connection, String tableName, String columnName) {
		this.connection = connection;
		this.tableName = tableName;
		this.columnName = columnName;
	}

	@SuppressWarnings({ "resource", "unchecked" })
	public ArrayList<String> getList() throws Exception {
		ArrayList<String> maskedColumn = new ArrayList<String>();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select " + columnName + " from " + tableName);
        
		ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(FIRSTNAMES_FILE));
        ArrayList<String> arrayList;
        arrayList= (ArrayList<String>) inputStream.readObject(); 	  
        for(int i=0;resultSet.next() == true;i++)
        {
        	if(resultSet.wasNull()) {
      			maskedColumn.add("\\N");
      			i--;
        	}
        	else if(i < 50000)
        		maskedColumn.add(arrayList.get(i));
          	else
          		maskedColumn.add(arrayList.get((int) (Math.random() * 50000 + 0)));
        }
        Collections.shuffle(maskedColumn);
		return maskedColumn;
	}
}
