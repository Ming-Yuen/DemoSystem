package com.common.util;

import java.util.Date;

public class DateTimeUtils {
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
}
