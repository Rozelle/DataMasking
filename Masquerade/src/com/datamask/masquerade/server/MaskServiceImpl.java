package com.datamask.masquerade.server;

import static com.datamask.masquerade.server.ConstantValues.RULE_CREDITCARDNULL;
import static com.datamask.masquerade.server.ConstantValues.RULE_CREDITCARDRANDOM;
import static com.datamask.masquerade.server.ConstantValues.RULE_FIRSTNAMESUBSTITUTION;
import static com.datamask.masquerade.server.ConstantValues.RULE_FULLNAMESUBSTITUTION;
import static com.datamask.masquerade.server.ConstantValues.RULE_LASTMASKEDVALUE;
import static com.datamask.masquerade.server.ConstantValues.RULE_LASTNAMESUBSTITUTION;
import static com.datamask.masquerade.server.ConstantValues.RULE_NONE;
import static com.datamask.masquerade.server.ConstantValues.RULE_ORIGINALVALUE;
import static com.datamask.masquerade.server.ConstantValues.RULE_RANDOMSUBSTITUTION;
import static com.datamask.masquerade.server.ConstantValues.RULE_RIGHTPARTNULLING;
import static com.datamask.masquerade.server.ConstantValues.RULE_SHUFFLE;
import static com.datamask.masquerade.server.ConstantValues.RULE_EMAILNULL;
import static com.datamask.masquerade.server.ConstantValues.SCHEMA_NAME;
import static com.datamask.masquerade.server.ConstantValues.SCHEMA_TABLE_NAME;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.datamask.masquerade.client.MaskService;
import com.datamask.masquerade.shared.MaskingDetails;
import com.datamask.masquerade.shared.SchemaConnection;
import com.datamask.masquerade.shared.TableDescription;

/**
 * The server-side implementation of the RPC service.
 */

public class MaskServiceImpl extends RemoteServiceServlet implements MaskService {
	private static final long serialVersionUID = 1L;
	Connection connection;
	ArrayList<ArrayList<String>> maskedTable;

	private static final String IPADDRESS_COLUMN = "ipaddress";
	private static final String PORT_COLUMN = "port";
	private static final String SCHEMA_COLUMN = "schemaname";
	private static final String USERNAME_COLUMN = "username";
	private static final String PASSWORD_COLUMN = "password";

	private void getConnection(SchemaConnection schemaConnection) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://" + schemaConnection.getIpAddress() + ":" + schemaConnection.getPort() + "/"
				+ schemaConnection.getSchemaName();
		String user = schemaConnection.getUserName();
		String pass = schemaConnection.getPassword();

