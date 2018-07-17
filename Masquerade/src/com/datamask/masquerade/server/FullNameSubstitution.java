package com.datamask.masquerade.server;

import static com.datamask.masquerade.server.ConstantValues.FIRSTNAMES_FILE;
import static com.datamask.masquerade.server.ConstantValues.LASTNAMES_FILE;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

public class FullNameSubstitution {
	Connection connection;
	String tableName, columnName;

	public FullNameSubstitution(Connection connection, String tableName, String columnName) {
		this.connection = connection;
		this.tableName = tableName;
		this.columnName = columnName;
	}

	@SuppressWarnings({ "resource", "unchecked" })
	public ArrayList<String> getList() throws Exception {
		ArrayList<String> maskedColumn = new ArrayList<String>();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select " + columnName + " from " + tableName);

		ObjectInputStream inputStreamFirstName = new ObjectInputStream(new FileInputStream(FIRSTNAMES_FILE));
		ObjectInputStream inputStreamLastName = new ObjectInputStream(new FileInputStream(LASTNAMES_FILE));

		ArrayList<String> arrayListFirstName;
		arrayListFirstName = (ArrayList<String>) inputStreamFirstName.readObject();

		ArrayList<String> arrayListLastName;
		arrayListLastName = (ArrayList<String>) inputStreamLastName.readObject();

		for (int i = 0; resultSet.next() == true; i++) {
			if (resultSet.wasNull()) 
			{
				maskedColumn.add("\\N");
				i--;
			} 
			else if (i < 50000)
				maskedColumn.add(arrayListFirstName.get(i) + " " + arrayListLastName.get(i));
			else
				maskedColumn.add(arrayListFirstName.get((int) (Math.random() * 50000 + 0)) + " "
						+ arrayListLastName.get((int) (Math.random() * 50000 + 0)));
		}
		Collections.shuffle(maskedColumn);
		return maskedColumn;
	}
}
