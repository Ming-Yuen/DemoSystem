package com.database.test;

import com.database.DatabaseRecordClass;
import com.database.annotation.DatabaseColumn;
import com.database.annotation.DatabasePrimaryKey;
import com.database.annotation.DatabaseTable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

//	public String[] getTablePrivateKey() {
//		return this.tablePrivateKey;
//	}
//
//	public void setWhereClauseKey(CharSequence whereClauseKey) {
//		this.whereClauseKey.append(whereClauseKey);
//	}
//
//	public StringBuffer getWhereClauseKey() {
//		return this.whereClauseKey;
//	}
//
//	public void setWhereClauseValues(List<Object> whereClauseValues) {
//		this.whereClauseValues.addAll(whereClauseValues);
//	}
//
//	public List<Object> getWhereClauseValues() {
//		return this.whereClauseValues;
//	}
}
