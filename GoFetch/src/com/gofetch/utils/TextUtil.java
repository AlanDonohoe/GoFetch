package com.gofetch.utils;

public class TextUtil {

	/**
	 * 
	 * @param stringToClean - a string representation of a number
	 * @return - the number, as a string, as an integer
	 */
	public static String returnInteger(String stringToClean) {

		String cleanString;

		int indexOfDecPoint = stringToClean.indexOf('.');

		if (indexOfDecPoint > 0) // in case there's no decimal place
		{
			cleanString = stringToClean.substring(0, indexOfDecPoint);
		} else {
			cleanString = stringToClean;
		}

		return cleanString;
	}

	/**
	 * 
	 * @param url 
	 * @return - section between the 1st '.' and 1st '/' of the url param
	 */
	public static String returnDomainName(String url){

		int firstDot, firstSlash;
		String newURL;

		//replaced 2 lines below with this line
		newURL = url.replaceFirst("www.", "");

		firstSlash = newURL.indexOf('/', 0); 
		firstSlash++;

		return newURL.substring(0, firstSlash);

	}

	public static String addHTTPToURL(String url){

		return ("http://" + url);
	}

	/*
	 * If url string does NOT have a '/' on the end, then this attaches '/'
	 * if url DOES have a '/' on the end, then just returns that string.
	 */
	public static String addSlashToEndOfString(String url){

		if(!url.endsWith("/")){
			return(url + "/");
		}
		else
			return url;

	}
	
	
	public static String removeSlashFromEndOfString(String url){
		
		if(url.endsWith("/")){
			
			int newLength = url.length();
			newLength--;
			url = url.substring(0,newLength);
		}
		return url;
		
	}
	
	
	public static String replaceHttpsWithHttp(String urlAddress){
		
		if(urlAddress.startsWith("https")){
			urlAddress = urlAddress.replace("https", "http");
		}
		
		return urlAddress;
			
	}
	
	public static String removeWSFromStartAndEnd(String stringToClear){
		
		
		//from: http://stackoverflow.com/questions/4728625/why-trim-is-not-working
		stringToClear = stringToClear.replace(String.valueOf((char) 160), " ").trim();
		
		return stringToClear;
	}
	
	/*
	 * returns same url passed as parameter, but with: 
	 * 1) any white space removed from beginning and end, 
	 * 2) and any trailing slash removed if present.
	 */
	public static String standardiseURL(String url){
		
		url = removeWSFromStartAndEnd(url);
		url = removeSlashFromEndOfString(url);
		
		//Pending - see if there's erroneous duplication with http & https versions of same url... - but leave for user discretion for now.
		
		//url = replaceHttpsWithHttp(url);
		
		return url;
	}


}
