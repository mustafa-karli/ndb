package com.nauticana.dbinit;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class InitializeDatabaseMain {

	public static final String	pgsqlDriver	= "org.postgresql.Driver";
	public static final String	pgsqlUrl	= "jdbc:postgresql://nca.cl96sshpc7ab.us-east-1.rds.amazonaws.com/OSKAR";

	public static final String	accessNca	= "D:\\WORKDATA\\NCA.accdb";

	public static void initNcaData(String driver, String url, String user, String pass) throws ClassNotFoundException, SQLException, IOException {
		Class.forName(driver);
		Connection connection = null;
		connection = DriverManager.getConnection(url, user, pass);
		System.out.println("Connected to " + url + " with user " + user + " to schema " + connection.getSchema());
		InitSQLFiles.buildDB(connection);
		connection.close();
	}

//	public static void initNcaAccess(String file) throws ClassNotFoundException, SQLException, IOException {
//		Connection connection = Connections.getAccessConnection(file);
//		InitSQLFiles.buildDB(connection);
//		InitSQLFiles.loadModules(connection);
//		InitSQLFiles.loadMotif(connection);
//		InitSQLFiles.translate(connection);
//		connection.close();
//	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		 initNcaData(pgsqlDriver, pgsqlUrl, "OSKAR", "nd30mrt18de");
//		initNcaAccess(accessNca);
	}

}
