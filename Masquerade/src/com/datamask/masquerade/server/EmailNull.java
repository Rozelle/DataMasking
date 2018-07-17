package com.datamask.masquerade.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EmailNull {
	Connection connection;
	String tableName, columnName;

	public EmailNull(Connection connection, String tableName, String columnName) {
		this.connection = connection;
		this.tableName = tableName;
		this.columnName = columnName;
	}

	public ArrayList<String> getList() throws SQLException {
		ArrayList<String> maskedColumn = new ArrayList<String>();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select " + columnName + " from " + tableName);
		while (resultSet.next()) {
			resultSet.getString(1);
			if (resultSet.wasNull()) {
				maskedColumn.add("\\N");
				continue;
			}
			String originalEmail = resultSet.getString(1);
			String nulledEmailString = "xxx" + originalEmail.substring(originalEmail.indexOf('@'), originalEmail.length());
			maskedColumn.add(nulledEmailString);
		}
		return maskedColumn;
	}

}
