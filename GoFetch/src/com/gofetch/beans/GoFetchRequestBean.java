package com.gofetch.beans;

public class GoFetchRequestBean {
	protected String url;
	protected String user_id;
	protected boolean socialData;
	protected boolean backLinkData;
	protected int noOfLayers;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public boolean isSocialData() {
		return socialData;
	}
	public void setSocialData(boolean socialData) {
		this.socialData = socialData;
	}
	public boolean isBackLinkData() {
		return backLinkData;
	}
	public void setBackLinkData(boolean backLinkData) {
		this.backLinkData = backLinkData;
	}
	public int getNoOfLayers() {
		return noOfLayers;
	}
	public void setNoOfLayers(int noOfLayers) {
		this.noOfLayers = noOfLayers;
	}
	
	
}
