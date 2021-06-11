package com.nauticana.dbutil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class Utils {
	
	public static boolean emptyStr(String s) {
		if (s == null)
			return true;
		if (s.isEmpty())
			return true;
		if (s.length() == 0)
			return true;
		return false;
	}
	
	public static boolean emptySql(String s) {
		if (s == null)
			return true;
		if (s.isEmpty())
			return true;
		if (s.length() == 0)
			return true;
		if (s.startsWith("--"))
			return true;
		if (s.startsWith("//"))
			return true;
		return false;
	}
	
	public static String camelCase(String s) {
		String[] x = s.toLowerCase(Locale.ENGLISH).split("_");
		String   l = x[0];
		
		for (int i = 1; i < x.length; i++) {
			byte b = (byte)x[i].charAt(0);
			char c = (char) (b-32);
			l = l + c + x[i].substring(1);
		}
		return l;
	}

	public static String titleCase(String s) {
		String[] x = s.toLowerCase(Locale.ENGLISH).split("_");
		String l = "";
		
		for (int i = 0; i < x.length; i++) {
			byte b = (byte)x[i].charAt(0);
			char c = (char) (b-32);
			l = l + c + x[i].substring(1);
		}
		return l;
	}
	
	public static void saveToFile(List<String> list, String filename, boolean utf8) throws IOException {
		String sep = System.getProperty("line.separator");
		PrintWriter writer;
		if (utf8)
	        writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF8"));
		else
			writer = new PrintWriter(new FileWriter(new File(filename)));
		for (String line : list) {
			writer.write(line);
			writer.write(sep);
		}
		writer.close();
	}
	
	public static void loadFromFile(String filename, List<String> list, boolean utf8) throws IOException {
		BufferedReader reader = null;
		if (utf8)
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF8"));
		else
			reader = new BufferedReader(new FileReader(filename));
		String text = "";
		while (text != null) {
			text = text.trim();
			if (!emptyStr(text))
				list.add(text);
			try {
				text = reader.readLine();
			} catch (Exception e) {
				text = null;
			}
		}
		if (reader != null) {
			reader.close();
		}
	}

	public static boolean runSQL(String sqltext, Connection conn, PrintWriter writer) {
		Statement stmt1 = null;
		try {
			stmt1 = conn.createStatement();
			stmt1.execute(sqltext);
			stmt1.close();
			stmt1 = null;
			return true;
		} catch (Exception e) {
			System.out.println(sqltext);
			e.printStackTrace();
			if (writer != null) {
				writer.write(sqltext);
				e.printStackTrace(writer);
			}
		} finally {
			if (stmt1 != null) {
				try {
					stmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
					if (writer != null) {
						writer.write(sqltext);
						e.printStackTrace(writer);
					}
				}
				stmt1 = null;
			}
		}
		return false;
	}

	public static boolean runSQLFile(String filename, Connection conn, boolean utf8, String logfile, boolean append) throws IOException {
		String logname = logfile;
		if (emptyStr(logname))
			logname = filename + ".log";
		BufferedReader reader = null;
		PrintWriter writer = null;

		String sep = System.getProperty("line.separator");
		String text = "";
		String sqltext = "";
		boolean noSqlError = true;
		
		if (utf8) {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF8"));
	        writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(logname), "UTF8"));
		}
		else {
			reader = new BufferedReader(new FileReader(filename));
			writer = new PrintWriter(new FileWriter(logname, append));
		}
		while (noSqlError && text != null) {
			text = text.trim();
			if (!emptySql(text)) {
				if (text.charAt(text.length() - 1) == ';') {
					sqltext = sqltext + " " + text.substring(0, text.length() - 1);
					text = "/";
				}
				if (text.equals("/")) {
					if (!runSQL(sqltext, conn, writer)) {
						noSqlError = false;
						sqltext = "SQL ERROR : " + sep + sqltext;
						writer.write(sqltext + sep);
						writer.close();
					} else {
						writer.write(sqltext + ";" + sep);
						sqltext = "";
					}
				} else
					sqltext = sqltext + " " + text;
			}
			try {
				text = reader.readLine();
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
		return noSqlError;
	}

	public static Properties serverProperties(String propFile) throws IOException {
		Properties p = new Properties();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(propFile)));
		p.load(reader);
		return p;
	}


}
