package com.nauticana.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connections {
	public static final String accessDriver	= "net.ucanaccess.jdbc.UcanaccessDriver";
	public static final String pgsqlDriver  = "org.postgresql.Driver";
	public static final String hsqldbDriver = "org.hsqldb.jdbc.JDBCDriver";

	public static final String pgsqlUrl     = "jdbc:postgresql://ncs.cl96sshpc7ab.us-east-1.rds.amazonaws.com:5432/NCADEV";
	public static final String hsqldbUrl    = "jdbc:hsqldb:hsql://localhost:9006/";

	public static Connection getAccessConnection(String file) throws ClassNotFoundException, SQLException {
		Class.forName(accessDriver);
		String url = "jdbc:ucanaccess://" + file;
		return DriverManager.getConnection(url);
	}
	
	public static Connection getHsqldbConnection(String dbname) throws ClassNotFoundException, SQLException {
		String url=hsqldbUrl+dbname;
		Class.forName(hsqldbDriver);
		Connection connection = DriverManager.getConnection(url, "SA", "");
		System.out.println("Connected to " + url + " with schema " + connection.getSchema());
		return connection;
	}

	public static Connection getPgsqlConnection(String url, String user, String pass) throws ClassNotFoundException, SQLException {
		Class.forName(pgsqlDriver);
		Connection connection = DriverManager.getConnection(url, user, pass);
		System.out.println("Connected to " + url + " with user " + user + " to schema " + connection.getSchema());
		return connection;
	}
}
