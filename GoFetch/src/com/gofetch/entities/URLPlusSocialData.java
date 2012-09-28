package com.gofetch.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a URL and a list of its (historical) social data.
 * @author alandonohoe
 *
 */
public class URLPlusSocialData {
	
	private URL url;
	private List<MiscSocialData> socialDataList = null;

	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
	public List<MiscSocialData> getSocialDataList() {
		if(null == socialDataList)
			socialDataList = new ArrayList<MiscSocialData>();
		return socialDataList;
	}
	public void setSocialDataList(List<MiscSocialData> socialDataList) {
		this.socialDataList = socialDataList;
	}
	
	@Override
    public String toString() {
	 
		String stringData =  url.toString() + ", \"SocialData\":" + socialDataList.toString();
		
		return stringData;
	 
 }
	
}
