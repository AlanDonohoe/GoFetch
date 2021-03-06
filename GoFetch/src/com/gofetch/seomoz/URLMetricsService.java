package com.gofetch.seomoz;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.logging.Logger;

import com.gofetch.seomoz.Authenticator;
import com.gofetch.utils.ConnectionUtil;

/**
 * 
 * Service class to call the various methods to
 * URL Metrics
 * 
 * URL Metrics is a paid API that returns the metrics about a URL or set of URLs.  
 * 
 * @author Radeep Solutions
 *
 */
public class URLMetricsService 
{
	private Authenticator authenticator;
	
	private static Logger log =  Logger.getLogger(URLMetricsService.class
			.getName());

	
	public URLMetricsService()
	{
		
	}
	
	/**
	 * 
	 * @param authenticator
	 */
	public URLMetricsService(Authenticator authenticator)
	{
		this.setAuthenticator(authenticator);
	}
	
	/**
	 * 
	 * This method returns the metrics about a URL or set of URLs.  
	 * 
	 * @param objectURL
	 * @param col This field filters the data to get only specific columns
	 * 			  col = 0 fetches all the data
	 * @return
	 * @throws IOException 
	 */
	public String getUrlMetrics(String objectURL, long col) throws Exception
	{
		
		String urlToFetch = "http://lsapi.seomoz.com/linkscape/url-metrics/" + URLEncoder.encode(objectURL) + "?" + authenticator.getAuthenticationStr();
		
		if(col > 0)
		{
			urlToFetch = urlToFetch + "&Cols=" + col;
		}
		
		log.info("urlToFetch:" + urlToFetch);
		
		String response = ConnectionUtil.get(urlToFetch);
		
		log.info("response:" + response);
		
		return response;
	}
	
	/**
	 * 
	 * Fetch all the Url Metrics for the objectURL
	 * 
	 * @param objectURL
	 * @return
	 * @throws IOException 
	 * 
	 * @see URLMetricsService#getUrlMetrics(String, int)
	 */
	public String getUrlMetrics(String objectURL) throws Exception
	{
		return getUrlMetrics(objectURL, 0);		
	}

	/**
	 * @param authenticator the authenticator to set
	 */
	public void setAuthenticator(Authenticator authenticator) {
		this.authenticator = authenticator;
	}

	/**
	 * @return the authenticator
	 */
	public Authenticator getAuthenticator() {
		return authenticator;
	}	
}
