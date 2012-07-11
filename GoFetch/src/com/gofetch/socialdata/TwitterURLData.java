package com.gofetch.socialdata;

/**
 * 
 * @author alandonohoe
 *
 * Class represents a mention of a URL on twitter.
 * 
 * date is assigned today's date when object is constructed.
 */

public class TwitterURLData {
	
	private java.sql.Date date;
	private String tweeterID;
	private String tweetText;
	private int followers;
	private int follows;
	private int kloutScore;
	

	public TwitterURLData(){
		
		java.util.Date dateJava = new java.util.Date();
		java.sql.Date dateSQL = new java.sql.Date(dateJava.getTime());
		
		date = dateSQL;
	}


	public String getTweeterID() {
		return tweeterID;
	}


	public void setTweeterID(String tweeterID) {
		this.tweeterID = tweeterID;
	}


	public String getTweetText() {
		return tweetText;
	}


	public void setTweetText(String tweetText) {
		this.tweetText = tweetText;
	}


	public int getFollowers() {
		return followers;
	}


	public void setFollowers(int followers) {
		this.followers = followers;
	}


	public int getFollows() {
		return follows;
	}


	public void setFollows(int follows) {
		this.follows = follows;
	}


	public int getKloutScore() {
		return kloutScore;
	}


	public void setKloutScore(int kloutScore) {
		this.kloutScore = kloutScore;
	}


	public java.sql.Date getDate() {
		return date;
	}
	
}
