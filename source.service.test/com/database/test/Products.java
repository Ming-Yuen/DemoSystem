package com.database.test;

import java.math.BigDecimal;

import com.database.jdbc.annotation.DatabaseColumn;
import com.database.jdbc.annotation.DatabasePrimaryKey;
import com.database.jdbc.annotation.DatabaseTable;

@DatabaseTable(tableName = "Products")
public class Products {//implements DatabaseRecordClass 
//	public final String tableName = "Products";
//
//	public final String[] tablePrivateKey = new String[] { "productCode" };
//
//	public final StringBuffer whereClauseKey = new StringBuffer();
//
//	public final ArrayList<Object> whereClauseValues = new ArrayList<Object>();

	@DatabasePrimaryKey()
	@DatabaseColumn(columnName = "productCode")
	public String productCode;
	
	@DatabaseColumn(columnName = "productName")
	public String productName;

	@DatabaseColumn(columnName = "productLine")
	public String productLine;

	@DatabaseColumn(columnName = "productScale")
	public String productScale;
	
	@DatabaseColumn(columnName = "productVendor")
	public String productVendor;
	
	@DatabaseColumn(columnName = "productDescription")
	public String productDescription;
	
	@DatabaseColumn(columnName = "quantityInStock")
	public Integer quantityInStock;
	
	@DatabaseColumn(columnName = "buyPrice")
	public BigDecimal buyPrice;
	
	@DatabaseColumn(columnName = "MSRP")
	public BigDecimal MSRP;

	public String getTableName() {
		return "Products";
	}
}
