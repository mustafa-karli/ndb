package com.nauticana.dbinit;

import java.io.IOException;
import java.sql.Connection;

import com.nauticana.dbutil.Utils;

public class InitSQLFiles {
	
	public static final String sqlBase         = "D:\\CODE\\ELIPS\\nca\\src\\main\\SQL\\";
//	public static final String sqlBase         = "\\data\\CODE\\ELIPS\\nca\\src\\main\\SQL\\";
	
	public static final String accessFile	   = sqlBase + "DEF\\MODULES.accdb";
	public static final String tableList       = sqlBase + "DDL\\tables.txt";
	
	public static final String ddlLogAll       = sqlBase + "LOG\\ALLDDL.LOG";
	public static final String dmlLogAll       = sqlBase + "LOG\\ALLDML.LOG";
	public static final String transNUCAll     = sqlBase + "LOG\\ALLTRANSNUC.LOG";
	public static final String transUCAll      = sqlBase + "LOG\\ALLTRANSUC.LOG";

	public static final String ddlContentSrv   = sqlBase + "DDL\\CONTENT_SERVER.SQL";
	public static final String ddlBasis        = sqlBase + "DDL\\BASIS.SQL";
	public static final String ddlBusiness     = sqlBase + "DDL\\BUSINESS.SQL";
	public static final String ddlAccounting   = sqlBase + "DDL\\ACCOUNTING.SQL";
	public static final String ddlPersonnel    = sqlBase + "DDL\\HUMAN_RESOURCES.SQL";
	public static final String ddlMaterial     = sqlBase + "DDL\\MATERIAL.SQL";
	public static final String ddlSales        = sqlBase + "DDL\\SALES.SQL";
	public static final String ddlProposal     = sqlBase + "DDL\\PROPOSAL.SQL";
	public static final String ddlPurchase     = sqlBase + "DDL\\PURCHASE.SQL";
	public static final String ddlProduction   = sqlBase + "DDL\\PRODUCTION.SQL";
	public static final String ddlShipment     = sqlBase + "DDL\\SHIPMENT.SQL";
	public static final String ddlMaintenance  = sqlBase + "DDL\\MAINTENANCE.SQL";
	public static final String ddlProject      = sqlBase + "DDL\\PROJECT.SQL";
	public static final String ddlHelpdesk     = sqlBase + "DDL\\HELPDESK.SQL";

	public static final String ddlSequence     = sqlBase + "DDL\\SEQUENCE.SQL";
	public static final String dmlJson         = sqlBase + "DDL\\JSON_SCENARIO.SQL";
	public static final String dmlDomain       = sqlBase + "DML\\DOMAIN.SQL";
	public static final String dmlModule       = sqlBase + "DML\\MODULE.SQL";
	public static final String dmlRole         = sqlBase + "DML\\ROLE.SQL";
	public static final String dmlController   = sqlBase + "DML\\CONTROLLER.SQL";

	public static final String dmlCategory     = sqlBase + "DML\\CATEGORY.SQL";

	public static final String dmlInitialBas   = sqlBase + "INIT\\INITIAL_BAS.SQL";
	public static final String dmlUserAccount  = sqlBase + "INIT\\USER_ACCOUNT.SQL";
	public static final String dmlCommonRole   = sqlBase + "INIT\\COMMON_ROLE.SQL";
	
	public static final String dmlInitialCar   = sqlBase + "INIT\\INITIAL_CAR.SQL";
	public static final String dmlInitialPat   = sqlBase + "INIT\\INITIAL_PAT.SQL";
	public static final String dmlInitialMh    = sqlBase + "INIT\\INITIAL_MH.SQL";
//	public static final String dmlPatRole      = sqlBase + "INIT\\PAT_ROLE.SQL";
	public static final String dmlSampleMh     = sqlBase + "INIT\\SAMPLE_MH.SQL";

	public static final String dmlMotifRole    = sqlBase + "INIT\\MOTIF_ROLE.SQL";
	public static final String dmlMotifBus     = sqlBase + "INIT\\MOTIF_BUS.SQL";
	public static final String dmlMotifSales   = sqlBase + "INIT\\MOTIF_SALES.SQL";
	public static final String dmlMotifAlibaba = sqlBase + "INIT\\ALIBABA_MAT_GRP.SQL";
	public static final String dmlConspro      = sqlBase + "INIT\\CONSPRO.SQL";
	
	public static final String dmlTransAr      = sqlBase + "LANG\\TRANS_AR.SQL";
	public static final String dmlTransDe      = sqlBase + "LANG\\TRANS_DE.SQL";
	public static final String dmlTransEn      = sqlBase + "LANG\\TRANS_EN.SQL";
	public static final String dmlTransEs      = sqlBase + "LANG\\TRANS_ES.SQL";
//	public static final String dmlTransFr      = sqlBase + "LANG\\TRANS_FR.SQL";
	public static final String dmlTransRu      = sqlBase + "LANG\\TRANS_RU.SQL";
	public static final String dmlTransTr      = sqlBase + "LANG\\TRANS_TR.SQL";
	public static final String dmlTransUc      = sqlBase + "LANG\\TRANS_UC.SQL";
	
