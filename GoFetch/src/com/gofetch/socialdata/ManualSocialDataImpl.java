package com.gofetch.socialdata;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minidev.json.JSONArray;

import com.gofetch.GoFetchConstants;
import com.gofetch.entities.MiscSocialData;
import com.gofetch.entities.SocialDataUnit;
import com.gofetch.entities.TwitterMention;
import com.gofetch.entities.FaceBookDataUnit;
import com.gofetch.utils.ConnectionUtil;
import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;

/**
 * Represents a manual implementation of the SocialData interface, where each of
 * the social metric's APIs/endpoints are called directly. This is the most
 * expensive way of getting the data, for ex: compared to single SharedCount API
 * call.
 * 
 * can be called to supplement when other less expensive calls fail.
 * 
 * @author alandonohoe
 * 
 */
public class ManualSocialDataImpl implements SocialData {

	private static Logger log = Logger.getLogger(ManualSocialDataImpl.class
			.getName());
	
	private boolean minimalLoging = true;


	@Override
	public List<TwitterMention> getTwitterMentions(String url) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Integer getStumbleUponData(String url){
		
		Integer stumbleViews;
		try {
			stumbleViews = getSingleSocialMetric(GoFetchConstants.stumbleUponEndPoint, url);
		} catch (IOException e) {
			if(!minimalLoging)
			{
			String errorMsg = "ManualSocialDataImp: Call to StumbleUpon failed to get data for: " + url;

			log.warning(errorMsg);
			}
			
			return null;
		}

		if(null == stumbleViews){
			
			if(!minimalLoging)
			{
			String errorMsg = "ManualSocialDataImp: Call to StumbleUpon failed to get data for: " + url;

			log.warning(errorMsg);
			}
			
			return null;

		}else{
			return stumbleViews;
		}
		
	}

	@Override
	public MiscSocialData getAllSocialData(String url) throws Exception {

		
		MiscSocialData miscSocialData = new MiscSocialData();	

		//////
		//  delicious
		Integer delicious = getSingleSocialMetric(GoFetchConstants.deliciousEndPoint, url);

		if(null == delicious){
			if(!minimalLoging)
			{
			String errorMsg = "ManualSocialDataImp: Call to Delicious failed to get data for: " + url;

			log.warning(errorMsg);
			}

		}else{
			miscSocialData.setDelicious(delicious);
		}	
		//
		/////////////

		// ///////
		// Facebook:
		FaceBookDataUnit fbdataUnit = null;

		fbdataUnit = getFaceBookData(url);

		if(null == fbdataUnit){
			
			String errorMsg = "ManualSocialDataImp: Call to facebook failed to get data for: " + url;

			log.warning(errorMsg);


		}else{
			// assign individual values to miscSocialData object
			miscSocialData.setFb_click_Count(fbdataUnit.getFb_click_Count());
			miscSocialData.setFb_comment_Count(fbdataUnit.getFb_comment_Count());
			miscSocialData.setFb_commentsbox_count(fbdataUnit.getFb_commentsbox_count());
			miscSocialData.setFb_like_Count(fbdataUnit.getFb_like_Count());
			miscSocialData.setFb_share_Count(fbdataUnit.getFb_share_Count());
			miscSocialData.setFb_total_Count(fbdataUnit.getFb_total_Count());
		}

		// end facebook
		// ///////////////


		////////////
		// google+ from: http://kahimyang.info/kauswagan/howto_blogs/1052-how_to_get_google__1_pluses_count_in_java

		Integer googlePlus = getGooglePlusFor(url);

		if(null == googlePlus){
			String errorMsg = "ManualSocialDataImp: Call to Google+ failed to get data for: " + url;

			log.warning(errorMsg);

		}else{
			miscSocialData.setGoogle_plus_one(googlePlus);
		}

		//
		////////////

		/////////
		// Linkedin:

		Integer linkedIn = getSingleSocialMetric(GoFetchConstants.linkedInPreEndPoint, url, GoFetchConstants.linkedInPostEndPoint);

		if(null == linkedIn){
			String errorMsg = "ManualSocialDataImp: Call to LinkedIn failed to get data for: " + url;

			log.warning(errorMsg);

		}else{
			miscSocialData.setLinkedin(linkedIn);
		}

		//
		/////////

		/////////
		// Pinterest:

		Integer pinterest = getSingleSocialMetric(GoFetchConstants.pinterestEndPoint, url);

		if(null == pinterest){
			if(!minimalLoging)
			{
			String errorMsg = "ManualSocialDataImp: Call to Pinterest failed to get data for: " + url;

			log.warning(errorMsg);
			}

		}else{
			miscSocialData.setPinterest(pinterest);
		}

		//
		/////////	

		//////
		// stumble upon
		Integer stumbleViews = getSingleSocialMetric(GoFetchConstants.stumbleUponEndPoint, url);

		if(null == stumbleViews){
			if(!minimalLoging)
			{
			String errorMsg = "ManualSocialDataImp: Call to StumbleUpon failed to get data for: " + url;

			log.warning(errorMsg);
			}

		}else{
			miscSocialData.setStumble_upon(stumbleViews);
		}

		//
		///////////

		//////////
		//  twitter:

		Integer twitterMentions = getSingleSocialMetric(GoFetchConstants.twitterPreEndPoint, url, GoFetchConstants.twitterPostEndPoint);

		if(null == twitterMentions){
			String errorMsg = "ManualSocialDataImp: Call to twitter failed to get data for: "
					+ url;
			log.warning(errorMsg);

		}else{
			miscSocialData.setTwitter(twitterMentions);
		}
		// end twitter
		////////


		return miscSocialData;
	} // end of main getSocialData method...


