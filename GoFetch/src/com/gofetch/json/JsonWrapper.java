package com.gofetch.json;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.gofetch.seomoz.URLPlusDataPoints;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * 
 * @author alandonohoe
 * Wraps common JSON requirements for ease of use
 */
public class JsonWrapper {
	
	private static Logger log = Logger.getLogger(JsonWrapper.class
			.getName());

	/**
	 * 
	 * @param jsonString - 
	 * @param key - key, whose value will be returned...
	 * @return int value of corresponding key
	 */
	static public Integer getJsonInt(String jsonString, String key){

		log.info("getJsonInt(...). jsonString: " + jsonString );
		//remove leading and trailing WS
		JSONObject jsonObj = new JSONObject(jsonString.trim());

		return(jsonObj.getInt(key));
	}

	/**
	 * 
	 * @param jsonString
	 * @param key : key, whose value will be returned...
	 * @return string value of corresponding key
	 */
	static public String getJsonString(String jsonString, String key){

		log.info("getJsonString(...). jsonString: " + jsonString );
		//remove leading and trailing WS
		JSONObject jsonObj = new JSONObject(jsonString.trim());

		return(jsonObj.getString(key));


	}

	//TODO: make this return generic/parametrized values
	// see: http://stackoverflow.com/questions/450807/java-generics-how-do-i-make-the-method-return-type-generic

	//This method solves this problem: http://stackoverflow.com/questions/9598707/gson-throwing-expected-begin-object-but-was-begin-array
	/**
	 * 
	 * @param jstring Json string that contains
	 * @return
	 */
	static public ArrayList<URLPlusDataPoints> getObjectListFromJson(String jstring){

		JsonParser parser = new JsonParser();
		JsonArray Jarray = parser.parse(jstring).getAsJsonArray();
		Gson gson = new Gson();

		ArrayList<URLPlusDataPoints> lcs = new ArrayList<URLPlusDataPoints>();

		for(JsonElement obj : Jarray )
		{
			URLPlusDataPoints cse = gson.fromJson( obj , URLPlusDataPoints.class);
			lcs.add(cse);
		}
		
		return lcs;

	}

}
