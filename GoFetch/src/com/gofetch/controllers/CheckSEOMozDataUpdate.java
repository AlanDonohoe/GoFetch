package com.gofetch.controllers;

import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
/**
 * 
 * @author alandonohoe
 * Using a cron timer, this will check all the target urls and links in the database to see if there's any change in status 
 * on the monthly update of the SEOMOz data.
 * 
 * 1) How to check if the backlink profile is exactly the same for the target?
 * 2) if its different, there may be the same no of backlinks, but the links themselves may of changed
 * 3) need to check that every backlink from last session is still present on SEOMoz 
 * 4) if not - then need to set date_expired in links table to todays date.
 * 5) if still present - then do nothing to url or link table
 * 6) if new backlink - then need to add to url table and add new link to link table.
 */

public class CheckSEOMozDataUpdate extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(CheckSEOMozDataUpdate.class.getName());


}
