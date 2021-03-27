package com.container;

import com.database.jdbc.annotation.DatabaseColumn;
import com.database.jdbc.annotation.DatabasePrimaryKey;
import com.database.jdbc.annotation.DatabaseTable;

@DatabaseTable(tableName = "UserInfo")
public class UserInfo {
	
	@DatabasePrimaryKey()
	@DatabaseColumn(columnName = "UserId")
	public String 	UserId;
	@DatabaseColumn(columnName = "GameName")
	public String 	GameName;
	@DatabaseColumn(columnName = "Pwd")
	public String 	Pwd;
	@DatabaseColumn(columnName = "SecondPWd")
	public String 	SecondPWd;
	@DatabaseColumn(columnName = "UserType")
	public String 	UserType;
	@DatabaseColumn(columnName = "email")
	public String 	email;
	@DatabaseColumn(columnName = "emailPWD")
	public String 	emailPWD;
	@DatabaseColumn(columnName = "idCard")
	public String 	idCard;
	@DatabaseColumn(columnName = "content")
	public String 	content;
	@DatabaseColumn(columnName = "Remark")
	public String 	Remark;
	@DatabaseColumn(columnName = "CreateDateTime")
	public String 	CreateDateTime;
	@DatabaseColumn(columnName = "ModifyDateTime")
	public String	ModifyDateTime;
}
