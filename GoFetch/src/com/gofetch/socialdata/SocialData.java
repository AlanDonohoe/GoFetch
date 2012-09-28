package com.gofetch.socialdata;

import java.util.List;

import com.gofetch.entities.*;

	/**
	 * 
	 * @author alandonohoe
	 * represents interface that, when implemented, queries various API's to retrieve social data for given url(s).
	 */
public interface SocialData {
	
	// returns list of twitter mentions (tweet text, tweeter id and tweeter's klout score) for all tweets mentioning url
	public List<TwitterMention> getTwitterMentions(String url);
	
	// returns a list of all social data for target url
	public MiscSocialData getAllSocialData(String url) throws Exception;
	
	public Integer getStumbleUponData(String url);

}
