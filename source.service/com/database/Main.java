package com.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Main {
	
	public static final String SELECT = "select";
	public static final String UPDATE = "update";
	public static final String SELECT_ALL_Field = "*";
	
	public static String sql = "select * from table1 where (select 1 from table2 where table)";

	public static void main(String[] args) {
		sql = sql.toLowerCase();
		if(sql.startsWith(SELECT)) {
			String tempSql = new String(sql);
			SQLDecodeInfo sqlInfo = new SQLDecodeInfo();
			sqlInfo.sqlFieldList = tempSql.substring(SELECT.length(), tempSql.indexOf("from"));
			System.out.println(sqlInfo.sqlFieldList);
		}
	}

	public void split(String value, String delimiter) {
		List<String> valueArr = new ArrayList<String>();
		String tempValue = new String(value);
		while(tempValue.isEmpty()) {
			if(tempValue.startsWith(delimiter)) {
				tempValue = StringUtils.right(tempValue, tempValue.length() - delimiter.length());
			}else {
				
			}
		}
	}
	
	public static class SQLDecodeInfo{
		public String sqlFieldList;
		public ArrayList<String> table;
		public String whereClause;
	}
}
