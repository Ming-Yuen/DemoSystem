package com.api.tianshu.util;

import java.sql.Connection;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.api.entry.model.CreateMissionRequest;
import com.api.entry.model.CreateUserRequest;
import com.database.DatabaseRecordHelper;
import com.tianshu.database.UserInfoRecord;

public class UserUtil {

	public void createUser(Connection dbConn, CreateUserRequest request) throws Exception {
		UserInfoRecord record 	= new UserInfoRecord();
		record.userId 			= StringUtils.isBlank(request.userId) 	? null : request.userId.trim();
		record.gamename			= StringUtils.isBlank(request.gamename) ? null : request.gamename.trim();
		record.pwd1 			= StringUtils.isBlank(request.password1)? null : request.password1.trim();
		record.pwd2 			= StringUtils.isBlank(request.password2)? null : request.password2.trim();
		record.usertype			= StringUtils.isBlank(request.usertype) ? null : request.usertype.trim();
		record.email 			= StringUtils.isBlank(request.email) 	? null : request.email.trim();
		record.emailPwd			= StringUtils.isBlank(request.emailPwd) ? null : request.emailPwd.trim();
		record.content 			= StringUtils.isBlank(request.usercontent) 	? null : request.usercontent.trim();
		record.remark 			= StringUtils.isBlank(request.remark) 	? null : request.remark.trim();
		record.createDatetime 	= new Date();
		record.modifyDatetime 	= new Date();
		DatabaseRecordHelper.insert(dbConn, record);
		
		CreateMissionRequest missionRequest = new CreateMissionRequest();
		missionRequest.userId				= request.userId;
		missionRequest.chuyao1				= request.chuyao1;
		missionRequest.chuyao2				= request.chuyao2;
		missionRequest.xinmo150				= request.xinmo150;
		missionRequest.xinmo160				= request.xinmo160;
		missionRequest.fuben				= request.fuben;
		missionRequest.wangzhe				= request.wangzhe;
		missionRequest.laoyou				= request.laoyou;
		missionRequest.ReferrerUserId		= request.ReferrerUserId;
		missionRequest.startDate			= request.startDate;
		missionRequest.months				= request.months;
		missionRequest.days					= request.days;
		missionRequest.missionremark		= request.missionremark;
		new MissionUtil().createMission(dbConn, missionRequest);
	}
}
