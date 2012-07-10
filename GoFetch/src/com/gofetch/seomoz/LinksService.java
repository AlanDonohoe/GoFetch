package com.gofetch.seomoz;

import java.net.URLEncoder;

import com.gofetch.seomoz.Authenticator;
import com.gofetch.seomoz.ConnectionUtil;

/**
 * 
 * Service class to call the various methods to 
 * Links API 
 * 
 * Links api returns a set of links to a page or domain.
 * 
 * @author Radeep Solutions
 *
 */
public class LinksService 
{
	private Authenticator authenticator;
	
	public LinksService()
	{
		
	}
	
	/**
	 * 
	 * @param authenticator
	 */
	public LinksService(Authenticator authenticator)
	{
		this.setAuthenticator(authenticator);
	}
	
	
	/**
	 * This method returns a set of links to a page or domain.
	 * 
	 * @param objectURL
	 * @param scope determines the scope of the Target link, as well as the Source results.
	 * @param filters  filters the links returned to only include links of the specified type.  You may include one or more of the following values separated by '+'
	 * @param sort determines the sorting of the links, in combination with limit and offset, this allows fast access to the top links by several orders:
	 * @param colSource specifies data about the source of the link is included
         * @param colLink  specifies data about the backlink to be included..... Added AD 17-4-12
	 * @param offset The start record of the page can be specified using the Offset parameter
	 * @param limit The size of the page can by specified using the Limit parameter.
	 * @return
	 */
	public String getLinks(String objectURL, String scope, String filters, String sort, long colSource, long colTarget, long colLink,int offset, int limit)
	{
		//TODO: replace depreciated method - http://stackoverflow.com/questions/213506/java-net-urlencoder-encodestring-is-deprecated-what-should-i-use-instead
		String urlToFetch = "http://lsapi.seomoz.com/linkscape/links/" + URLEncoder.encode(objectURL) + "?" + authenticator.getAuthenticationStr();
		
                
		if(scope != null)
		{
			urlToFetch = urlToFetch + "&Scope=" + scope;
		}
		if(filters != null)
		{
			urlToFetch = urlToFetch + "&Filter=" + filters;
		}
		if(sort != null)
		{
			urlToFetch = urlToFetch + "&Sort=" + sort;
		}
		if(colSource > 0)
		{
			urlToFetch = urlToFetch + "&SourceCols=" + colSource;
		}
		if(offset >= 0)
		{
			urlToFetch = urlToFetch + "&Offset=" + offset;
		}
		if(limit >= 0)
		{
			urlToFetch = urlToFetch + "&Limit=" + limit;
		}
                if(colLink > 0){
                        urlToFetch = urlToFetch + "&LinkCols=" + colLink;
                    
                }
                if(colTarget > 0){
                        urlToFetch = urlToFetch + "&TargetCols=" + colTarget;
                }

                String response = ConnectionUtil.makeRequest(urlToFetch);
		
		return response;
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
