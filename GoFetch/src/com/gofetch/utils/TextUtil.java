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
	 * @param url ex: http://www.marksandspencer.com/Maternity....
	 * @return - section between the 1st '.' and 1st '/' of the url param
	 * 	ex: marksandspencer.com
	 * 
	 * Issue: goo.gl/gf1PY - returns gl as domain.... Not goo.gl
	 */
	public static String returnDomainName(String url){

		int firstDot, firstSlash;

		if(url.contains("www")){
			firstDot = url.indexOf('.');
			firstDot++;
		}else // dealing with shortened URL:
			firstDot = 0;

		firstSlash = url.indexOf('/', firstDot); 
		
		// deal with no trailing slash
		if(-1 == firstSlash){
			firstSlash = url.length();
			firstSlash--;
			
		}

		return url.substring(firstDot, firstSlash);

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
	
	/**
	 * 
	 * @param url - URL address to parse
	 * @return - tidy version of the passed URL
	 * 
	 * removes http://www, https://www, and all trailing args after the first ?
	 * ex. passed: http://www.marksandspencer.com/Maternity-Bras-Guide-Lingerie-Underwear-Womens/b/908657031?ie=UTF8&ie=UTF8?ie=UTF8&pf_rd_r=1R8EMRGQ20MJB1FAM6ZT&pf_rd_m=A2BO0OYVBKIQJM&pf_rd_t=101&pf_rd_i=1323471031&pf_rd_p=475115433&pf_rd_s=left-nav-2
	 * returned: www.marksandspencer.com/Maternity-Bras-Guide-Lingerie-Underwear-Womens/b/908657031
	 */
	public static String tidyURLAddress(String url){
		
		String parsedAddress;
		Integer secondDot, firstQuestionMark;
		
		// remove http:
		parsedAddress = url.substring(7);
		secondDot = parsedAddress.indexOf('.', 8);// find the dot after the first dot of http://www.
		firstQuestionMark = parsedAddress.indexOf('?', secondDot++);
		
		if(firstQuestionMark < 0) // if there's no trailing arguments...
			return parsedAddress; // just return
		
		parsedAddress = parsedAddress.substring(0, firstQuestionMark--);
		
		return parsedAddress;
		
	}


}
