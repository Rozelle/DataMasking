package com.datamask.masquerade.server;

import com.datamask.masquerade.client.LoginService;
import static com.datamask.masquerade.server.ConstantValues.LOGIN_TABLE_NAME;
import static com.datamask.masquerade.server.ConstantValues.ADMIN_USERNAME;
import static com.datamask.masquerade.server.ConstantValues.DEFAULT_ADMIN_PASSWORD;
import static com.datamask.masquerade.server.ConstantValues.SCHEMA_NAME;

import java.sql.*;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {
	private static final long serialVersionUID = 1L;
	
	final static String USERNAME_COLUMN = "uname";
	final static String PASSWORD_COLUMN = "upass";

	public boolean logintoServer(String login, String password) throws IllegalArgumentException {
		Connection connection = null;
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + SCHEMA_NAME);
			Statement statement = connection.createStatement();
			
			ResultSet resultTable = statement.executeQuery(
					"SELECT count(*) FROM sqlite_master WHERE type='table' AND name='" + LOGIN_TABLE_NAME + "'");
			int i = resultTable.getInt(1);
			if (i == 0) // create new table and insert
			{
				statement.executeUpdate("create table " + LOGIN_TABLE_NAME + " (" + USERNAME_COLUMN + " String, "
						+ PASSWORD_COLUMN + " String, UNIQUE(" + USERNAME_COLUMN + "))");
				statement.executeUpdate("insert into " + LOGIN_TABLE_NAME + " values ('" + ADMIN_USERNAME + "' , '"
						+ DEFAULT_ADMIN_PASSWORD + "')");
			}
			ResultSet rs = statement.executeQuery(
					"select * from " + LOGIN_TABLE_NAME + " where " + USERNAME_COLUMN + " = '" + login + "'");
			boolean flag = false;
			while (rs.next()) {
				if (login.equals(rs.getString(USERNAME_COLUMN)) && password.equals(rs.getString(PASSWORD_COLUMN))) {
					flag = true;
					break;
				}
			}
			return flag;
		}

		catch (Exception e) {
			return false;
		}

		finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

}
