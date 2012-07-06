package com.gofetch;

public class GoFetchRequestBean {
	protected String url;
	protected boolean facebookData;
	protected boolean twitterData;
	protected boolean backLinkData;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isFacebookData() {
		return facebookData;
	}
	public void setFacebookData(boolean facebookData) {
		this.facebookData = facebookData;
	}
	public boolean isTwitterData() {
		return twitterData;
	}
	public void setTwitterData(boolean twitterData) {
		this.twitterData = twitterData;
	}
	public boolean isBackLinkData() {
		return backLinkData;
	}
	public void setBackLinkData(boolean backLinkData) {
		this.backLinkData = backLinkData;
	}
	
	
	
}
