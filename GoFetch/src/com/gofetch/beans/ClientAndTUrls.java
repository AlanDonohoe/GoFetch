package com.gofetch.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gofetch.entities.User;

public class ClientAndTUrls implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	User user; // a client
	List<URLAndBoolSelection> urls; // and client's associated target URLs
	boolean selected; // if user selects the client and thus all client's target urls
						// if this is selected true - then cascade down to all client's t-urls
	
	String id; // used by client side JS script to hide & show panel with this data in.
	
	static int instanceCounter = 0;
	
	public ClientAndTUrls(){
		urls = new ArrayList<URLAndBoolSelection>();
		instanceCounter++;
		id = Integer.toString(instanceCounter);
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<URLAndBoolSelection> getUrls() {
		return urls;
	}
	public void setUrls(List<URLAndBoolSelection> urls) {
		this.urls = urls;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setAllUrlsSelection(boolean selected)
	{
		for(URLAndBoolSelection url: urls){
			
			url.setSelected(selected);
		}	
	}
	
}
