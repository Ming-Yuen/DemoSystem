package com.api.model;

import java.util.Date;

import com.file.annotation.ExcelFieldName;

public class FileExcelRecord {

	@ExcelFieldName("Item code")
	public String itemCode;
	
	@ExcelFieldName("Amount")
	public Integer amount;
	
	@ExcelFieldName("Modify date")
	public Date modifyDate;
}
