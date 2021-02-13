package com.database.hibernate.testcase;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.database.hibernate.HibernateHelper;
import com.database.hibernate.model.ProductsModel;
import com.google.gson.Gson;

public class hibernate {

	public static void test() throws Exception {
//		ProductsModel model = new ProductsModel();
//		model.setId(UUID.randomUUID().toString());
//		model.setProductCode("Test");
//		model.setProductName("test name");
//
//		model.setProductLine("line");
//
//		model.setProductScale("scale");
//
//		model.setProductVendor("vendor");
//
//		model.setProductDescription("desc");
//
//		model.setQuantityInStock(10);
//
//		model.setBuyPrice(new BigDecimal(10));
//
//		model.setMSRP(new BigDecimal(10));
//		model.setModifytime(new java.sql.Date(new Date().getTime()));
//		HibernateHelper.addObject(model);
		List<?> result = HibernateHelper.query("select productName, productLine FROM ProductsModel where productName = '1969 Harley Davidson Ultimate Chopper'");
		System.out.println(new Gson().toJson(result));
		
	}
}
