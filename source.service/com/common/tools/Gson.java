package com.common.tools;

public class Gson extends JSON{
	
	private static final com.google.gson.Gson gson = new com.google.gson.Gson();

	public static String toJson(Object obj){
		return gson.toJson(obj);
	}
}
