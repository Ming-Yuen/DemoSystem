package com.container;

import com.configuration.ServerConfigation;
import com.database.DatabaseHelper;
import com.database.DatabaseRecordHelper;
import com.database.DatabaseRecordRebuild;
import com.database.hibernate.model.ProductsModel;
import com.database.test.Products;
import com.global.Global;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;

public class Container extends HttpServlet {
	private static final long serialVersionUID = 1963256997966895234L;

	public void init() {
		synchronized (this) {
			Global.getLogger.debug(getClass().getName(), "service start");
			Connection dbConn = null;
			boolean isProcessSuccess = false;;
			try {
				dbConn = DatabaseHelper.getConnection();
				ServerConfigation.IPprocess();
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
	
	public void test(Connection dbConn) throws Exception {
		ProductsModel model = new ProductsModel();
		model.productCode = "test";

		model.productName = "test name";

		model.productLine = "line";

		model.productScale = "scale";

		model.productVendor = "vendor";

		model.productDescription = "desc";

		model.quantityInStock = 10;

		model.buyPrice = new BigDecimal(10);

		model.MSRP = new BigDecimal(10);
//		HibernateHelper helper = new HibernateHelper();
//		helper.addObject(model);
//		List<ProductsModel> record = helper.getObject(ProductsModel.class);
//		System.out.println(record.get(0).productCode);
		ArrayList<Products> result = DatabaseRecordHelper.queryRecord(dbConn, Products.class);
		Gson son = new Gson();
		for(Products p : result) {
			System.out.println(son.toJson(p));
		}
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
			if (isProcessSuccess) {
				dbConn.commit();
			}
		}
	}
}
