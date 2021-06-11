package com.nauticana.dbgen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import com.nauticana.dbinit.InitSQLFiles;
import com.nauticana.dbserver.HSQLServerFactory;
import com.nauticana.dbutil.Utils;

public class ExportFieldDefinitions {

	public static final String pgsqlDriver = "org.postgresql.Driver";
	public static final String pgsqlUrl    = "jdbc:postgresql://ncs.cl96sshpc7ab.us-east-1.rds.amazonaws.com:5432/NCADEV";
	public static final String hsqldbUrl   = "jdbc:hsqldb:hsql://localhost:9006/NCA";

	public static final int T_BYTE  = 1;
	public static final int T_SHORT = 2;
	public static final int T_INT   = 3;
	public static final int T_LONG  = 4;
	public static final int T_FLT   = 5;
	public static final int T_STR   = 6;
	public static final int T_DATE  = 7;
	
	public static final String [] typeNamesJava = {"unknown", "BYTE", "SHORT", "INTEGER", "LONG", "FLOAT", "STRING", "DATE"}; 
	public static final String [] typeNamesNams = {"unknown", "I1",   "I2",    "I4",      "I8",   "F",     "T",      "D"}; 

	public static int getType(int sqlType) {
		switch (sqlType) {
		case java.sql.Types.CHAR        : return T_STR;
		case java.sql.Types.VARCHAR     : return T_STR;
		case java.sql.Types.VARBINARY   : return T_STR;
		case java.sql.Types.NCHAR       : return T_STR;
		case java.sql.Types.NVARCHAR    : return T_STR;
		case java.sql.Types.LONGVARCHAR : return T_STR;
		case java.sql.Types.LONGNVARCHAR: return T_STR;
		case java.sql.Types.DATE        : return T_DATE;
		case java.sql.Types.TIME        : return T_DATE;
		case java.sql.Types.TIMESTAMP   : return T_DATE;
		case java.sql.Types.NUMERIC     : return T_FLT;
		case java.sql.Types.DECIMAL     : return T_FLT;
		case java.sql.Types.FLOAT       : return T_FLT;
		case java.sql.Types.REAL        : return T_FLT;
		case java.sql.Types.DOUBLE      : return T_FLT;
		case java.sql.Types.TINYINT     : return T_BYTE;
		case java.sql.Types.SMALLINT    : return T_SHORT;
		case java.sql.Types.INTEGER     : return T_INT;
		case java.sql.Types.BIGINT      : return T_LONG;
		default: return 0;
		}
	}
	
	
	public static void exportController(String driver, String url, String user, String pass) throws ClassNotFoundException, SQLException, IOException {
		Class.forName(driver);
		Connection connection = DriverManager.getConnection(url, user, pass);
		System.out.println("Connected to " + url + " with user " + user + " to schema " + connection.getSchema());
		getTableDefinition(InitSQLFiles.tableList, connection);
		connection.close();
	}
	
	public static String fType(String mType) {
		return "";
	}
	

	public static String requiredText(int r) {
		if (r == 0) return "Labels.REQUIRED";
		return "\"\"";
	}
	

	public static void getTableDefinition(String filename, Connection conn) throws IOException, SQLException {
		File logfile = new File(filename + ".log");
		PrintWriter writer = null;
		Statement stmt1 = null;
		stmt1 = conn.createStatement();
		String sep = System.getProperty("line.separator");
		String text = "";
		
		Scanner reader = new Scanner(new FileInputStream(filename));
		
		writer = new PrintWriter(new FileWriter(logfile));
		while (text != null) {
			text = text.trim();
			if (!Utils.emptyStr(text)) {
				try {
					ResultSet rs = stmt1.executeQuery("SELECT * FROM " + text + " WHERE 1=2");
					
					String f = rs.getMetaData().getColumnName(1);
					String a = Utils.camelCase(rs.getMetaData().getColumnName(1));
					String t = typeNamesNams[getType(rs.getMetaData().getColumnType(1))];
					String r = requiredText(rs.getMetaData().isNullable(1));
					
					int lf = f.length();
					int la = a.length();
					int lt = t.length();
					int lr = r.length();
					
					String ff = "\tpublic  static final String[] FIELD_NAMES = new String[] { \"" + f + "\"";
					String fa = "\tpublic  static final String[] ATTRIBUTES  = new String[] { \"" + a + "\"";
					String ft = "\tpublic  static final String[] INPUT_TYPES = new String[] { \"" + t + "\"";
					String fr = "\tpublic  static final String[] REQUIRED    = new String[] { "   + r;
					
					for (int i=2; i<=rs.getMetaData().getColumnCount(); i++) {
						
						f = rs.getMetaData().getColumnName(i);
						a = Utils.camelCase(rs.getMetaData().getColumnName(i));
						t = typeNamesNams[getType(rs.getMetaData().getColumnType(i))];
						r = requiredText(rs.getMetaData().isNullable(i));
						
						ff = ff + "," + "                              \"".substring(lf) + f + "\"";
						fa = fa + "," + "                              \"".substring(la) + a + "\"";
						ft = ft + "," + "                              \"".substring(lt) + t + "\"";
						fr = fr + "," + "                                ".substring(lr) + r + "";

						lf = f.length();
						la = a.length();
						lt = t.length();
						lr = r.length();
					}
					
					writer.write(sep);
					writer.write(sep);
					writer.write("import com.nauticana.basis.utils.Labels;" + sep + sep); 
					writer.write("@Entity" + sep);
					writer.write("@Table(name = " + Utils.titleCase(text) + ".TABLE_NAME)" + sep);
					writer.write("public class " + Utils.titleCase(text) + " implements java.io.Serializable {" + sep + sep);
					writer.write("\tprivate static final long     serialVersionUID = 1L;" + sep);
					writer.write("\tpublic  static final String   ROOTMAPPING = \"" + Utils.camelCase(text) + "\";" + sep);
					writer.write("\tpublic  static final String   TABLE_NAME  = \"" + text + "\";" + sep);
					writer.write(ff + "};" + sep);
					writer.write(fa + "};" + sep);
					writer.write(ft + "};" + sep);
					writer.write(fr + "};" + sep);
				} catch (Exception e) {
				}
			}
			try {
				text = reader.nextLine();
			} catch (Exception e) {
				text = null;
			}
		}
		if (reader != null) {
			reader.close();
		}
		if (writer != null) {
			writer.close();
		}
		stmt1.close();

	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		exportController(HSQLServerFactory.DRIVER, hsqldbUrl, "SA", "");
	}

}
