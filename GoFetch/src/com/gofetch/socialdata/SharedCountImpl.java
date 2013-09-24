package com.gofetch.socialdata;

//import java.net.ConnectException;
import java.util.List;
//import java.util.logging.Level;
import java.util.logging.Logger;

//import com.gofetch.controllers.ProcessNewTargets;
import com.gofetch.entities.*;
import com.gofetch.utils.ConnectionUtil;
//import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;

/*
 * 1 implementation of the social data interface.
 * uses http://api.sharedcount.com/?url=https://www.bbc.....
 * this call returns a json representation of following data:
 * StumbleUpon":1,"Reddit":0,"Facebook":{"commentsbox_count":0,"click_count":0,"total_count":0,"comment_count":0,"like_count":0,"share_count":0},"Delicious":0,"GooglePlusOne":0,"Buzz":0,"Twitter":0,"Diggs":0,"Pinterest":0,"LinkedIn":467}
 * 
 */
public class SharedCountImpl implements SocialData{

	private static Logger log = Logger.getLogger(SharedCountImpl.class.getName());

	static final String SHARED_COUNT_URL = "http://api.sharedcount.com/?url=";

	/**
	 * uses sharedcount, which requires calling its API twice - once with a trailing slash and once without.
	 * if any of the returned fields are zero in 1 call and have a value in the other then will need to assimilate
	 * the results of the two calls  
	 * - replacing any zero results from the "failed call" with the "proper" result from the successful call
	 * @throws Exception 
	 * returns MiscSocialData object with all relevant/possible fields filled
	 * 
	 */
	//TODO: tidy this whole method up when its been tested....
	public MiscSocialData getAllSocialData(String url) throws Exception {

		MiscSocialData socialData = null;
		String response = null; 
		String sharedCountURL = SHARED_COUNT_URL + url;
	
		try{

			response = ConnectionUtil.get(sharedCountURL);

		} catch (Exception e){
			String msg = "Exception thrown... target url = " + url + " SharedCountImpl: getAllSocialData";
	
			log.warning(msg);
			throw (e);
		} 

		if(null == response){
			return null;
		}

		if(!response.contains("Twitter")){
			return null;
		}

		socialData = getSharedCountDataFromResponse(response); 
		
		return socialData;

	}

	/**
	 * Takes a string and returns SharedCount object with the data filled from stringData
	 * see: http://code.google.com/p/json-path/
	 * 
	 * @param stringData - typically http response that contains JSON formatted data from sharedcount.com
	 * @return
	 */
	private MiscSocialData getSharedCountDataFromResponse(String stringData){

		MiscSocialData miscSocialData = new MiscSocialData();

		int delicious = JsonPath.read(stringData,"$.Delicious");
		int googlePlusOne = JsonPath.read(stringData,"$.GooglePlusOne");
		int twitter = JsonPath.read(stringData,"$.Twitter");
		int pinterest = JsonPath.read(stringData,"$.Pinterest");
		int linkedIn = JsonPath.read(stringData,"$.LinkedIn");
		int stumbleUpon = JsonPath.read(stringData,"$.StumbleUpon");

		int commentsbox_count = JsonPath.read(stringData,"$.Facebook.commentsbox_count");
		int click_count = JsonPath.read(stringData,"$.Facebook.click_count");
		int total_count = JsonPath.read(stringData,"$.Facebook.total_count");
		int comment_count = JsonPath.read(stringData,"$.Facebook.comment_count");
		int like_count = JsonPath.read(stringData,"$.Facebook.like_count");
		int share_count = JsonPath.read(stringData,"$.Facebook.share_count");

		miscSocialData.setFb_click_Count(click_count);
		miscSocialData.setFb_comment_Count(comment_count);
		miscSocialData.setFb_commentsbox_count(commentsbox_count);
		miscSocialData.setFb_like_Count(like_count);
		miscSocialData.setFb_share_Count(share_count);
		miscSocialData.setFb_total_Count(total_count);

		miscSocialData.setDelicious(delicious);
		miscSocialData.setGoogle_plus_one(googlePlusOne);
		miscSocialData.setLinkedin(linkedIn);
		miscSocialData.setPinterest(pinterest);
		miscSocialData.setTwitter(twitter);
		miscSocialData.setStumble_upon(stumbleUpon);

		return miscSocialData;

	}

	/**
	 * will use Dom's topsy and Klout api's to get this data....
	 */
	public List<TwitterMention> getTwitterMentions(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getStumbleUponData(String url) {
		// TODO Auto-generated method stub
		return null;
	}

}
