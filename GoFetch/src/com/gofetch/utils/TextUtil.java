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


}
