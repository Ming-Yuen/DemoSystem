package com.container;

import com.configuration.ServerConfigation;
import com.database.DatabaseHelper;
import com.database.DatabaseRecordHelper;
import com.database.DatabaseRecordRebuild;
import com.database.hibernate.HibernateHelper;
import com.database.hibernate.model.ProductsModel;
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

import javax.servlet.http.HttpServlet;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;

public class Container extends HttpServlet {
	private static final long serialVersionUID = 1963256997966895234L;
	
	private static String systemPath;

	public void init() {
		synchronized (this) {
			Global.getLogger.debug(getClass().getName(), "service start");
			ServerConfigation.IPprocess();//build API swagger
			Connection dbConn = null;
			boolean isProcessSuccess = false;;
			try {
				dbConn = DatabaseHelper.getConnection();
				systemPath = getServletContext().getRealPath("");
				test(dbConn);
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
				Global.getLogger.debug(getClass().getName(), "service end");
			}
		}
	}
	
	public static String getSystemPath() {
		return systemPath;
	}
	
	public void baidu() throws IOException {
		String url = "https://tieba.baidu.com/p/6656185856";
		String uri2 = "https://tieba.baidu.com/f/commit/post/add";
		HttpConnection.Mandatory mandatory = new HttpConnection.Mandatory();
		mandatory.setApiUrl(url);
		mandatory.setContentType(MediaType.APPLICATION_JSON);
		mandatory.setMethod(HttpMethod.POST);
		HttpConnection http = new HttpConnection(mandatory, null);
		System.out.println(http.connect().getContent());
	}
	
	public void test(Connection dbConn) throws Exception {
//		ProductsModel model = new ProductsModel();
////		model.productCode = "test";
//
//		model.productName = "test name";
//
//		model.productLine = "line";
//
//		model.productScale = "scale";
//
//		model.productVendor = "vendor";
//
//		model.productDescription = "desc";
//
//		model.quantityInStock = 10;
//
//		model.buyPrice = new BigDecimal(10);
//
//		model.MSRP = new BigDecimal(10);
//		helper.addObject(model);
//		HibernateHelper.query("select productName, productLine FROM ProductsModel where productName = '1969 Harley Davidson Ultimate Chopper'");
		
		
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
