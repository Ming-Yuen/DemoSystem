package com.api.model;

import java.util.Date;

import com.file.annotation.FileFieldName;

public class FileExcelRecord {

	@FileFieldName("Item code")
	public String itemCode;
	
	@FileFieldName("Amount")
	public Integer amount;
	
	@FileFieldName("Modify date")
	public Date modifyDate;
}
