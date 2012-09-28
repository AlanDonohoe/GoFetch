package com.gofetch.seomoz;

import java.util.List;

/*
 * 
 */
public interface SEOMoz {

	// returns list of all SEOMoz provided links pointing to the target.
	public List<URLPlusDataPoints> getLinks(String urlTarget) throws Exception;
	
	// returns list of unique domains pointing to the target.
	//public List<URLPlusDataPoints> getUniqueDomains(String urlTarget);
	
	// returns an equivalent URLPlusDataPoints object as "url" parameter, with Domain & Page Authority data, domain name and document title
	public URLPlusDataPoints getURLMetricsData(URLPlusDataPoints url);
	
	// fills the list parameter with page & domain authority and document title - for each url.
	public List<URLPlusDataPoints> getAuthorityData(List<URLPlusDataPoints> urls);
	
}