	/**
	 * 
	 * @param preEndPoint
	 * @param url
	 * @param postEndPoint
	 * @return first integer in the response to the call to preEndPoint + url + postEndPoint
	 * @throws IOException 
	 */
	public Integer getSingleSocialMetric(String preEndPoint, String url, String postEndPoint) throws IOException{

		String response = null;
		String request = preEndPoint + url + postEndPoint;
		Integer socialCount;

		try {
			response = ConnectionUtil.makeRequest(request);
		} catch (Exception e) {

			return null;

		}

		if(null == response)
			return null;

		// deal with specific delicious api issue...
		if(preEndPoint.contains("delicious.com")){
			if(response.length() < 3)
				return null;
			
			int i = response.indexOf("\"total_posts\":");

			if(i < 1)
				return null;
			else{
				response = response.substring(i);
			}
		}

		// deal with specific stumble_upon issue...
		if(preEndPoint.contains("stumbleupon.com")){

			int i = response.indexOf("\"views\":");

			if(i < 1)
				return null;
			else{
				response = response.substring(i);
			}

		} 
		
		//deal with specific pinterest issue:
		if(preEndPoint.contains("pinterest.com")){
			
			int countTextIndex = response.indexOf("\"count\":");
			
			int dashTextIndex = response.indexOf("\"-\"");
			
			int urlTextIndex = response.indexOf("\"url\"");
					
			if(countTextIndex < 1) // there's some serious problem  from Pinterest
				return null;
			else{
				// pinterest returns count: "-" if the url is not in it system.
				//	if its between the count and url text - then we have no entry.
				if((countTextIndex < dashTextIndex) && (dashTextIndex < urlTextIndex)){
					return null;
				}
				
			}
			
		}

		//get first integer out
		Pattern intsOnly = Pattern.compile("\\d+");
		Matcher makeMatch = intsOnly.matcher(response);
		makeMatch.find();
		String inputInt = makeMatch.group();
		socialCount = Integer.parseInt(inputInt);

		return socialCount;
	}

	/**
	 * overridden version of method, for when the social end point has no post fix 
	 * @param preEndPoint
	 * @param url
	 * @return
	 * @throws IOException 
	 */
	public Integer getSingleSocialMetric(String preEndPoint, String url) throws IOException{

		return(getSingleSocialMetric(preEndPoint, url, ""));
	}

	public FaceBookDataUnit getFaceBookData(String url) throws Exception {

		FaceBookDataUnit faceBookData = new FaceBookDataUnit();
		String response;
		JSONArray jsonArray; // = null;


		String faceBookEndPoint = "http://api.ak.facebook.com/restserver.php?v=1.0&method=links.getStats&urls="
				+ url + "&format=json";

		try {
			response = ConnectionUtil.makeRequest(faceBookEndPoint);
		} catch (Exception e) {

			return null;
		}

		jsonArray = JsonPath.read(response, "$..total_count");
		int total_count = (Integer) jsonArray.get(0);

		jsonArray = JsonPath.read(response,"$..commentsbox_count");
		int commentsbox_count = (Integer) jsonArray.get(0);

		jsonArray = JsonPath.read(response,"$..click_count");
		int click_count = (Integer) jsonArray.get(0);

		jsonArray = JsonPath.read(response,"$..comment_count");
		int comment_count = (Integer) jsonArray.get(0);

		jsonArray = JsonPath.read(response,"$..like_count");
		int like_count = (Integer) jsonArray.get(0);

		jsonArray = JsonPath.read(response,"$..share_count");
		int share_count = (Integer) jsonArray.get(0);	

		faceBookData.setFb_click_Count(click_count);
		faceBookData.setFb_comment_Count(comment_count);
		faceBookData.setFb_commentsbox_count(commentsbox_count);
		faceBookData.setFb_like_Count(like_count);
		faceBookData.setFb_share_Count(share_count);
		faceBookData.setFb_total_Count(total_count);

		return faceBookData;

	}

	public Integer getGooglePlusFor(String urlTarget){


		String postData = GoFetchConstants.googlePrePost + urlTarget + GoFetchConstants.googlePostPost;
		String response;
		String response2;
		Integer socialCount;

		try {

			response = ConnectionUtil.post(GoFetchConstants.googlePlusEndPoint, postData);


		} catch (Exception e) {
			return null;
		}

		if(null == response)
			return null;

		//TODO: test here - for 2nd call - it fails.... what is returned when count is not in teh string???
		int countIndex = response.indexOf("count");

		if(countIndex < 1)
			return null;

		response2 = response.substring(countIndex);

		//get first integer out after "count"
		Pattern intsOnly = Pattern.compile("\\d+");
		Matcher makeMatch = intsOnly.matcher(response2);
		makeMatch.find();
		String inputInt = makeMatch.group();
		socialCount = Integer.parseInt(inputInt);

		return socialCount;
	}
}
