package com.nauticana.dbgen;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nauticana.dbinit.InitSQLFiles;
import com.nauticana.dbutil.Connections;
import com.nauticana.dbutil.Utils;

public class CheckDBConsistency {

	
	public static boolean generateTable(Connection conn, String tablename) {
		String sql = "INSERT INTO TABLE_CONTROLLER_STATIC (TABLENAME, ROOTMAPPING, MODULE, BEANCLASS, IDCLASS) VALUES ('" + tablename + "', '" + Utils.camelCase(tablename) + "', 'basis', '" + Utils.titleCase(tablename) + "', '" + Utils.titleCase(tablename) + "Id');";
		try {
		//	conn.createStatement().execute(sql);
			System.out.println(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void generateFields(Connection source, Connection destination, String tablename) throws SQLException {
		String sql = "SELECT * FROM " + tablename + " WHERE 1=2";
		ResultSet rs = source.createStatement().executeQuery(sql);
		
		for (int i=1; i<=rs.getMetaData().getColumnCount(); i++) {
			String fieldName = rs.getMetaData().getColumnName(i);
			String fieldPath = Utils.camelCase(rs.getMetaData().getColumnName(i));
			String fieldType = ExportFieldDefinitions.typeNamesNams[ExportFieldDefinitions.getType(rs.getMetaData().getColumnType(i))];
	//		sql = "INSERT INTO TABLE_FIELD_FACE (TABLENAME, FIELDNAME, EDIT_STYLE, EDIT_JSTL_PATH, VIEW_JSTL_PATH, SEARCH_STYLE, LOOKUP_STYLE, TRANSLATED, MINVALUE, MAXVALUE) VALUES ('" + tablename + "', '" + fieldName + "', '" + fieldType + "', '" + fieldPath + "', '" + fieldPath + "', 'F', 'N', 'N', '', '');";
			sql = tablename + ";" + fieldName + ";" + fieldType + ";" + fieldPath + ";" + fieldPath + ";F;N;N;;;"+i;
		//	destination.createStatement().execute(sql);
			System.out.println(sql);
		}
	}
	
	public static void generateAllTables(Connection source, Connection destination, ArrayList<String> tablenames) throws ClassNotFoundException, SQLException {
		for (String tablename : tablenames) {
			if (generateTable(destination, tablename)) {
				generateFields(source, destination, tablename);
			}
		}
	}
	
	public static void checkFields(Connection source, Connection destination) throws SQLException {
		String sql3 = "SELECT TABLENAME FROM TABLE_CONTROLLER_STATIC ORDER BY TABLENAME";
		ResultSet rs3 = destination.createStatement().executeQuery(sql3);
		
		while (rs3.next()) {
			String tablename = rs3.getString(1);
			String sql1 = "SELECT * FROM " + tablename + " WHERE 1=2";
			String sql2 = "SELECT * FROM TABLE_FIELD_FACE WHERE TABLENAME='" + tablename + "' ORDER BY 11";
			ResultSet rs1 = source.createStatement().executeQuery(sql1);
			ResultSet rs2 = destination.createStatement().executeQuery(sql2);
			int k = 0;
			int i = 0;
			while (rs2.next()) {
				i = rs2.getInt("SEQ");
				k++;
				String fieldName = rs1.getMetaData().getColumnName(i);
				String fieldFace = rs2.getString("FIELDNAME");
				if (i != k)
					System.out.println(tablename + "." + k + " missing");
				if (!fieldName.equals(fieldFace))
					System.out.println(tablename + "." + k + " " + fieldName + " <> " + fieldFace);
			}

			for (int j = i + 1; j <= rs1.getMetaData().getColumnCount(); j++) {
				System.out.println(tablename + "." + j + " " + rs1.getMetaData().getColumnName(j) + " missing");
			}
		}
	}
	
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
//		String[] tablenames = new String[] {"ACCOUNT_BALANCE","ACCOUNT_MASTER","ACCOUNT_SCHEMA","ACCOUNT_TRANSACTION","ACCOUNT_TRANSACTION_ITEM","ACCOUNT_TRANSACTION_TEMPLATE","ACCOUNT_TX_TEMPLATE_ITEM","BANK","BANK_ACCOUNT","BANK_BRANCH"};
//		String[] tablenames = new String[] {"MATERIAL_TYPE_ATTRIBUTE_GROUP"};
//		String[] tablenames = new String[] {"REQUEST_FOR_PROPOSAL","REQUEST_FOR_PROPOSAL_ITEM","PROPOSAL_TO_RFP","PROPOSAL_TO_RFP_ITEM"};
		ArrayList<String> tablenames = new ArrayList<String>();
		Utils.loadFromFile("C:\\Users\\Mustafa\\x.txt", tablenames, false);
		Connection sourceConnection = Connections.getHsqldbConnection("NCA");
		Connection destConnection   = Connections.getAccessConnection(InitSQLFiles.accessFile);
		generateAllTables(sourceConnection, destConnection, tablenames);
		checkFields(sourceConnection, destConnection);
	}

}
