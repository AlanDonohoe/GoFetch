package com.gofetch.seomoz;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.logging.Logger;

import com.gofetch.json.JsonWrapper;
import com.gofetch.seomoz.Authenticator;
import com.gofetch.utils.ConnectionUtil;
import com.gofetch.utils.HttpResponseReader;

/**
 * 
 * Service class to call the various methods to Links API
 * 
 * Links api returns a set of links to a page or domain.
 * 
 * @author Radeep Solutions
 * 
 */
public class LinksService {
	private Authenticator authenticator;
	
	private static Logger log = Logger.getLogger(LinksService.class
			.getName());


	public LinksService() {

	}

	/**
	 * 
	 * @param authenticator
	 */
	public LinksService(Authenticator authenticator) {
		this.setAuthenticator(authenticator);
	}

	/**
	 * This method returns a set of links to a page or domain.
	 * 
	 * @param objectURL
	 * @param scope
	 *            determines the scope of the Target link, as well as the Source
	 *            results.
	 * @param filters
	 *            filters the links returned to only include links of the
	 *            specified type. You may include one or more of the following
	 *            values separated by '+'
	 * @param sort
	 *            determines the sorting of the links, in combination with limit
	 *            and offset, this allows fast access to the top links by
	 *            several orders:
	 * @param colSource
	 *            specifies data about the source of the link is included
	 * @param colLink
	 *            specifies data about the backlink to be included..... Added AD
	 *            17-4-12
	 * @param offset
	 *            The start record of the page can be specified using the Offset
	 *            parameter
	 * @param limit
	 *            The size of the page can by specified using the Limit
	 *            parameter.
	 * @return
	 * @throws IOException
	 */
	public String getLinks(String objectURL, String scope, String filters,
			String sort, long colSource, long colTarget, long colLink,
			int offset, int limit) throws IOException, Exception{
		
		log.info("Entering getLinks(...)");
		
		String response= "";
		// TODO: replace depreciated method -
		// http://stackoverflow.com/questions/213506/java-net-urlencoder-encodestring-is-deprecated-what-should-i-use-instead
		String urlToFetch = "http://lsapi.seomoz.com/linkscape/links/"
				+ URLEncoder.encode(objectURL, "UTF-8") + "?"
				+ authenticator.getAuthenticationStr();
		
		//29-7-13: getting issues with the encoded URL, not getting back links....
		//alternative:
//		String urlToFetch = "http://lsapi.seomoz.com/linkscape/links/"
//				+ objectURL + "?"
//				+ authenticator.getAuthenticationStr();

		if (scope != null) {
			urlToFetch = urlToFetch + "&Scope=" + scope;
		}
		if (filters != null) {
			urlToFetch = urlToFetch + "&Filter=" + filters;
		}
		if (sort != null) {
			urlToFetch = urlToFetch + "&Sort=" + sort;
		}
		if (colSource > 0) {
			urlToFetch = urlToFetch + "&SourceCols=" + colSource;
		}
		if (offset >= 0) {
			urlToFetch = urlToFetch + "&Offset=" + offset;
		}
		if (limit >= 0) {
			urlToFetch = urlToFetch + "&Limit=" + limit;
		}
		if (colLink > 0) {
			urlToFetch = urlToFetch + "&LinkCols=" + colLink;

		}
		if (colTarget > 0) {
			urlToFetch = urlToFetch + "&TargetCols=" + colTarget;
		}

		log.info("urlToFetch: " + urlToFetch);
		
		try{
		
		response = ConnectionUtil.makeRequest(urlToFetch);
		}catch(Exception e){
			log.severe(e.getMessage());
			// send up stack so that we will NOT set "backlinks_got = true"
			throw(e);
		}
		
		if(null == response){
			throw (new Exception("null response from: " + urlToFetch));
		}
		
		//these JSON assistant methods - were causing more exceptions than they were worth..
		
//		if(!HttpResponseReader.successfulResponse(response)){
//			
//			log.info("Call unsuccessful");
//			
//			String errMsg = JsonWrapper.getJsonString(response, "error_message");
//			Exception exp = new Exception(errMsg);
//			
//			throw (exp);
//		}	

		return response;
	}

	/**
	 * @param authenticator
	 *            the authenticator to set
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
