package com.datamask.masquerade.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.datamask.masquerade.client.ConfigurationService;

public class ConfigurationServiceImpl extends RemoteServiceServlet implements ConfigurationService {
	private static final long serialVersionUID = 1L;

	public ArrayList<ArrayList<String>> getConList() throws IllegalArgumentException {
		Connection connection = null;

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:event.db");

			Statement statement = connection.createStatement();

			statement.executeUpdate("create table if not exists schematable "
					+ "(ipaddress String, port String, schemaname String, username String, password String , UNIQUE(schemaname))");

			ResultSet rs = statement.executeQuery("select * from schematable");

			ArrayList<ArrayList<String>> result = new ArrayList<>();

			while (rs.next()) {
				ArrayList<String> row = new ArrayList<>(5);

				// host, port, schemaname, username, password)

				row.add(rs.getString("ipaddress"));

				row.add(rs.getString("port"));

				row.add(rs.getString("schemaname"));

				row.add(rs.getString("username"));

				row.add(rs.getString("password"));

				result.add(row);
			}

			return result;

		}

		catch (SQLException e) {
			System.err.println(e.getMessage());
		}

		finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
			}
		}

		return null;
	}

	/**********************************************************************************************/
	@Override
	public ArrayList<String> getDestList() throws IllegalArgumentException {
		Connection connection = null;

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:event.db");

			Statement statement = connection.createStatement();

			statement.executeUpdate("create table if not exists destinationtable "
					+ "(ipaddress String, port String, schemaname String, username String, password String , UNIQUE(schemaname))");

			ResultSet rs = statement.executeQuery("select * from destinationtable");

			ArrayList<String> result = new ArrayList<>(5);

			// host, port, schemaname, username, password)

			result.add(rs.getString("ipaddress"));

			result.add(rs.getString("port"));

			result.add(rs.getString("schemaname"));

			result.add(rs.getString("username"));

			result.add(rs.getString("password"));

			return result;
		}

		catch (SQLException e) {
			return null;
		}

		finally {
			try {
				if (connection != null)
					connection.close();
			}

			catch (SQLException e) {
				return null;
			}
		}
	}

	@Override
	public boolean removeCon(String conName) throws IllegalArgumentException {
		Connection connection = null;

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:event.db");
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("select * from schematable where schemaname = '" + conName + "'");

			String removeConnection[] = new String[5];
			removeConnection[0] = rs.getString(1);
			removeConnection[1] = rs.getString(2);
			removeConnection[2] = rs.getString(3);
			removeConnection[3] = rs.getString(4);
			removeConnection[4] = rs.getString(5);
			deleteMaskedTable(removeConnection); 
			statement.executeUpdate("delete from schematable where schemaname = '" + conName + "'");
			return true;
		} catch (Exception e) {
			return false;
		}
		finally
		{
			try{
				if(connection!=null)
					connection.close();
			}
			catch(Exception e) {}
		}
	}

	private void deleteMaskedTable(String[] removeConnection) throws SQLException, ClassNotFoundException {
		String jdbcDriver = "com.mysql.jdbc.Driver";
		Class.forName(jdbcDriver);
		Connection connection = DriverManager.getConnection("jdbc:mysql://" + removeConnection[0] + ":"
				+ removeConnection[1] + "/" + removeConnection[2] + "?characterEncoding=UTF-8&useSSL=false",
				removeConnection[3], removeConnection[4]);

		Statement stmt = connection.createStatement();

		stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
		ResultSet resultset = stmt.executeQuery("show tables");
		while (resultset.next()) {
			if (resultset.getString(1).startsWith("masked")) {
				stmt.executeUpdate("drop table " + resultset.getString(1));
			}

		}

		stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
		connection.close();

	}

	@Override
	public boolean removeDest(String destName) throws IllegalArgumentException {
		Connection connection = null;

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:event.db");

			Statement statement = connection.createStatement();

			statement.executeUpdate("delete from destinationtable where schemaname = '" + destName + "'");

			return true;
		} catch (Exception e) {
			return false;
		}

		finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
			}
		}
	}

	@Override
	public String testNewCon(String[] newCon) throws IllegalArgumentException {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");

			// schematable (ipaddress, port, schemaname, username, password)
			connection = DriverManager.getConnection("jdbc:mysql://" + newCon[0] + ":" + newCon[1] + "/" + newCon[2]
					+ "?characterEncoding=UTF-8&useSSL=false", newCon[3], newCon[4]);

			if (connection != null)
				return "successful";

			else
				return "Not successful";

		} catch (Exception e) {
			return e.toString();
		}

		finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
			}
		}
	}

	@Override
	public String testNewDest(String[] newCon) throws IllegalArgumentException {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// schematable (ipaddress, port, schemaname, username, password)
			connection = DriverManager.getConnection(
					"jdbc:mysql://" + newCon[0] + ":" + newCon[1] + "/" + "?characterEncoding=UTF-8&useSSL=false",
					newCon[3], newCon[4]);

			if (connection != null) {
				connection.close();
				return "successful";
			}

			else
				return "Not successful";

		} catch (Exception e) {
			return e.toString();
		}

		finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
			}
		}
	}

	@Override
	public String addNewCon(String[] newCon) throws IllegalArgumentException {
		Connection connection = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			// schematable (host, port, schemaname, username, password)

			connection = DriverManager.getConnection("jdbc:mysql://" + newCon[0] + ":" + newCon[1] + "/" + newCon[2]
					+ "?characterEncoding=UTF-8&useSSL=false", newCon[3], newCon[4]);

			if (connection != null) {
				connection.close();

				connection = null;

				connection = DriverManager.getConnection("jdbc:sqlite:event.db");

				Statement statement = connection.createStatement();

				statement.executeUpdate("insert into schematable " + "values('" + newCon[0] + "', '" + newCon[1]
						+ "', '" + newCon[2] + "', '" + newCon[3] + "', '" + newCon[4] + "')");

				// copying all tables from old schema to new masked schema
				connection.close();
				// copying all tables and renaming as masked tables
				createMaskedTables(newCon);

				return "Saved";
			}

			else
				return "Could not perform save successfully";

		} catch (Exception e) {
			return e.toString();
		}

		finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
			}

		}
	}
	// for creating masked tables in the same schema

	private void createMaskedTables(final String[] newCon) throws Exception {
		String jdbcDriver = "com.mysql.jdbc.Driver";

		Class.forName(jdbcDriver);
		Connection connection = DriverManager.getConnection("jdbc:mysql://" + newCon[0] + ":" + newCon[1] + "/"
				+ newCon[2] + "?characterEncoding=UTF-8&useSSL=false", newCon[3], newCon[4]);
		ResultSet rs;
		Statement stmt = connection.createStatement();
		Statement s = connection.createStatement();

		String tableName, query;
		ArrayList<String> fk = new ArrayList<String>();
		boolean flag = false;
		try {
			rs = stmt.executeQuery("show tables");
			while (rs.next()) {
				if (rs.getString(1).startsWith("masked")) {
					flag = true;
					break;
				}
			}

			if (flag == true)// already has the tables made thus no need to
								// create them
				return;

			// create tables with references
			rs = stmt.executeQuery("show tables");
			while (rs.next()) {
				// create table
				tableName = rs.getString(1);
				s.executeUpdate("CREATE TABLE masked" + tableName + " LIKE " + tableName);

				// adding values
				s.executeUpdate("INSERT INTO masked" + tableName + " SELECT * FROM " + tableName);
			}

			// creating references
			rs = stmt.executeQuery("show tables");
			while (rs.next()) {
				tableName = rs.getString(1);
				if (tableName.startsWith("masked"))
					continue;
				ForeignKeyReference obj = new ForeignKeyReference(connection, tableName);
				fk = obj.getForeignKeys();
				for (int j = 0; j < fk.size(); j++) {
					query = "ALTER TABLE masked" + tableName + " ADD FOREIGN KEY (" + fk.get(j) + ") REFERENCES masked"
							+ obj.getReferences(fk.get(j)) + ";";
					s.executeUpdate(query);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public String addDestCon(String[] newCon) throws IllegalArgumentException {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// schematable (host, port, schemaname, username, password)

			connection = DriverManager.getConnection(
					"jdbc:mysql://" + newCon[0] + ":" + newCon[1] + "/" + "?characterEncoding=UTF-8&useSSL=false",
					newCon[3], newCon[4]);

			if (connection != null) {
				connection.close();

				connection = null;

				connection = DriverManager.getConnection("jdbc:sqlite:event.db");

				Statement statement = connection.createStatement();

				statement.executeUpdate("insert into destinationtable " + "values('" + newCon[0] + "', '" + newCon[1]
						+ "', '" + newCon[2] + "', '" + newCon[3] + "', '" + newCon[4] + "')");

				return "Saved";
			}

			else
				return "Could not perform save successfully";

		} catch (Exception e) {
			return e.toString();
		}

		finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
			}
		}
	}

	@Override
	public String updateCon(String[] newCon) throws IllegalArgumentException {

		Connection connection = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			// schematable (host, port, schemaname, username, password)

			connection = DriverManager.getConnection("jdbc:mysql://" + newCon[0] + ":" + newCon[1] + "/" + newCon[2]
					+ "?characterEncoding=UTF-8&useSSL=false", newCon[3], newCon[4]);

			if (connection != null) {
				connection.close();

				connection = null;

				connection = DriverManager.getConnection("jdbc:sqlite:event.db");

				Statement statement = connection.createStatement();

				statement.executeUpdate("update destinationtable " + "set host = '" + newCon[0] + "' port = '"
						+ newCon[1] + "' username = '" + newCon[3] + "'  password = '" + newCon[4] + "'"
						+ "where schemaname = '" + newCon[2] + "'");

				return "Saved";
			}

			else
				return "Could not perform save successfully";

		} catch (Exception e) {
			return e.toString();
		}

		finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
			}
		}
	}

	@Override
	public String updateDest(String[] newCon) throws IllegalArgumentException {

		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// schematable (host, port, schemaname, username, password)

			connection = DriverManager.getConnection("jdbc:mysql://" + newCon[0] + ":" + newCon[1] + "/" + newCon[2]
					+ "?characterEncoding=UTF-8&useSSL=false", newCon[3], newCon[4]);

			if (connection != null) {
				connection.close();

				connection = null;

				connection = DriverManager.getConnection("jdbc:sqlite:event.db");

				Statement statement = connection.createStatement();

				statement.executeUpdate("update schematable " + "set host = '" + newCon[0] + "' port = '" + newCon[1]
						+ "' username = '" + newCon[3] + "'  password = '" + newCon[4] + "'" + "where schemaname = '"
						+ newCon[2] + "'");

				return "Saved";
			}

			else
				return "Could not perform save successfully";

		} catch (Exception e) {
			return e.toString();
		}

		finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
			}
		}
	}

}
