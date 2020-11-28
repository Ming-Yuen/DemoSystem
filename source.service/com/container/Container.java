package com.container;

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

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;

public class Container extends HttpServlet {
	private static final long serialVersionUID = 1963256997966895234L;
	
	private static String systemPath;

	public void init() {
		synchronized (this) {
			Global.getLogger.info(getClass().getName(), "service start");
			ServerConfigation.IPprocess();//build API swagger
			Connection dbConn = null;
			boolean isProcessSuccess = false;;
			try {
				dbConn = DatabaseHelper.getConnection();
				systemPath = getServletContext().getRealPath("");
				test();
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
	}
	
	public void test() throws Exception {
		hibernate.test();
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
