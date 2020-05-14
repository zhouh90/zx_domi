package com.zx.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String TIME_FORMAT = "HH:mm";
	private static final String DAY_FORMAT = "yyyy-MM-dd";

	public static Date addDays(Date date, int days) {
		return new Date(date.getTime() + days * 24 * 60 * 60 * 1000L);
	}

	public static Date strToDate(String strDateTime) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(DATETIME_FORMAT);
			return formatter.parse(strDateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date strToDate(String strDateTime, String format) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			return formatter.parse(strDateTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public static String dateToStr(Date date) {
		return dateToStr(date, TIME_FORMAT);
	}

	public static String dateToStr(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	public static Date getDayEndTime(Date d) {
		Calendar calendar = Calendar.getInstance();
		if (null != d) {
			calendar.setTime(d);
		}
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23,
				59, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return new Date(calendar.getTimeInMillis());
	}

	public static Date getDayStartTime(Date d) {
		Calendar calendar = Calendar.getInstance();
		if (null != d) {
			calendar.setTime(d);
		}
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0,
				0, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new Date(calendar.getTimeInMillis());
	}
}
