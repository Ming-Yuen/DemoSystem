package com.container;

import com.database.DatabaseRecordClass;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Products implements DatabaseRecordClass {
	public final String tableName = "Products";

	public final String[] tablePrivateKey = new String[] { "productCode" };

	public final StringBuffer whereClauseKey = new StringBuffer();

	public final ArrayList<Object> whereClauseValues = new ArrayList<Object>();

	public String productCode;

	public String productName;

	public String productLine;

	public String productScale;

	public String productVendor;

	public String productDescription;

	public Integer quantityInStock;

	public BigDecimal buyPrice;

	public BigDecimal MSRP;

	public String getTableName() {
		return "Products";
	}

	public String[] getTablePrivateKey() {
		return this.tablePrivateKey;
	}

	public void setWhereClauseKey(CharSequence whereClauseKey) {
		this.whereClauseKey.append(whereClauseKey);
	}

	public StringBuffer getWhereClauseKey() {
		return this.whereClauseKey;
	}

	public void setWhereClauseValues(List<Object> whereClauseValues) {
		this.whereClauseValues.addAll(whereClauseValues);
	}

	public List<Object> getWhereClauseValues() {
		return this.whereClauseValues;
	}
}
