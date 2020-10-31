package com.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatUtil {
	public static String getDatePoor(Date startDate, Date endDate) {
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		long ns = 1000;

		long diff = endDate.getTime() - startDate.getTime();
		long day = diff / nd;
		long hour = diff % nd / nh;
		long min = diff % nd % nh / nm;
		long sec = diff % nd % nh % nm / ns;

		return "day : " + day + " hour : " + hour + " min : " + min + " sec : " + sec + " mill : " + diff;
	}
	
	public static String convDateToString(Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}
	
	public static Date convStringToDate(String date, String pattern) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.parse(date);
	}
	
	public static Date cutTime(Date date, Integer round) {
		if(round <= 1) {
			return date;
		}
		Integer i = (int)Math.pow(10, round);
		return new Date(date.getTime()/i*i);
	}
}
