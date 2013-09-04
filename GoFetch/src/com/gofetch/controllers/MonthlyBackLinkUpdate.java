package com.gofetch.controllers;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gofetch.entities.URLDBService;
/**
 * 
 * @author alandonohoe
 * This is called on the first day of the month and simply
 * sets all URLs who have had their get_backlinks = true,
 * sets their set backlinks_got = false.
 * These are then pulled by ProcessNewTargets to check for new backlinks
 */
public class MonthlyBackLinkUpdate extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger log =  Logger.getLogger(MonthlyBackLinkUpdate.class
			.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		log.info("Entering MonthlyBackLinkUpdate::doGet()");
		
		URLDBService urlDBUnit = new URLDBService();
		
		urlDBUnit.updateTURLsGetBacklinks();
		
		
	}
	
	

}
