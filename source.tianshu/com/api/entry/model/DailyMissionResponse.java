package com.api.entry.model;

import java.util.ArrayList;

import com.api.std.Response;

public class DailyMissionResponse extends Response{
	
	public ArrayList<UserMission> mission = new ArrayList<UserMission>();
	
	public ArrayList<UserMission> lastDayMission = new ArrayList<UserMission>();

	public static class UserMission{
		public String missionType;
		public String UserId;
		public String UserPwd;
	}
}
