package com.gofetch.controllers;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gofetch.entities.URL;
import com.gofetch.entities.URLDBService;
import com.gofetch.seomoz.SEOMoz;
import com.gofetch.seomoz.SEOMozImplFreeAPI;
import com.gofetch.seomoz.URLPlusDataPoints;

/**
 * weekly checks for backlinks and social data of marked urls of interest
 * @author alandonohoe
 *
 */
public class PremierLeagueCrawl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger(PremierLeagueCrawl.class
			.getName());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		URLDBService urlDBUnit = new URLDBService();
		SEOMoz seoMoz = new SEOMozImplFreeAPI();	
		
		//1. get all target URLs in DB to get backlink and social data for...
		
		//TODO: replace with REAL code that selects target urls which have been selected to be monitored as per prem league...
		//urlDBUnit.getPremierLeagueURLs();
		List<URL> urls = urlDBUnit.getUnproccessedTargetURLs(100);
		List<URLPlusDataPoints> links;
		
		// 2: run though each url and get its backLink data...
		
		for(URL url: urls){
			
			try {
				links = seoMoz.getLinkFromUniqueDomains(url.getUrl_address());
			} catch (Exception e) {
				log.severe(e.getMessage());
			}
			
		}
		
		// 3. get social data....
		
		
		// 4. get image - if not already got.. if its a new target url...
		
		
		
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doGet(req, resp);
	}

	
}
