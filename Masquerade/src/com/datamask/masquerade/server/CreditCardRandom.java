package com.datamask.masquerade.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CreditCardRandom {
	Connection connection;
	String tableName, columnName;

	public CreditCardRandom(Connection connection, String tableName, String columnName) {
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
			String originalCardNumber = resultSet.getString(1);
			// revove all charcters except the digits in the card number
			String creditCardNumberWithDigitsOnly = originalCardNumber.replaceAll("[^0-9.]", "");
			
			// First digit to be unchanged and last to be found by Luhn algorithm
			int length = creditCardNumberWithDigitsOnly.length() - 2; 
			StringBuilder builder = new StringBuilder(creditCardNumberWithDigitsOnly.substring(0, 1));
			
			// Generate len-2 digits random number
			for (int i = 0; i < length; i++)
				builder.append((int) (Math.random() * 10 + 0));

			// Lhun algorithm to find last valid digit of cc
			int sum = 0;
			for (int i = 0; i < builder.length(); i++) {
				// Get the digit at the current position.
				int digit = Integer.parseInt(builder.substring(i, (i + 1)));
				if ((i % 2) == 0) {
					digit = digit * 2;
					if (digit > 9)
						digit = (digit / 10) + (digit % 10);
				}
				sum += digit;
			}

			// The check digit is the number required to make the sum a multiple of 10
			int mod = sum % 10;
			builder.append((mod == 0) ? 0 : 10 - mod);

			for (int i = 0; i < originalCardNumber.length(); i++)
				if (!Character.isDigit(originalCardNumber.charAt(i)))
					builder.insert(i, originalCardNumber.charAt(i));

			maskedColumn.add(builder.toString());
		}
		return maskedColumn;
	}

}