	public static final String ddlAdempiere    = "D:\\GIT\\nca\\SQLCP\\cp_adempiere.txt";
	public static final String ddlAdempiereLog = "D:\\GIT\\nca\\SQLCP\\cp_adempiere.log";

	public static void buildDB(Connection conn) throws IOException {
		Utils.runSQLFile(ddlSequence,    conn, false, ddlLogAll, false);
		Utils.runSQLFile(ddlBasis,       conn, false, ddlLogAll, true);
		Utils.runSQLFile(ddlContentSrv,  conn, false, ddlLogAll, true);
		Utils.runSQLFile(ddlBusiness,    conn, false, ddlLogAll, true);
		Utils.runSQLFile(ddlAccounting,  conn, false, ddlLogAll, true);
		Utils.runSQLFile(ddlPersonnel,   conn, false, ddlLogAll, true);
		Utils.runSQLFile(ddlMaterial,    conn, false, ddlLogAll, true);
		Utils.runSQLFile(ddlSales,       conn, false, ddlLogAll, true);
		Utils.runSQLFile(ddlProposal,    conn, false, ddlLogAll, true);
		Utils.runSQLFile(ddlPurchase,    conn, false, ddlLogAll, true);
		Utils.runSQLFile(ddlShipment,    conn, false, ddlLogAll, true);
		Utils.runSQLFile(ddlProduction,  conn, false, ddlLogAll, true);
		Utils.runSQLFile(ddlMaintenance, conn, false, ddlLogAll, true);
		Utils.runSQLFile(ddlProject,     conn, false, ddlLogAll, true);
		Utils.runSQLFile(ddlHelpdesk,    conn, false, ddlLogAll, true);
		Utils.runSQLFile(dmlJson,        conn, false, ddlLogAll, true);
	}

	public static void loadModules(Connection conn) throws IOException {
			Utils.runSQLFile(dmlDomain,         conn, false, dmlLogAll, false);
			Utils.runSQLFile(dmlModule,         conn, false, dmlLogAll, true);
			Utils.runSQLFile(dmlRole,           conn, false, dmlLogAll, true);
			Utils.runSQLFile(dmlController,     conn, false, dmlLogAll, true);
			Utils.runSQLFile(dmlUserAccount,    conn, false, dmlLogAll, true);
			Utils.runSQLFile(dmlInitialBas,     conn, false, dmlLogAll, true);
			Utils.runSQLFile(dmlCommonRole,     conn, false, dmlLogAll, true);
//			Utils.runSQLFile(dmlInitialCar,     conn, false, dmlLogAll, true);
//			Utils.runSQLFile(dmlPatRole,        conn, false, dmlLogAll, true);
//			Utils.runSQLFile(dmlInitialPat,     conn, false, dmlLogAll, true);
//			Utils.runSQLFile(dmlInitialMh,      conn, false, dmlLogAll, true);
//			Utils.runSQLFile(dmlSampleMh,       conn, false, dmlLogAll, true);
	}

	public static void loadMotif(Connection conn) throws IOException {
		Utils.runSQLFile(dmlMotifRole,         conn, false, dmlLogAll, true);
		Utils.runSQLFile(dmlMotifBus,          conn, false, dmlLogAll, true);
		Utils.runSQLFile(dmlMotifAlibaba,      conn, false, dmlLogAll, true);
		Utils.runSQLFile(dmlMotifSales,        conn, false, dmlLogAll, true);
//		Utils.runSQLFile(dmlConspro,           conn, false, dmlLogAll, true);
	}
	
	public static void loadConspro(Connection conn) throws IOException {
//		Utils.runSQLFile(dmlMotifRole,         conn, false, dmlLogAll, true);
//		Utils.runSQLFile(dmlMotifBus,          conn, false, dmlLogAll, true);
//		Utils.runSQLFile(dmlMotifAlibaba,      conn, false, dmlLogAll, true);
//		Utils.runSQLFile(dmlMotifSales,        conn, false, dmlLogAll, true);
		Utils.runSQLFile(dmlConspro,           conn, false, dmlLogAll, true);
	}
	
	public static void translate(Connection conn) throws IOException {
		Utils.runSQLFile(dmlTransTr,     conn, false, transNUCAll, false);
		Utils.runSQLFile(dmlTransEn,     conn, false, transNUCAll, true);
		Utils.runSQLFile(dmlTransEs,     conn, false, transNUCAll, true);
//		Utils.runSQLFile(dmlTransFr,     conn, false, transNUCAll, true);
		Utils.runSQLFile(dmlTransDe,     conn, false, transNUCAll, true);
		Utils.runSQLFile(dmlTransRu,     conn, true, transUCAll, false);
		Utils.runSQLFile(dmlTransAr,     conn, true, transUCAll, true);
	}
	
	public static void buildAdempiere(Connection conn, String encoding) {
		try {
			Utils.runSQLFile(ddlAdempiere, conn, false, ddlAdempiereLog, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
