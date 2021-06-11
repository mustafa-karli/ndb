package com.nauticana.dbserver;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.hsqldb.server.ServerAcl.AclFormatException;

import com.nauticana.dbutil.Utils;

public class DbServerMain {
	
	public static final String cfg= "cfg/dbs.conf";
//	public static final String cfg=System.getenv("NTCN_HOME") + "/cfg/dbs.conf";

	public static void main(String[] args) {
		try {
			startFromConf(cfg);
		} catch (ClassNotFoundException | IOException | AclFormatException | SQLException e) {
			e.printStackTrace();
		}
	}

	public static String url(String dbname) {
		return HSQLServerFactory.url(dbname);
	}

	public static void startFromConf(String dbconf) throws IOException, AclFormatException, ClassNotFoundException, SQLException {
		Properties p = Utils.serverProperties(dbconf);
		
		int c = 1;
		
		while (c > 0) {
			String root = p.getProperty("root"+c);
			String dbname  = p.getProperty("server"+c);
			String portStr = p.getProperty("port"+c);
			String buildb  = p.getProperty("builddb"+c);
			if (Utils.emptyStr(dbname) || Utils.emptyStr(root) || Utils.emptyStr(portStr)) {
				c = 0;
			} else {
				boolean build = "1".equals(buildb);
				short   port  = Short.parseShort(portStr);
				HSQLServerFactory.startdb(dbname, port, root);
				
				if (build) {
					Connection conn = HSQLServerFactory.getConnection(dbname, "SA", "");
					int g = 1;
					while (g > 0) {
						String sqldir  = p.getProperty("sqlGroup"+g);
						String logfile = p.getProperty("logGroup"+g);
						boolean utf8 = "UTF8".equals(p.getProperty("codepage"+g));
						boolean append = false;
						if (Utils.emptyStr(sqldir) || Utils.emptyStr(logfile)) {
							g = 0;
						} else {
							int f = 1;
							while (f > 0) {
								String sqlfile = p.getProperty("grp"+g+"sql"+f);
								if (Utils.emptyStr(sqlfile)) {
									f = 0;
								} else {
									sqlfile = sqldir+"/"+sqlfile;
									System.out.println("Running " + sqlfile);
									if (!Utils.runSQLFile(sqlfile, conn, utf8, logfile, append)) return;
									append = true;
									f++;
								}
							}
							g++;
						}
					}
				}
				c++;
			}
		}
	}
}
