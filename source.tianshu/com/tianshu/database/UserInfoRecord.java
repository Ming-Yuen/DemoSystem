package com.tianshu.database;

import java.util.Date;

import com.database.annotation.DatabaseColumn;
import com.database.annotation.DatabasePrimaryKey;
import com.database.annotation.DatabaseTable;

@DatabaseTable(tableName = "UserInfo")
public class UserInfoRecord {

	@DatabasePrimaryKey()
	@DatabaseColumn(columnName = "UserId")
	public String userId;

	@DatabaseColumn(columnName = "GameName")
	public String gamename;

	@DatabaseColumn(columnName = "Pwd")
	public String pwd1;

	@DatabaseColumn(columnName = "SecondPWd")
	public String pwd2;

	@DatabaseColumn(columnName = "UserType")
	public String usertype;

	@DatabaseColumn(columnName = "email")
	public String email;
	
	@DatabaseColumn(columnName = "emailPwd")
	public String emailPwd;

	@DatabaseColumn(columnName = "content")
	public String content;

	@DatabaseColumn(columnName = "Remark")
	public String remark;

	@DatabaseColumn(columnName = "CreateDateTime")
	public Date createDatetime;

	@DatabaseColumn(columnName = "ModifyDateTime")
	public Date modifyDatetime;
}
