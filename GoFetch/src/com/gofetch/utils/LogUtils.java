package com.gofetch.utils;


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

import com.gofetch.entities.URL;

public class LogUtils {
	
	private static Logger log = Logger.getLogger(LogUtils.class
			.getName());
	
	/**
	 * 
	 * @param req
	 * @return List of URL IDs as integers - stripped from the name value params of the request param
	 */
	public static List<Integer> getURLIDs(HttpServletRequest req){
		
		List<Integer> urlIDs = new ArrayList<Integer>();
		
		String paramName, paramValue, urlParameters = "";

		Enumeration<String> paramNames = req.getParameterNames();
		while (paramNames.hasMoreElements()) {

			// check that we're not at the first param, if not - then need to
			// add & to the string
			if (!urlParameters.isEmpty())
				urlParameters += "&";

			paramName = (String) paramNames.nextElement();

			paramValue = req.getParameter(paramName);
				
			urlIDs.add(Integer.parseInt(paramValue));
		}

		return urlIDs;
	}

	/**
	 * 
	 * @param req - HttpServletRequest object with 0-* params & values.
	 * @return string of params and their values
	 */
	public static String listParams(HttpServletRequest req) {
		
		log.info("In listParams");
		
		String paramName, paramValue, urlParameters = "";

		// //////////////
		// Log params & values
		Enumeration<String> paramNames = req.getParameterNames();
		while (paramNames.hasMoreElements()) {

			// check that we're not at the first param, if not - then need to
			// add & to the string
			if (!urlParameters.isEmpty())
				urlParameters += "&";

			paramName = (String) paramNames.nextElement();
			urlParameters += paramName;
			urlParameters += "=";

			paramValue = req.getParameter(paramName);
			urlParameters += paramValue;
		}

		return urlParameters;
	}

}