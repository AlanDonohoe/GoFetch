package com.gofetch;

public class GoFetchConstants {

	public static final int MIN_VALID_URL_LENGTH = 12;			 // used for user input validation - min = http://a.com 

	public static final double SOCIAL_DATA_RANGE_MIN = 5; 		// if social data difference in % is below this, then the url's social data frequency is decremented
	
	public static final double SOCIAL_DATA_RANGE_MAX = 10; 		// if social data difference in % is above this, then the url's social data frequency is incremented
	
	public static final Integer DAILY_FREQ   = 3;						// Social data freq
	public static final Integer WEEKLY_FREQ  = 2;						//having problems getting correct data back from persisted URLs  - remove the final from these to see if that helps
	public static final Integer MONTHLY_FREQ = 1;
	
	public static final int ALLOWED_SOCIAL_DATA_DIFFERENCE = 2;	// %age difference allowed between url with trailing slash and url without trailing slash		
	
	public static final int URL_ENTERED_BY_SEOMOZ = 0;
	public static final int URL_ENTERED_BY_USER = 1;
	
	public static final int NO_OF_URLS_TO_PROCESS = 20; // no of urls to process (get backlinks for from SEOMoz) on a daily basis 
	
	public static final int MAX_NO_OF_SOCIAL_URLS = 5000; // max no of urls to pull and break into social crawl tasks
	
	///////////////////
	// used when hitting social services for data - url goes in between preEndpoint and postEndPoint

	public static final String deliciousEndPoint  = "http://feeds.delicious.com/v2/json/urlinfo/data?url=";
	
	public static final String faceBookPreEndPoint  = "http://api.ak.facebook.com/restserver.php?v=1.0&method=links.getStats&urls=";
	public static final String faceBookPostEndPoint = "&format=json";
	
	public static final String googlePlusEndPoint  = "https://clients6.google.com/rpc?key=AIzaSyCKSbrvQasunBoV16zDH9R33D88CeLr9gQ";
	public static final String googlePrePost =  "[{\"method\":\"pos.plusones.get\",\"id\":\"p\",\"params\":{\"nolog\":true,\"id\":\"";
	public static final String googlePostPost =	"\",\"source\":\"widget\",\"userId\":\"@viewer\",\"groupId\":\"@self\"},\"jsonrpc\":\"2.0\",\"key\":\"p\",\"apiVersion\":\"v1\"}]";
			
	public static final String linkedInPreEndPoint   = "http://www.linkedin.com/countserv/count/share?url=";
	public static final String linkedInPostEndPoint  = "&callback=myCallback&format=jsonp";
	
	public static final String pinterestEndPoint  = "http://api.pinterest.com/v1/urls/count.json?url=";

	public static final String stumbleUponEndPoint = "http://www.stumbleupon.com/services/1.01/badge.getinfo?url=";
	
	public static final String twitterPreEndPoint  = "http://urls.api.twitter.com/1/urls/count.json?url="; 
	public static final String twitterPostEndPoint = "&callback=twttr.receiveCount";
	

}
