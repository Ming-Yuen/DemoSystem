package com.container;

import com.configuration.ServerConfigation;
import com.database.jdbc.DatabaseBackupFile;
import com.database.jdbc.DatabaseHelper;
import com.database.jdbc.DatabaseRecordRebuild;
import com.global.Global;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;

import org.apache.commons.lang3.StringUtils;

public class Container extends HttpServlet {
	
	public static final String pathSepar = File.separator;
	
	private static final long serialVersionUID = 1963256997966895234L;
	public  static final String catalinaLogPath = StringUtils.join(System.getProperty("catalina.base"), pathSepar, "logs", pathSepar);
	
	private static String systemPath;

	public void init() {
		systemInfomationLog();
		ServerConfigation.IPprocess();//build API swagger
		Connection dbConn = null;
		boolean isProcessSuccess = false;;
		try {
			dbConn = DatabaseHelper.getConnection();
			systemPath = getServletContext().getRealPath("");
			DatabaseBackupFile.backupSchema(dbConn);
			isProcessSuccess = true;
		} catch (Exception e) {
			Global.getLogger.error(getClass().getName(), e.getMessage(), e);
		} finally {
			DatabaseHelper.commit(dbConn, isProcessSuccess);
			Global.getLogger.info("System initialization completed");
		}
	}
	
	public static String getSystemPath() {
		return systemPath;
	}
	
	public void systemInfomationLog() {
		Global.getLogger.info("System initialization begins");
		String catalina = System.getProperty("catalina.base");
		Global.getLogger.info("System base directory : " + catalina);
	}

	public void dbProcess() throws SQLException {
		Connection dbConn = null;
		boolean isProcessSuccess = false;
		try {
			dbConn = DatabaseHelper.getConnection();
			DatabaseRecordRebuild.rebuildDatabase(dbConn);
			isProcessSuccess = true;
		} catch (Exception e) {
			Global.getLogger.error(getClass().getName(), e.getMessage(), e);
		} finally {
			if (isProcessSuccess && dbConn != null) {
				dbConn.commit();
			}
		}
	}
}