		connection = DriverManager.getConnection(url, user, pass);

	}

	@Override
	public ArrayList<SchemaConnection> getSchemaConnection() throws Exception {
		ArrayList<SchemaConnection> arrayList = new ArrayList<SchemaConnection>();
		SchemaConnection schemaConnection;
		Connection sqliteConnection;
		Class.forName("org.sqlite.JDBC");

		sqliteConnection = DriverManager.getConnection("jdbc:sqlite:" + SCHEMA_NAME);
		Statement statement = sqliteConnection.createStatement();

		ResultSet resultSet = statement.executeQuery("select * from " + SCHEMA_TABLE_NAME);
		while (resultSet.next()) {
			schemaConnection = new SchemaConnection(resultSet.getString(IPADDRESS_COLUMN),
					resultSet.getString(PORT_COLUMN), resultSet.getString(SCHEMA_COLUMN),
					resultSet.getString(USERNAME_COLUMN), resultSet.getString(PASSWORD_COLUMN));
			arrayList.add(schemaConnection);
		}
		if (sqliteConnection != null)
			sqliteConnection.close();
		return arrayList;
	}

	@Override
	public ArrayList<TableDescription> getTables(SchemaConnection schemaConnection) throws Exception {
		getConnection(schemaConnection);

		if (connection == null) {
			return null;
		}
		ArrayList<TableDescription> tableDescriptionList = new ArrayList<TableDescription>();

		TableDescription tableDescription;
		String[] columnDescription;
		ArrayList<ArrayList<String>> ruleSuggestionList;
		ArrayList<String[]> columns;
		ArrayList<String> columnRuleSuggestions;

		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("show tables");
		while (resultSet.next()) {
			String table = resultSet.getString(1);
			if (table.startsWith("masked"))
				continue;

			TableKeys tableKeys = new TableKeys(table, connection.getMetaData());
			ArrayList<String> foreignKeyList = tableKeys.getForeignKeys();
			ArrayList<String> primaryKeyList = tableKeys.getPrimaryKeys();

			Statement tableDescriptionStatement = connection.createStatement();
			ResultSet resultSetTableDescription = tableDescriptionStatement.executeQuery("select * from " + table);
			ResultSetMetaData resultSetMetaData = resultSetTableDescription.getMetaData();
			int columnCount = resultSetMetaData.getColumnCount();

			columns = new ArrayList<String[]>();
			ruleSuggestionList = new ArrayList<ArrayList<String>>();
			for (int i = 0; i < columnCount; i++) {
				columnDescription = new String[2];
				columnRuleSuggestions = new ArrayList<String>();

				columnDescription[0] = resultSetMetaData.getColumnName(i + 1);
				columnDescription[1] = resultSetMetaData.getColumnTypeName(i + 1).toLowerCase();
				columns.add(columnDescription);

				// checks if the column is a foreign key or not, if so masking
				// cannot be done
				if (foreignKeyList.contains(resultSetMetaData.getColumnName(i + 1))) {
					foreignKeyList.remove(resultSetMetaData.getColumnName(i + 1));
					columnRuleSuggestions.add(RULE_NONE);
				}
				// checks if the column is a primary key or not, if so some
				// rules cannot be applied
				else if (primaryKeyList.contains(resultSetMetaData.getColumnName(i + 1))) {
					columnRuleSuggestions.add(RULE_ORIGINALVALUE);
					columnRuleSuggestions.add(RULE_LASTMASKEDVALUE);
					columnRuleSuggestions.add(RULE_SHUFFLE);
				} else {
					columnRuleSuggestions.add(RULE_ORIGINALVALUE);
					columnRuleSuggestions.add(RULE_LASTMASKEDVALUE);
					columnRuleSuggestions.add(RULE_SHUFFLE);
					columnRuleSuggestions.add(RULE_RANDOMSUBSTITUTION);
					columnRuleSuggestions.add(RULE_CREDITCARDRANDOM);
					columnRuleSuggestions.add(RULE_FIRSTNAMESUBSTITUTION);
					columnRuleSuggestions.add(RULE_LASTNAMESUBSTITUTION);
					columnRuleSuggestions.add(RULE_FULLNAMESUBSTITUTION);
					columnRuleSuggestions.add(RULE_RIGHTPARTNULLING);
					columnRuleSuggestions.add(RULE_CREDITCARDNULL);
					columnRuleSuggestions.add(RULE_EMAILNULL);
				}
				ruleSuggestionList.add(columnRuleSuggestions);
			}
			tableDescription = new TableDescription(table, columns, ruleSuggestionList);
			tableDescriptionList.add(tableDescription);
		}
		return tableDescriptionList;
	}

	@Override
	public boolean maskSchema(MaskingDetails[] maskingDetailsArray) throws Exception {
		boolean flag = true;
		for (int i = 0; i < maskingDetailsArray.length; i++) {
			flag = updateMaskedTable(maskingDetailsArray[i].getSchemaConnection(),
					maskingDetailsArray[i].getTableName(), maskingDetailsArray[i].getColumnDetails());
			if (flag == false)
				break;
		}
		return flag;
	}

	private boolean updateMaskedTable(SchemaConnection schemaConnection, String tableName,
			ArrayList<ArrayList<String>> maskingTableColumns) {
		try {
			if (connection != null)
				connection.close();
			getConnection(schemaConnection);
			
			getMaskedTable(schemaConnection, tableName, maskingTableColumns);

			final String lineSeparator = System.getProperty("line.separator");
			StringBuilder sb = new StringBuilder();
			int i, j;
			// now append masked data in a loop
			for (i = 0; i < maskedTable.get(0).size(); i++) {
				for (j = 0; j < maskedTable.size() - 1; j++) {
					sb.append(maskedTable.get(j).get(i) + ",");
				}
				sb.append(maskedTable.get(j).get(i) + lineSeparator);
			}
			// now write to file
			File f = new File("masked.csv");
			Files.write(Paths.get(f.getAbsolutePath()), sb.toString().getBytes());

			Statement statement = connection.createStatement();
			statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
			statement.executeUpdate("TRUNCATE masked" + tableName);
			// write data into mysql as new table called "masked'table-name'
			statement.executeQuery("LOAD DATA LOCAL INFILE 'masked.csv' INTO TABLE masked" + tableName
					+ " FIELDS TERMINATED BY ',' LINES TERMINATED BY '" + lineSeparator + "' ;");
			statement.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");

			f.delete();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void getMaskedTable(SchemaConnection schemaConnection, String tableName,
			ArrayList<ArrayList<String>> maskingTableColumns) throws Exception {
		maskedTable = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < maskingTableColumns.size(); i++) {
			ArrayList<String> column = getMaskedColumn(tableName, maskingTableColumns.get(i).get(0),
					maskingTableColumns.get(i).get(1), maskingTableColumns.get(i).get(2));
			maskedTable.add(column);
			
		}
	}

	private ArrayList<String> getMaskedColumn(String tableName, String columnName, String dataType, String rule)
			throws Exception {
		ArrayList<String> maskedColumn = new ArrayList<String>();
		switch (rule) {
		case RULE_NONE:
		case RULE_LASTMASKEDVALUE:
			maskedColumn = new LastMaskedValues(connection, tableName, columnName).getList();
			break;
		case RULE_ORIGINALVALUE:
			maskedColumn = new OriginalValues(connection, tableName, columnName).getList();
			break;
		case RULE_SHUFFLE:
			maskedColumn = new Shuffle(connection, tableName, columnName).getList();
			break;
		case RULE_RANDOMSUBSTITUTION:
			maskedColumn = new RandomSubstitution(connection, tableName, columnName, dataType).getList();
			break;
		case RULE_CREDITCARDRANDOM:
			maskedColumn = new CreditCardRandom(connection, tableName, columnName).getList();
			break;
		case RULE_FIRSTNAMESUBSTITUTION:
			maskedColumn = new FirstNameSubstitution(connection, tableName, columnName).getList();
			break;
		case RULE_LASTNAMESUBSTITUTION:
			maskedColumn = new LastNameSubstitution(connection, tableName, columnName).getList();
			break;
		case RULE_FULLNAMESUBSTITUTION:
			maskedColumn = new FullNameSubstitution(connection, tableName, columnName).getList();
			break;
		case RULE_RIGHTPARTNULLING:
			maskedColumn = new RightPartNulling(connection, tableName, columnName).getList();
			break;
		case RULE_CREDITCARDNULL:
			maskedColumn = new CreditCardNull(connection, tableName, columnName).getList();
			break;
		case RULE_EMAILNULL:
			maskedColumn = new EmailNull(connection, tableName, columnName).getList();
			break;
		}
		return maskedColumn;
	}
}