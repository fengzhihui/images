package com.fzh.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VeDate {

	public static Date stringToDate(String strDate) {
		  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  ParsePosition pos = new ParsePosition(0);
		  return formatter.parse(strDate, pos);
	}
	
	public static String stringDate(String strDate,String format) {
		  SimpleDateFormat formatter = new SimpleDateFormat(format);
		  ParsePosition pos = new ParsePosition(0);
		  Date strtodate = formatter.parse(strDate, pos);
		  return formatter.format(strtodate);
	}
	
	/**
	 * yyyy-MM-dd格式的日期
	 * @param strDate
	 * @return
	 */
	public static String getDateYMD(Date date) {
		  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		  return formatter.format(date);
	}
	
	public static String DateToString(Date date) {
		  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  return formatter.format(date);
	}
	
	/**
	 * yyyyMMddHHmmss型日期
	 * @param date
	 * @return
	 */
	public static String getDateYMDHMS(Date date) {
		  SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		  return formatter.format(date);
	}
	
}
