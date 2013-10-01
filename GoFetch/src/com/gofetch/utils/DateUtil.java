package com.gofetch.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.joda.time.DateTime;

public class DateUtil {

	static private final long MILLIS_IN_A_DAY = 86400000; // = 1000*60*60*24; 
	
	
	public static String getTodaysDateAsDDMMYYYY(){
		
		return(getFormattedDate(getTodaysDate()));
		
	}

	/**
	 * 
	 * @param strDate
	 * @return strDate formatted: "yyyy-MM-dd"
	 * @throws ParseException
	 */
	public static Date getDateFromString(String strDate) throws ParseException{

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d=sdf.parse(strDate);
		
		return d;
	}

	/**
	 * 
	 * @param date 
	 * @return date formatted: "dd-MM-yyyy"
	 */
	public static String getFormattedDate(Date date){
		return (new  SimpleDateFormat("dd-MM-yyyy").format(date));

	}

	public static Date getTodaysDate(){

		java.util.Calendar cal = java.util.Calendar.getInstance();
		java.util.Date utilDate = cal.getTime();
		Date date = new Date(utilDate.getTime());
		
		//TODO: replace all these with joda time impl...
		//Jodatime impl:
		//DateTime dt = new DateTime();

		return date;

	}


	public static Date getYesterDaysDate(){

		Date yesterdaysDate = new java.util.Date(new java.util.Date().getTime() - MILLIS_IN_A_DAY); 

		return yesterdaysDate; 
	}
	
	public static Date getTommorrowsDate(){
		Date tmwDate = new java.sql.Date(new java.util.Date().getTime() + MILLIS_IN_A_DAY); 

		return tmwDate; 
	}
	
	public static Date getNextWeeksDate(){
		Date tmwDate = new java.sql.Date(new java.util.Date().getTime() + (MILLIS_IN_A_DAY * 7)); 

		return tmwDate; 
	}
	
	
	public static Date getNextMonthsDate(){
		Date tmwDate = new java.sql.Date(new java.util.Date().getTime() + (MILLIS_IN_A_DAY * 30)); 

		return tmwDate; 
	}
	
}
