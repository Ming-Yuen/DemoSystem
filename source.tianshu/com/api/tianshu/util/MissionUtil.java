package com.api.tianshu.util;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.api.entry.model.CreateMissionRequest;
import com.api.entry.model.DailyMissionResponse;
import com.database.DatabaseHelper;
import com.database.DbException;
import com.tianshu.database.DailyMissionRequest;
import com.tianshu.database.UserMission;

public class MissionUtil {

	public void createMission(Connection dbConn, CreateMissionRequest request) throws Exception {
		ArrayList<ArrayList<Object>> checkUserExists = DatabaseHelper.query(dbConn, "select 1 from userinfo where userinfo.userid = ?", new Object[] {request.userId});
		if(checkUserExists.isEmpty()) {
			throw new Exception("User not exists in userinfo, userid : " + request.userId);
		}
		
		if(!StringUtils.isBlank(request.chuyao1)) {//高妖
			createMissionRecord(dbConn, request, request.chuyao1);
		}
		if(!StringUtils.isBlank(request.chuyao2)) {//特除
			createMissionRecord(dbConn, request, request.chuyao2);
		}
		if(!StringUtils.isBlank(request.xinmo150)) {//150心魔
			createMissionRecord(dbConn, request, request.xinmo150);
		}
		if(!StringUtils.isBlank(request.xinmo160)) {//160心魔
			createMissionRecord(dbConn, request, request.xinmo160);
		}
		if(!StringUtils.isBlank(request.fuben)) {//160心魔
			createMissionRecord(dbConn, request, request.fuben);
		}
		if(!StringUtils.isBlank(request.wangzhe)) {//王者
			createMissionRecord(dbConn, request, request.wangzhe);
		}
		if(!StringUtils.isBlank(request.laoyou)) {//老友
			createMissionRecord(dbConn, request, request.laoyou);
		}
	}
	public void createMissionRecord(Connection dbConn, CreateMissionRequest request, String missionType) throws Exception {
		UserMission mission = new UserMission();
		mission.userId 				= StringUtils.isBlank(request.userId) ? null : request.userId.trim();
		mission.missionType 		= missionType;
		mission.ReferrerUserId 		= StringUtils.isBlank(request.ReferrerUserId) ? null : request.ReferrerUserId.trim();
		mission.StartMissionDate 	= DateUtils.parseDate(request.startDate, "yyyy-MM-dd");
		
		Date endDate = mission.StartMissionDate;
		if(request.months != null && request.months > 0) {
			endDate = DateUtils.addMonths(endDate, request.months);
		}
		if(request.days != null && request.days > 0) {
			endDate = DateUtils.addDays(endDate, request.days);
		}
		mission.EndMissionDate 		= DateUtils.addSeconds(endDate, -1);//43199 = 60x60x12 seconds
		mission.Remark 				= StringUtils.isBlank(request.missionremark) ? null : request.missionremark.trim();;
//		DatabaseRecordHelper.insert(dbConn, mission);
		String sql = "insert into UserMission (userId, missionType, ReferrerUserId, StartMissionDate, EndMissionDate, Remark) values (" + StringUtils.repeat("'%s'", ",", 6) + ")";
		SimpleDateFormat  parse = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		sql = sql.format(sql, mission.userId, mission.missionType, mission.ReferrerUserId, parse.format(mission.StartMissionDate), parse.format(mission.EndMissionDate), mission.Remark);
		DatabaseHelper.update(dbConn, sql, null);
	}
	public DailyMissionResponse dailyMission(Connection dbConn, DailyMissionRequest request) throws DbException, ParseException {
		String sql = new StringBuilder("select info.UserId, info.Pwd, mission.MissionType from UserInfo info, UserMission mission ")
							   .append("where info.userId = mission.UserId and ")
							   .append("StartMissionDate < ? and ? < EndMissionDate").toString();
		ArrayList<ArrayList<Object>> dbResultList = DatabaseHelper.query(dbConn, sql, new Object[] {request.missionDate, DateUtils.addDays(DateUtils.parseDate(request.missionDate, "yyyy-MM-dd"), -1)});
		
		DailyMissionResponse response = new DailyMissionResponse();
		for(int index = 0; index < dbResultList.size(); index++) {
			DailyMissionResponse.UserMission userMission = new DailyMissionResponse.UserMission();
			userMission.UserId 		= DatabaseHelper.<String>getValue(dbResultList, index, 0);
			userMission.UserPwd 	= DatabaseHelper.<String>getValue(dbResultList, index, 1);
			userMission.missionType = DatabaseHelper.<String>getValue(dbResultList, index, 2);
			response.mission.add(userMission);
		}
		
		sql = new StringBuilder("select info.UserId, info.Pwd, mission.MissionType from UserInfo info, UserMission mission ")
						.append("where info.userId = mission.UserId and ")
						.append("EndMissionDate = ?").toString();
		dbResultList = DatabaseHelper.query(dbConn, sql, new Object[] {DateUtils.parseDate(request.missionDate, "yyyy-MM-dd")});
		
		for(int index = 0; index < dbResultList.size(); index++) {
			DailyMissionResponse.UserMission userMission = new DailyMissionResponse.UserMission();
			userMission.UserId 		= DatabaseHelper.<String>getValue(dbResultList, index, 0);
			userMission.UserPwd 	= DatabaseHelper.<String>getValue(dbResultList, index, 1);
			userMission.missionType = DatabaseHelper.<String>getValue(dbResultList, index, 2);
			response.lastDayMission.add(userMission);
		}
		return response;
	}

}
