package com.container;

import com.common.tools.DatabaseBackupFile;
import com.common.util.DateUtils;
import com.common.util.FileOutputStreamUtil;
import com.configuration.ServerConfigation;
import com.database.DatabaseHelper;
import com.database.DatabaseRecordHelper;
import com.database.DatabaseRecordRebuild;
import com.database.hibernate.HibernateHelper;
import com.database.hibernate.model.ProductsModel;
import com.database.hibernate.testcase.hibernate;
import com.database.test.Products;
import com.global.Global;
import com.google.gson.Gson;
import com.net.util.HttpConnection;
import com.scheduler.ThreadScheduler;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;

public class Container extends HttpServlet {
	
	public static final String pathSepar = File.separator;
	
	private static final long serialVersionUID = 1963256997966895234L;
	public  static final String catalinaLogPath = StringUtils.join(System.getProperty("catalina.base"), pathSepar, "logs", pathSepar);
	
	private static String systemPath;

	public void init() {
		Global.getLogger.info(getClass().getName(), "service start");
		ServerConfigation.IPprocess();//build API swagger
		Connection dbConn = null;
		boolean isProcessSuccess = false;;
		try {
			dbConn = DatabaseHelper.getConnection();
			systemPath = getServletContext().getRealPath("");
			DatabaseBackupFile.backupAllSchema(dbConn);
//			backupDatabase(dbConn);
			isProcessSuccess = true;
		} catch (Exception e) {
			Global.getLogger.error(getClass().getName(), e.getMessage(), e);
		} finally {
			try {
				if (isProcessSuccess && dbConn!= null) {
					dbConn.commit();
				}else if(dbConn!= null){
					dbConn.rollback();
				}
			}catch(SQLException e) {
				Global.getLogger.error(this.getClass().getName(), e.getMessage(), e);
			}
			Global.getLogger.info(getClass().getName(), "service end");
		}
	}
	

	String debugPath = System.getProperty("catalina.base") + File.separator + "backupdatabase" + File.separator;
	public void backupDatabase(Connection dbConn) throws Exception {
		List<String> resultSet = new ArrayList<String>();
		DatabaseMetaData metaData = dbConn.getMetaData();
        ResultSet res = metaData.getCatalogs();

        while (res.next()) {
            resultSet.add(res.getString("TABLE_CAT"));
        }
        
		String json = new Gson().toJson(DatabaseRecordHelper.queryRecord(dbConn, UserMission.class));
		
		ArrayList<ArrayList<Object>> result = DatabaseHelper.query(dbConn, "select table_name from information_schema.tables");
		
		json = new Gson().toJson(DatabaseRecordHelper.queryRecord(dbConn, UserInfo.class));
		FileOutputStreamUtil util = new FileOutputStreamUtil(false);
//		hibernate.test();
	}
	
	public static String getSystemPath() {
		return systemPath;
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
