package com.datamask.masquerade.server;

import static com.datamask.masquerade.server.ConstantValues.LOGIN_TABLE_NAME;
import static com.datamask.masquerade.server.ConstantValues.SCHEMA_NAME;
import static com.datamask.masquerade.server.ConstantValues.ADMIN_USERNAME;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.datamask.masquerade.client.SettingService;

/**
 * The server-side implementation of the RPC service.
 */

public class SettingServiceImpl extends RemoteServiceServlet implements SettingService, IsSerializable {
	private static final long serialVersionUID = 1L;
	private static final String USERNAME_COLUMN = "uname";
	private static final String PASSWORD_COLUMN = "upass";

	public boolean changePassword(String oldPassword, String newPassword) throws IllegalArgumentException {

		Connection connection = null;

		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:" + SCHEMA_NAME);
			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery("select " + PASSWORD_COLUMN + " from " + LOGIN_TABLE_NAME + " where "
					+ USERNAME_COLUMN + " = 'admin'");

			if (oldPassword.equals(rs.getString(PASSWORD_COLUMN))) {
				connection.close();
				updateTable(newPassword);
				return true;
			}

			else {
				if (connection != null)
					connection.close();
				return false;
			}
		}

		catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	private void updateTable(String newPassword) throws Exception {
		Class.forName("org.sqlite.JDBC");
		Connection connection = DriverManager.getConnection("jdbc:sqlite:" + SCHEMA_NAME);
		Statement statementUpdate = connection.createStatement();

		statementUpdate.executeUpdate(
				"delete from " + LOGIN_TABLE_NAME + " where " + USERNAME_COLUMN + " = '" + ADMIN_USERNAME + "'");
		statementUpdate.executeUpdate(
				"insert into " + LOGIN_TABLE_NAME + " values('" + ADMIN_USERNAME + "', '" + newPassword + "')");

		connection.close();
	}
	// connection to database for adding new user

	public String addUser(String newUserName, String newUserPassword) throws Exception {

		Connection connection = null;

		Class.forName("org.sqlite.JDBC");

		connection = DriverManager.getConnection("jdbc:sqlite:" + SCHEMA_NAME);
		Statement statement = connection.createStatement();

		ResultSet rs = statement.executeQuery("select " + USERNAME_COLUMN + " from " + LOGIN_TABLE_NAME);

		while (rs.next()) {
			if (rs.getString(1).equals(newUserName)) {
				connection.close();
				return "Username already exists!! Retry";
			}
		}
		Statement statementUpdate = connection.createStatement();
		statementUpdate.executeUpdate(
				"insert into " + LOGIN_TABLE_NAME + " values('" + newUserName + "' , '" + newUserPassword + "')");

		connection.close();

		return "Successfully Added";
	}

	// connection to database for deleting user entry
	public String deleteUser(String deleteUserName, String deleteUserPassword) throws Exception {

		Connection connection = null;

		Class.forName("org.sqlite.JDBC");

		connection = DriverManager.getConnection("jdbc:sqlite:" + SCHEMA_NAME);
		Statement statement = connection.createStatement();

		ResultSet rs = statement.executeQuery("select * from " + LOGIN_TABLE_NAME);
		boolean flag = false;
		while (rs.next()) {
			if (deleteUserName.equals(rs.getString(USERNAME_COLUMN))
					&& deleteUserPassword.equals(rs.getString(PASSWORD_COLUMN))) {
				flag = true;
				break;
			}
		}
		if (flag == true) {
			Statement statementUpdate = connection.createStatement();
			statementUpdate.executeUpdate("delete from " + LOGIN_TABLE_NAME + " where " + USERNAME_COLUMN +" = '"+ deleteUserName +"'");
			connection.close();
		} else {
			connection.close();
			return "Username not Found !!Please retry";
		}
		return "Successfully deleted!";
	}

}
