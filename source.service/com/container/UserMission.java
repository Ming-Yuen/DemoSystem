package com.container;

import java.sql.Timestamp;

import com.database.annotation.DatabaseColumn;
import com.database.annotation.DatabasePrimaryKey;
import com.database.annotation.DatabaseTable;

@DatabaseTable(tableName = "UserMission")
public class UserMission {

	@DatabasePrimaryKey()
	@DatabaseColumn(columnName = "UserId")
	public String 	UserId;
	@DatabaseColumn(columnName = "MissionType")
	public String 	MissionType;
	@DatabaseColumn(columnName = "StartMissionDate")
	public String 	StartMissionDate;
	@DatabaseColumn(columnName = "EndMissionDate")
	public String 	EndMissionDate;
	@DatabaseColumn(columnName = "ReferrerUserId")
	public String 	ReferrerUserId;
	@DatabaseColumn(columnName = "Remark")
	public String 	Remark;
	@DatabaseColumn(columnName = "createDateTime")
	public String 	createDateTime;
	@DatabaseColumn(columnName = "modifyDateTime")
	public String 	modifyDateTime;
}
