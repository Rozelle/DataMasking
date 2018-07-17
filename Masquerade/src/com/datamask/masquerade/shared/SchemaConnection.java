package com.datamask.masquerade.shared;

import java.io.Serializable;

public class SchemaConnection implements Serializable{
	private static final long serialVersionUID = 1L;
	private String ipAddress;
	private String port;
	private String schemaName;
	private String userName;
	private String password;
	
	public SchemaConnection() {}
	public SchemaConnection(String ipAddress, String port, String schemaName, String userName, String password) {
		this.ipAddress = ipAddress;
		this.port = port;
		this.schemaName = schemaName;
		this.userName = userName;
		this.password = password;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public String getPort() {
		return port;
	}
	public String getSchemaName() {
		return schemaName;
	}
	public String getUserName() {
		return userName;
	}
	public String getPassword() {
		return password;
	}
}
