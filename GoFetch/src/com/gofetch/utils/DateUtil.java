package com.gofetch.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {

	static private final long MILLIS_IN_A_DAY = 86400000; // = 1000*60*60*24; 

	public static Date getDateFromString(String strDate) throws ParseException{

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d=sdf.parse(strDate);
		
		return d;
	}

	public static String getFormattedDate(Date date){
		return (new  SimpleDateFormat("dd-MM-yyyy").format(date));

	}

	public static Date getTodaysDate(){

		java.util.Calendar cal = java.util.Calendar.getInstance();
		java.util.Date utilDate = cal.getTime();
		Date date = new Date(utilDate.getTime());

		return date;

	}


	public static Date getYesterDaysDate(){

		Date yesterdaysDate = new java.sql.Date(new java.util.Date().getTime() - MILLIS_IN_A_DAY); 

		return yesterdaysDate; 
	}
}
