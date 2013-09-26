package com.gofetch.seomoz;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.logging.Logger;

import com.gofetch.email.AdminEmailHelper;
import com.gofetch.json.JsonWrapper;
import com.gofetch.seomoz.Authenticator;
import com.gofetch.utils.ConnectionUtil;
import com.gofetch.utils.HttpResponseReader;
import com.google.api.server.spi.Constant;

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

		log.info("Entering getLinks");
		int counter = 0;
		boolean success = false;
		String errorMessage = "";

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

		// repeat until we have got some response or exceeded no of retries
		while(!success && (counter++ < Constants.HTTP_RETRIES )){
			log.info("Try no: " + counter);
			
			//Sleep: so to stay within free quota limits 
			try {
				Thread.sleep(Constants.FREE_API_SEOMOZ_SERVER_DELAY);
			} catch (InterruptedException e) {
				// from: http://stackoverflow.com/questions/9139128/a-sleeping-thread-is-getting-interrupted-causing-loss-of-connection-to-db
				Thread.currentThread().interrupt(); // restore interrupted status
			}

			try{
				response = ConnectionUtil.get(urlToFetch);
				success = true;
			}catch(Exception e){
				errorMessage = e.getMessage();
				log.severe(errorMessage);
				success = false;
			}

			if(null == response){
				errorMessage = "response == null";
				log.warning(errorMessage);
				success = false;
			}

			if(response.isEmpty()){
				errorMessage = "response.isEmpty";
				log.warning(errorMessage);
				success = false;
			}
		}
		//finally, if the request has failed Constants.HTTP_RETRIES times...
		if(false == success){
			AdminEmailHelper emailHelper = new AdminEmailHelper();
			try {
				emailHelper.sendWarningEmailToAdministrator("In: " + LinksService.class
						.getName() + "\n After " +Constants.HTTP_RETRIES + " attempts. Failed to get: " + urlToFetch + " "+ errorMessage);
			} catch (Exception emailEx) {}
		}
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
