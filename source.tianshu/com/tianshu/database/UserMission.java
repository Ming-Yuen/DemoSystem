package com.tianshu.database;

import java.util.Date;

import com.database.jdbc.annotation.DatabaseColumn;
import com.database.jdbc.annotation.DatabasePrimaryKey;
import com.database.jdbc.annotation.DatabaseTable;

@DatabaseTable(tableName = "UserMission")
public class UserMission {

	@DatabasePrimaryKey()
	@DatabaseColumn(columnName = "UserId")
	public String userId;

	@DatabasePrimaryKey()
	@DatabaseColumn(columnName = "missionType")
	public String missionType;

	@DatabasePrimaryKey()
	@DatabaseColumn(columnName = "StartMissionDate")
	public Date StartMissionDate;
	
	@DatabaseColumn(columnName = "EndMissionDate")
	public Date EndMissionDate;
	
	@DatabaseColumn(columnName = "ReferrerUserId")
	public String ReferrerUserId;
	
	@DatabaseColumn(columnName = "Remark")
	public String Remark;
	
	@DatabaseColumn(columnName = "createDateTime")
	public Date createDateTime;
	
	@DatabaseColumn(columnName = "modifyDateTime")
	public Date modifyDateTime;
}
