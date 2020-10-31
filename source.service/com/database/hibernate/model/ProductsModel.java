package com.database.hibernate.model;

import java.math.BigDecimal;
//C:\Users\Admin\eclipse-workspace\DemoSystemBackEnd\source.service\com\database\hibernate\model\ProductsModel.java
//C:\Users\Admin\eclipse-workspace\DemoSystemBackEnd\src\ProductsModel.xml
public class ProductsModel {
	private String productCode;

	private String productName;

	private String productLine;

	private String productScale;

	private String productVendor;

	private String productDescription;

	private Integer quantityInStock;

	private BigDecimal buyPrice;

	private BigDecimal MSRP;

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductLine() {
		return productLine;
	}

	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}

	public String getProductScale() {
		return productScale;
	}

	public void setProductScale(String productScale) {
		this.productScale = productScale;
	}

	public String getProductVendor() {
		return productVendor;
	}

	public void setProductVendor(String productVendor) {
		this.productVendor = productVendor;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public Integer getQuantityInStock() {
		return quantityInStock;
	}

	public void setQuantityInStock(Integer quantityInStock) {
		this.quantityInStock = quantityInStock;
	}

	public BigDecimal getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(BigDecimal buyPrice) {
		this.buyPrice = buyPrice;
	}

	public BigDecimal getMSRP() {
		return MSRP;
	}

	public void setMSRP(BigDecimal mSRP) {
		MSRP = mSRP;
	}

}
