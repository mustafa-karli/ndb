package com.nauticana.dbgen;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.nauticana.dbinit.InitSQLFiles;
import com.nauticana.dbutil.Connections;
import com.nauticana.dbutil.Utils;

public class ExportModuleDML {

	public static final String	SPACES			= "                                                  ";

	public static String indentLeft(String s, int sp) {
		return s + SPACES.substring(0, sp - s.length());
	}

	public static String indentright(String s, int sp) {
		return SPACES.substring(0, sp - s.length()) + s;
	}

	public static String zeroStr(String s) {
		if (s == null)
			return "";
		return s;
	}

	public static void createModuleDML(String driver, String url) throws ClassNotFoundException, SQLException, IOException {
		Class.forName(driver);
		Connection con1 = DriverManager.getConnection(url);
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;

		ArrayList<String> moduleSql = new ArrayList<String>();
		ArrayList<String> roleSql = new ArrayList<String>();
		ArrayList<String> sequenceSql = new ArrayList<String>();
		ArrayList<String> controllerSql = new ArrayList<String>();
		ArrayList<String> domainSql = new ArrayList<String>();

		ArrayList<String> enSql = new ArrayList<String>();
		ArrayList<String> trSql = new ArrayList<String>();
		ArrayList<String> esSql = new ArrayList<String>();
		ArrayList<String> deSql = new ArrayList<String>();
		ArrayList<String> ruSql = new ArrayList<String>();
		ArrayList<String> arSql = new ArrayList<String>();

		String s;

		rs1 = con1.createStatement().executeQuery("SELECT MODULE, MENU, ICON, DISPLAY_ORDER, ADMIN_ROLE FROM MAIN_MENU");

		while (rs1.next()) {
			String module = zeroStr(rs1.getString(1));
			String menu = zeroStr(rs1.getString(2));
			String role = zeroStr(rs1.getString(5));

			s = "INSERT INTO MAIN_MENU (MENU, CAPTION, ICON, DISPLAY_ORDER) VALUES ('" + menu + "', '" + menu + "', '" + zeroStr(rs1.getString(3)) + "', " + zeroStr(rs1.getString(4)) + ");";
			moduleSql.add(s);
			s = "INSERT INTO AUTHORITY_GROUP (AUTHORITY_GROUP,CAPTION) VALUES ('" + role + "', '" + role + "');";
			roleSql.add(s);

			rs2 = con1.createStatement().executeQuery("SELECT OBJECT_TYPE FROM AUTHORITY_OBJECT WHERE MODULE='" + module + "'");
			while (rs2.next()) {
				String aobject = zeroStr(rs2.getString(1));
				s = "INSERT INTO AUTHORITY_OBJECT (OBJECT_TYPE) VALUES ('" + aobject + "');";
				moduleSql.add(s);

				rs3 = con1.createStatement().executeQuery("SELECT ACTION FROM AUTHORITY_OBJECT_ACTION WHERE OBJECT='" + aobject + "'");
				while (rs3.next()) {
					String action = zeroStr(rs3.getString(1));
					s = "INSERT INTO AUTHORITY_OBJECT_ACTION (OBJECT_TYPE, ACTION) VALUES ('" + aobject + "', '" + action + "');";
					moduleSql.add(s);
					s = "INSERT INTO OBJECT_AUTHORIZATION (AUTHORITY_GROUP,OBJECT_TYPE,ACTION,KEY_VALUE) VALUES ('" + role + "', '" + aobject + "', '" + action + "', '*');";
					roleSql.add(s);
				}
			}

			rs2 = con1.createStatement().executeQuery("SELECT PAGENAME, ICON, LINK, DISPLAY_ORDER FROM SCREEN_PAGE WHERE PAGENAME NOT LIKE 'REST*' AND MODULE='" + module + "'");
			while (rs2.next()) {
				String page = zeroStr(rs2.getString(1));
				String icon = zeroStr(rs2.getString(2));
				String link = zeroStr(rs2.getString(3));
				String dord = zeroStr(rs2.getString(4));
				s = "INSERT INTO SCREEN_PAGE (PAGENAME,CAPTION,ICON,URL,MENU,DISPLAY_ORDER) VALUES ('" + page + "', '" + page + "', '" + icon + "', '" + link + "', '" + menu + "', " + dord + ");";
				moduleSql.add(s);
				s = "INSERT INTO OBJECT_AUTHORIZATION (AUTHORITY_GROUP,OBJECT_TYPE,ACTION,KEY_VALUE) VALUES ('" + role + "', 'PAGE', 'ACCESS', '" + page + "');";
				roleSql.add(s);
			}
			moduleSql.add("");

			rs2 = con1.createStatement().executeQuery(
					"SELECT TABLENAME, ROOTMAPPING, BEANCLASS, IDCLASS, SEARCH_VIEW, LIST_VIEW, EDIT_VIEW, SHOW_VIEW, SELECT_VIEW, ORDERBY, SEQUENCE_NAME, CLIENT_DEPENDENT, ORGANIZATION_CHECK, CACHE_IN_HASH FROM TABLE_CONTROLLER_STATIC WHERE MODULE='"
							+ module + "'");
			while (rs2.next()) {
				String table = zeroStr(rs2.getString(1));
				String rootMapping = zeroStr(rs2.getString(2));
				// String beanClass = zeroStr(rs2.getString(3));
				// String idClass = zeroStr(rs2.getString(4));
				String searchView = zeroStr(rs2.getString(5));
				String listView = zeroStr(rs2.getString(6));
				String editView = zeroStr(rs2.getString(7));
				String showView = zeroStr(rs2.getString(8));
				String selectView = zeroStr(rs2.getString(9));
				String order = zeroStr(rs2.getString(10));
				String sequence = zeroStr(rs2.getString(11));
				String clientDep = zeroStr(rs2.getString(12));
				String orgDep = zeroStr(rs2.getString(13));
				String cacheInHash = zeroStr(rs2.getString(14));
				s = "INSERT INTO TABLE_CONTROLLER_STATIC (TABLENAME, ROOTMAPPING, MODULE, CUSTOMER_SPECIFIC, ORGANIZATION_CHECK, CACHE_IN_HASH, SEARCH_VIEW, LIST_VIEW, EDIT_VIEW, SHOW_VIEW, SELECT_VIEW, ORDERBY, SEQUENCE_NAME) VALUES ('"
					+ table + "', '" + rootMapping + "', '" + module + "', '" + clientDep + "', '" + orgDep + "', '" + cacheInHash + "', '" + searchView + "', '" + listView + "', '" + editView + "', '" + showView + "', '" + selectView
					+ "', '" + order + "', '" + sequence + "');";
				controllerSql.add(s);
				if (!Utils.emptyStr(sequence))
					sequenceSql.add("CREATE SEQUENCE " + sequence + " START WITH 1 INCREMENT BY 1;");
				s = "INSERT INTO OBJECT_AUTHORIZATION (AUTHORITY_GROUP,OBJECT_TYPE,ACTION,KEY_VALUE) VALUES ('" + role + "', 'TABLE', 'SELECT', '" + table + "');";
				roleSql.add(s);
				s = "INSERT INTO OBJECT_AUTHORIZATION (AUTHORITY_GROUP,OBJECT_TYPE,ACTION,KEY_VALUE) VALUES ('" + role + "', 'TABLE', 'INSERT', '" + table + "');";
				roleSql.add(s);
				s = "INSERT INTO OBJECT_AUTHORIZATION (AUTHORITY_GROUP,OBJECT_TYPE,ACTION,KEY_VALUE) VALUES ('" + role + "', 'TABLE', 'UPDATE', '" + table + "');";
				roleSql.add(s);
				s = "INSERT INTO OBJECT_AUTHORIZATION (AUTHORITY_GROUP,OBJECT_TYPE,ACTION,KEY_VALUE) VALUES ('" + role + "', 'TABLE', 'DELETE', '" + table + "');";
				roleSql.add(s);

				rs3 = con1.createStatement().executeQuery("SELECT TABLENAME, FIELDNAME, EDIT_STYLE, EDIT_JSTL_PATH, VIEW_JSTL_PATH, SEARCH_STYLE, LOOKUP_STYLE, TRANSLATED, MINVALUE, MAXVALUE FROM TABLE_FIELD_FACE WHERE TABLENAME='" + table + "'");
				while (rs3.next()) {
					s = "INSERT INTO TABLE_FIELD_FACE (TABLENAME, FIELDNAME, EDIT_STYLE, EDIT_JSTL_PATH, VIEW_JSTL_PATH, SEARCH_STYLE, LOOKUP_STYLE, TRANSLATED, MINVALUE, MAXVALUE) VALUES ('" + zeroStr(rs3.getString(1)) + "', '"
							+ zeroStr(rs3.getString(2)) + "', '" + zeroStr(rs3.getString(3)) + "', '" + zeroStr(rs3.getString(4)) + "', '" + zeroStr(rs3.getString(5)) + "', '" + zeroStr(rs3.getString(6)) + "', '" + zeroStr(rs3.getString(7))
							+ "', '" + zeroStr(rs3.getString(8)) + "', '" + zeroStr(rs3.getString(9)) + "', '" + zeroStr(rs3.getString(10)) + "');";
					controllerSql.add(s);
				}
				controllerSql.add("");

				rs3 = con1.createStatement().executeQuery("SELECT TABLENAME, OBJECT_TYPE, CAPTION, MIMETYPE FROM TABLE_CONTENT_TYPE WHERE TABLENAME='" + table + "'");
				while (rs3.next()) {
					s = "INSERT INTO TABLE_CONTENT_TYPE (TABLENAME, OBJECT_TYPE, CAPTION, MIMETYPE) VALUES ('" + zeroStr(rs3.getString(1)) + "', '" + zeroStr(rs3.getString(2)) + "', '" + zeroStr(rs3.getString(3)) + "', '" + zeroStr(rs3.getString(4)) + "');";
					controllerSql.add(s);
				}

				rs3 = con1.createStatement().executeQuery("SELECT TABLENAME, ACTION, CAPTION, METHOD, ENABLE, AUTHORITY_CHECK, RECORD_SPECIFIC FROM TABLE_ACTION WHERE TABLENAME='" + table + "'");
				while (rs3.next()) {
					s = "INSERT INTO TABLE_ACTION (TABLENAME, ACTION, CAPTION, METHOD, ENABLE, AUTHORITY_CHECK, RECORD_SPECIFIC) VALUES ('" + zeroStr(rs3.getString(1)) + "', '" + zeroStr(rs3.getString(2)) + "', '"
							+ zeroStr(rs3.getString(3)) + "', '" + zeroStr(rs3.getString(4)) + "', '" + zeroStr(rs3.getString(5)) + "', '" + zeroStr(rs3.getString(6)) + "', '" + zeroStr(rs3.getString(7)) + "');";
					controllerSql.add(s);
				}
				controllerSql.add("");
			}
			roleSql.add("");

			rs2 = con1.createStatement().executeQuery("SELECT DOMAIN, KEYSIZE, CAPTION, SORT_BY FROM DOMAIN_NAME WHERE MODULE='" + module + "'");
			while (rs2.next()) {
				String domain = zeroStr(rs2.getString(1));
				s = "INSERT INTO DOMAIN_NAME (DOMAIN, KEYSIZE, CAPTION, SORT_BY) VALUES ('" + domain + "', " + zeroStr(rs2.getString(2)) + ", '" + zeroStr(rs2.getString(3)) + "', '" + zeroStr(rs2.getString(4)) + "');";
				domainSql.add(s);

				rs3 = con1.createStatement().executeQuery("SELECT DOMAIN, REFVALUE, CAPTION FROM DOMAIN_VALUE WHERE DOMAIN='" + domain + "'");
				while (rs3.next()) {
					s = "INSERT INTO DOMAIN_VALUE (DOMAIN, REFVALUE, CAPTION) VALUES ('" + zeroStr(rs3.getString(1)) + "', '" + zeroStr(rs3.getString(2)) + "', '" + zeroStr(rs3.getString(3)) + "');";
					domainSql.add(s);
				}
				domainSql.add("");
			}
		}

		rs3 = con1.createStatement().executeQuery("SELECT CONSTRAINT_NAME, MASTER_TABLE, DETAIL_TABLE, DETAIL_ATTRIBUTE, MD_VIEW, PAGING, FILTER, ORDERBY FROM MASTER_DETAIL_RELATION");
		while (rs3.next()) {
			s = "INSERT INTO MASTER_DETAIL_RELATION (CONSTRAINT_NAME, MASTER_TABLE, DETAIL_TABLE, DETAIL_ATTRIBUTE, MD_VIEW, PAGING, FILTER, ORDERBY) VALUES ('" +
					zeroStr(rs3.getString(1)) + "', '" + zeroStr(rs3.getString(2)) + "', '"	+ zeroStr(rs3.getString(3)) + "', '" + 
					zeroStr(rs3.getString(4)) + "', '" + zeroStr(rs3.getString(5)) + "', '" + zeroStr(rs3.getString(6)) + "', '" +
					zeroStr(rs3.getString(7)) + "', '" + zeroStr(rs3.getString(8)) + "');";
			controllerSql.add(s);
		}

		rs3 = con1.createStatement().executeQuery("SELECT CAPTION, TRUPPER, TRLOWER, ENUPPER, ENLOWER, ESUPPER, ESLOWER, DEUPPER, DELOWER, RUUPPER, RULOWER, ARUPPER, ARLOWER FROM TRANSLATION ORDER BY CAPTION");

		trSql.add("INSERT INTO LANGUAGE (LANGCODE, CAPTION, LOCALE_STR, DIRECTION, FLAG) VALUES ('TR', 'TURKISH', 'tr_TR', 'LEFT',  'flag-icon-tr');");
		enSql.add("INSERT INTO LANGUAGE (LANGCODE, CAPTION, LOCALE_STR, DIRECTION, FLAG) VALUES ('EN', 'ENGLISH', 'en_US', 'LEFT',  'flag-icon-gb');");
		esSql.add("INSERT INTO LANGUAGE (LANGCODE, CAPTION, LOCALE_STR, DIRECTION, FLAG) VALUES ('ES', 'SPANISH', 'es_ES', 'LEFT',  'flag-icon-es');");
		// frSql.add("INSERT INTO LANGUAGE (LANGCODE, CAPTION, LOCALE_STR, DIRECTION,
		// FLAG) VALUES ('FR', 'FRENCH', 'fr_FR', 'LEFT', 'flag-icon-fr');");
		deSql.add("INSERT INTO LANGUAGE (LANGCODE, CAPTION, LOCALE_STR, DIRECTION, FLAG) VALUES ('DE', 'DEUTSCH', 'de_DE', 'LEFT',  'flag-icon-de');");
		ruSql.add("INSERT INTO LANGUAGE (LANGCODE, CAPTION, LOCALE_STR, DIRECTION, FLAG) VALUES ('RU', 'RUSSIAN', 'ru_RU', 'LEFT',  'flag-icon-ru');");
		arSql.add("INSERT INTO LANGUAGE (LANGCODE, CAPTION, LOCALE_STR, DIRECTION, FLAG) VALUES ('AR', 'ARABIC',  'ar_SA', 'RIGHT', 'flag-icon-sa');");

		while (rs3.next()) {
			String caption = rs3.getString(1);
			String lower = rs3.getString("TRLOWER");
			String upper = rs3.getString("TRUPPER");
			if (!(Utils.emptyStr(upper) && Utils.emptyStr(lower)))
				trSql.add("INSERT INTO CAPTION_TRANSLATION (CAPTION,LANGCODE,LABELUPPER,LABELLOWER) VALUES ('" + caption + "', 'TR', '" + upper + "', '" + lower + "');");
			lower = rs3.getString("ENLOWER");
			upper = rs3.getString("ENUPPER");
			if (!(Utils.emptyStr(upper) && Utils.emptyStr(lower)))
				enSql.add("INSERT INTO CAPTION_TRANSLATION (CAPTION,LANGCODE,LABELUPPER,LABELLOWER) VALUES ('" + caption + "', 'EN', '" + upper + "', '" + lower + "');");
			lower = rs3.getString("ESLOWER");
			upper = rs3.getString("ESUPPER");
			if (!(Utils.emptyStr(upper) && Utils.emptyStr(lower)))
				esSql.add("INSERT INTO CAPTION_TRANSLATION (CAPTION,LANGCODE,LABELUPPER,LABELLOWER) VALUES ('" + caption + "', 'ES', '" + upper + "', '" + lower + "');");
			// lower = rs3.getString("FRLOWER");
			// upper = rs3.getString("FRUPPER");
			// if (!(Utils.emptyStr(upper) && Utils.emptyStr(lower)))
			// frSql.add("INSERT INTO CAPTION_TRANSLATION
			// (CAPTION,LANGCODE,LABELUPPER,LABELLOWER) VALUES ('"+ caption + "', 'FR', '" +
			// upper + "', '" + lower + "');");
			lower = rs3.getString("DELOWER");
			upper = rs3.getString("DEUPPER");
			if (!(Utils.emptyStr(upper) && Utils.emptyStr(lower)))
				deSql.add("INSERT INTO CAPTION_TRANSLATION (CAPTION,LANGCODE,LABELUPPER,LABELLOWER) VALUES ('" + caption + "', 'DE', '" + upper + "', '" + lower + "');");
			lower = rs3.getString("RULOWER");
			upper = rs3.getString("RUUPPER");
			if (!(Utils.emptyStr(upper) && Utils.emptyStr(lower)))
				ruSql.add("INSERT INTO CAPTION_TRANSLATION (CAPTION,LANGCODE,LABELUPPER,LABELLOWER) VALUES ('" + caption + "', 'RU', '" + upper + "', '" + lower + "');");
			lower = rs3.getString("ARLOWER");
			upper = rs3.getString("ARUPPER");
			if (!(Utils.emptyStr(upper) && Utils.emptyStr(lower)))
				arSql.add("INSERT INTO CAPTION_TRANSLATION (CAPTION,LANGCODE,LABELUPPER,LABELLOWER) VALUES ('" + caption + "', 'AR', '" + upper + "', '" + lower + "');");
		}

		Utils.saveToFile(moduleSql, InitSQLFiles.dmlModule, false);
		Utils.saveToFile(roleSql, InitSQLFiles.dmlRole, false);
		Utils.saveToFile(sequenceSql, InitSQLFiles.ddlSequence, false);
		Utils.saveToFile(domainSql, InitSQLFiles.dmlDomain, false);
		Utils.saveToFile(controllerSql, InitSQLFiles.dmlController, false);

		Utils.saveToFile(trSql, InitSQLFiles.dmlTransTr, false);
		Utils.saveToFile(enSql, InitSQLFiles.dmlTransEn, false);
		Utils.saveToFile(esSql, InitSQLFiles.dmlTransEs, false);
		// Utils.saveToFile(frSql, InitSQLFiles.dmlTransFr, false);
		Utils.saveToFile(deSql, InitSQLFiles.dmlTransDe, false);
		Utils.saveToFile(ruSql, InitSQLFiles.dmlTransRu, true);
		Utils.saveToFile(arSql, InitSQLFiles.dmlTransAr, true);
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		createModuleDML(Connections.accessDriver, "jdbc:ucanaccess://" + InitSQLFiles.accessFile);
	}

}
