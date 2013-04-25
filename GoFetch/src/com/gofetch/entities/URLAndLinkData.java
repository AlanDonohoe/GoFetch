package com.gofetch.entities;

/*
 * Represents the join of a call to the links table and url table,
 * for representing backlink URLs with associated link data: eg: anchor text, date, etc
 */
public class URLAndLinkData {
	
	public URLAndLinkData(){
		
	}
	
	private URL url;
	private Link link;
	
	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
	public Link getLink() {
		return link;
	}
	public void setLink(Link link) {
		this.link = link;
	}
	
	

}
