package com.gofetch.charts;

import java.io.Serializable;
import java.util.List;

import net.minidev.json.JSONArray;

import com.gofetch.entities.Link;
import com.gofetch.entities.MiscSocialData;
import com.gofetch.entities.URL;
import com.jayway.jsonpath.JsonPath;

public class GoogleChartsWrapper implements Serializable{

	private static final long serialVersionUID = -1609347431677931581L;

	public GoogleChartsWrapper(){
		
		socialDataString   = "";
		backLinkDataString = "";
			
	}
	
	String chartTitle;
	String socialDataString;
	String backLinkDataString;
	
	public String getChartTitle() {
		return chartTitle;
	}
	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
	}
	
	public String getSocialDataString() {
		return socialDataString;
	}
	public void setSocialDataString(String socialDataString) {
		this.socialDataString = socialDataString;
	}

	public String getBackLinkDataString() {
		return backLinkDataString;
	}
	public void setBackLinkDataString(String backLinkDataString) {
		this.backLinkDataString = backLinkDataString;
	}
	
	/**
	 * Accepts arrayList of miscSocialData, parses it and saves it Google Chart friendly format - as this object's chartDataString
	 */
	public String parseSocialDataToString(List<MiscSocialData> socialData){
		
		String socialDataString = socialData.toString();
		String stringParsed, jsonArrayString;
		Integer jsonArrayInteger;
		
		if(socialData.isEmpty()){
			
			stringParsed = "[['Date', 'FB (Total)', 'Twitter', 'LinkedIn', 'GooglePlus', 'PInterest', 'Stumbles', 'Delicious']," +
					"			[0,0,0,0,0,0,0,0]]";
			
			
			return stringParsed;
		}
		
		

 		// correct the format for JsonPath parsing:
		socialDataString = socialDataString.replace("[{", "{ \"socialdata\": { \"data\": [{");
		socialDataString = socialDataString.replace("}]", "}]}}");
		
		///////
		// JSON parsing:
		
		//Get all the dates:
		JSONArray jsonDateArray = JsonPath.read(socialDataString, "$.socialdata.data[*].Date");
		
		// facebook data:
		JSONArray jsonFBArray = JsonPath.read(socialDataString, "$.socialdata.data[*].FaceBookTotal");
		
		//"Twitter": 
		JSONArray jsonTwitterArray = JsonPath.read(socialDataString, "$.socialdata.data[*].Twitter");
		
		//"LinkedIn" 
		JSONArray jsonLinkedInArray = JsonPath.read(socialDataString, "$.socialdata.data[*].LinkedIn");
				
		// stumble data: 
		JSONArray jsonStumbleArray = JsonPath.read(socialDataString, "$.socialdata.data[*].StumbleUpon");
		
		// "Delicious"
		JSONArray jsonDeliciousArray = JsonPath.read(socialDataString, "$.socialdata.data[*].Delicious");
		
		// "GooglePlus"
		JSONArray jsonGooglePlusArray = JsonPath.read(socialDataString, "$.socialdata.data[*].GooglePlus");
		
		// "Pinterest" 
		JSONArray jsonPinterestArray = JsonPath.read(socialDataString, "$.socialdata.data[*].Pinterest");

		//
		//////////
			
		// set the main data elements
		stringParsed = "[['Date', 'FB (Total)', 'Twitter', 'LinkedIn', 'GooglePlus', 'PInterest', 'Stumbles', 'Delicious'], ";
		
		for (int i = (socialData.size() -1); i >= 0; i--){
			
			// beginning of each new array entry - start with [
			stringParsed += "[";
			
			// get each date - starting with the earliest
			jsonArrayString = "\'";
			jsonArrayString += (String) jsonDateArray.get(i);
			jsonArrayString += "\',";
			
			stringParsed += jsonArrayString;
			
			// get FB
			jsonArrayInteger = (Integer)jsonFBArray.get(i);
			jsonArrayString = Integer.toString(jsonArrayInteger);
			jsonArrayString += ",";
			
			stringParsed += jsonArrayString;
			
			//"Twitter": 
			jsonArrayInteger = (Integer)jsonTwitterArray.get(i);
			jsonArrayString = Integer.toString(jsonArrayInteger);
			jsonArrayString += ",";
			
			stringParsed += jsonArrayString;
			
			//"LinkedIn" 
			jsonArrayInteger = (Integer)jsonLinkedInArray.get(i);
			jsonArrayString = Integer.toString(jsonArrayInteger);
			jsonArrayString += ",";
			
			stringParsed += jsonArrayString;
			
			// "GooglePlus"
			jsonArrayInteger = (Integer) jsonGooglePlusArray.get(i);
			jsonArrayString = Integer.toString(jsonArrayInteger);
			jsonArrayString += ",";
			
			stringParsed += jsonArrayString;
					
			// "Pinterest" 
			jsonArrayInteger = (Integer) jsonPinterestArray.get(i);
			jsonArrayString = Integer.toString(jsonArrayInteger);
			jsonArrayString += ",";
			
			stringParsed += jsonArrayString;
					
			// stumble data: 
			jsonArrayInteger = (Integer) jsonStumbleArray.get(i);
			jsonArrayString = Integer.toString(jsonArrayInteger);
			jsonArrayString += ",";
			
			stringParsed += jsonArrayString;
			
			// "Delicious"
			jsonArrayInteger = (Integer) jsonDeliciousArray.get(i);
			jsonArrayString = Integer.toString(jsonArrayInteger);
			//jsonArrayString += ",";
			
			stringParsed += jsonArrayString;

			
			// end of each new row entry of array - end with : "]," 
			stringParsed += "],";
						 	
		}
		
		// and if final, remove final comma
		stringParsed = stringParsed.substring(0, stringParsed.length() -1);
		
		// ... and add final closing bracket
		stringParsed += "]";

		return stringParsed;
	}
	
	public String parseBackLinkData(List<MiscSocialData> socialData){
		
		return "";
	}
	
	
	public String parseBackLinkData(String targetURLAddress, List<Link> links,
			List<URL> sourceURLs) {

		return "";
		
	}

}
