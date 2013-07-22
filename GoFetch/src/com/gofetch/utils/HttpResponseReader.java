package com.gofetch.utils;

import java.util.logging.Logger;
import com.gofetch.json.*;

/**
 * 
 * @author alandonohoe
 * Parses various responses, json, etc - and returns info on them
 */
public class HttpResponseReader {
	
	private static Logger log = Logger.getLogger(HttpResponseReader.class
			.getName());
	
	/**
	 * 
	 * @param jsonResponse 
	 * @return true if 'status' param value = 200. else returns false.
	 */
	static public boolean successfulResponse(String jsonResponse){
		
		log.info("Entering successfulResponse(...)");
		int respStatus = JsonWrapper.getJsonInt(jsonResponse, "status");
		
		if(200 != respStatus)
			return false;
		else
			return true;
	}
	
}
