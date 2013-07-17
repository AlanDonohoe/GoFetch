package com.gofetch.controllers;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EnterLinkFromBrowser extends HttpServlet{

	/**
	 * Class accepts series of name value pairs that specify a hyperlink (anchor text, source & target URL, etc)
	 * parses and validates them, then prepopulates the "add link" form with these values and sends form back to 
	 * browser.
	 * 
	 * This allows the addition of links to GoFetch DB via a chrome plug in.
	 * 
	 * 
	 * ex: http://gofetchdata.appspot.com/enterlinkfrombrowser?sourceURL=http://www.berlin-mitte-institut.de/web-tv/&targetURL=http://bank.marksandspencer.com/banking/internet-banking/overview/
	 * ex: http://localhost:8888/enterlinkfrombrowser?sourceURL=http://www.berlin-mitte-institut.de/web-tv/&targetURL=http://bank.marksandspencer.com/banking/internet-banking/overview/
	 * 	 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger(EnterLinkFromBrowser.class
			.getName());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		
		String sourceURL, anchorText, targetURL, client, campaign, date;

		
		sourceURL = req.getParameter("source");
		targetURL = req.getParameter("target");
		anchorText = req.getParameter("anchor");
		
		
		//TODO: work this out from the target URL - client = req.getParameter("client");
		// user enters this: campaign = req.getParameter("campaign");
		// get todays - date = req.getParameter("date");
		
		
		
		//TODO: some validation the params...
		
		
		//TODO: fill the bean backing the link entry form with the params and return the form to the browser
		
		
		try {
			getServletContext().getRequestDispatcher("/enterlink.jsf").forward(
					req, resp);
		} catch (ServletException e) {
		
	
	}
	}

}
