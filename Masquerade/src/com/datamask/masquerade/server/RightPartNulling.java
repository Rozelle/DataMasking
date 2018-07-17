package com.datamask.masquerade.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RightPartNulling {
	Connection connection;
	String tableName, columnName;

	public RightPartNulling(Connection connection, String tableName, String columnName) {
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

			String originalValue = new String(resultSet.getString(1));

			int length = originalValue.length();
			String nulledValue = originalValue.substring(0, length / 3);

			for (int i = length / 3; i < length; i++) {
				if (Character.isLetterOrDigit(originalValue.charAt(i)))
					nulledValue += "x";
				else
					nulledValue += originalValue.charAt(i);
			}

			maskedColumn.add(nulledValue);
		}

		return maskedColumn;
	}

}
