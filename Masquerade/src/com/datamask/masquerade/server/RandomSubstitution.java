package com.datamask.masquerade.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RandomSubstitution {
	Connection connection;
	String tableName, columnName, dataType;

	public RandomSubstitution(Connection connection, String tableName, String columnName, String dataType) {
		this.connection = connection;
		this.tableName = tableName;
		this.columnName = columnName;
		this.dataType = dataType;
	}

	public ArrayList<String> getList() throws SQLException {
		ArrayList<String> maskedColumn = new ArrayList<String>();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select " + columnName + " from " + tableName);
		dataType = dataType.toUpperCase();
		switch (dataType) {
		case "VARCHAR":
			char alphabets[] = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
					'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
			while (resultSet.next()) {
				resultSet.getString(1);
				if (resultSet.wasNull()) {
					maskedColumn.add("\\N");
					continue;
				}
				String originalString = new String(resultSet.getString(1));
				int length = originalString.length();
				String substitutedString = originalString.substring(0, length / 3);

				for (int i = length / 3; i < length; i++) {
					if (Character.isDigit(originalString.charAt(i)))
						substitutedString += (int) (Math.random() * 10 + 0);
					else if(Character.isLetter(originalString.charAt(i)))
					{
						if (Character.isLowerCase(originalString.charAt(i)))
							substitutedString += alphabets[(int) (Math.random() * 26 + 0)];
						else
							substitutedString += (char) (alphabets[(int) (Math.random() * 26 + 0)]-32);
					}
					else
						substitutedString += (char) originalString.charAt(i);
				}
				maskedColumn.add(substitutedString);
			}
			break;

		case "DATE":
			StringBuffer stringBuffer;
			int part1, part2;
			String substitutedPart1, substitutedPart2;
			
			while (resultSet.next()) {
				resultSet.getString(1);
				if (resultSet.wasNull()) {
					maskedColumn.add("\\N");
					continue;
				}
				stringBuffer = new StringBuffer(resultSet.getString(1));

				part1 = (int) (Math.random() * 12 + 1);
				part2 = (int) (Math.random() * 28 + 1);

				substitutedPart1 = (String.valueOf(part1).length() < 2) ? 0 + String.valueOf(part1) : String.valueOf(part1);
				substitutedPart2 = (String.valueOf(part2).length() < 2) ? 0 + String.valueOf(part2) : String.valueOf(part2);

				stringBuffer = stringBuffer.replace(5, 7, substitutedPart1);
				stringBuffer = stringBuffer.replace(8, 10, substitutedPart2);

				maskedColumn.add(stringBuffer.toString());
			}
			break;

		case "TINYINT":
		case "SMALLINT":
		case "BIGINT":
		case "INT":
		case "DECIMAL":
		case "NUMERIC":
		case "FLOAT":
		case "REAL":
		case "DOUBLE":
			while (resultSet.next()) {
				resultSet.getString(1);

				if (resultSet.wasNull()) {
					maskedColumn.add("\\N");
					continue;
				}

				String originalNumber = resultSet.getString(1);
				String substitutedNumber = "";

				for (int j = 0; j < originalNumber.length(); j++) {
					if (Character.isDigit(originalNumber.charAt(j))) {
						if (j == 0)
							substitutedNumber += (int) (Math.random() * 9 + 1);
						else
							substitutedNumber += (int) (Math.random() * 10 + 0);
					} else
						substitutedNumber += originalNumber.charAt(j);
				}
				maskedColumn.add(substitutedNumber);
			}
			break;
		}// switch ends
		return maskedColumn;
	}

}
