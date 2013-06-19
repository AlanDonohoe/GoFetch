package com.gofetch.entities;

import java.io.Serializable;

import com.gofetch.utils.DateUtil;
import com.gofetch.utils.TextUtil;

/*
 * Represents the join of a call to the links table and url table,
 * for representing backlink URLs with associated link data: eg: anchor text, date, etc
 */
public class URLAndLinkData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private URL url; // backlink URL
	private Link link; // link details - anchor text, campaign, etc

	// used for displaying the url address - stripping it of any trailing junk text
	private String tidyURLAddress;
	private String tidyTargetURLAddress;
	private String linkBuildingActivity;
	
	//old
	//private Integer targetURLId;
	
	//new:
	private URL targetURL;

	public URLAndLinkData() {

		url = new URL();
		link = new Link();

	}

	public String getTidyURLAddress() {
		
		//New:
		
		//if we have already parsed the unTidy url address  just return it.
		if(null!= tidyURLAddress)
			return tidyURLAddress;
		
		tidyURLAddress = TextUtil.tidyURLAddress(url.getUrl_address());
		
		return tidyURLAddress;
		
		
		
		//OLD:
		
//		// ex. of issue url address: http://www.marksandspencer.com/Maternity-Bras-Guide-Lingerie-Underwear-Womens/b/908657031?ie=UTF8&ie=UTF8?ie=UTF8&pf_rd_r=1R8EMRGQ20MJB1FAM6ZT&pf_rd_m=A2BO0OYVBKIQJM&pf_rd_t=101&pf_rd_i=1323471031&pf_rd_p=475115433&pf_rd_s=left-nav-2
//		String parsedAddress;
//		Integer secondDot, firstQuestionMark;
//		
//		//if we have already parsed the unTidy url address  just return it.
//		if(null!= tidyURLAddress)
//			return tidyURLAddress;
//		
//		//else tidy up messy original url address.
//		parsedAddress = url.getUrl_address();
//		
//		// remove http:
//		parsedAddress = parsedAddress.substring(7);
//		secondDot = parsedAddress.indexOf('.', 8);// find the dot after the first dot of http://www.
//		firstQuestionMark = parsedAddress.indexOf('?', secondDot++);
//		
//		if(firstQuestionMark < 0) // if there's no trailing arguments...
//			return parsedAddress; // just return
//		
//		tidyURLAddress = parsedAddress.substring(0, firstQuestionMark--);
//		
//		return tidyURLAddress;
	}

	/**
	 * 
	 * @param tidyURLAddress - this param ignored. as this field: tidyURLAddress is just used for display purposes.
	 * and so this function just sets the tidyURLAddress as the URL address after its been parsed by getTidyURLAddress()...
	 */
	public void setTidyURLAddress(String tidyURLAddress) {
		this.tidyURLAddress = getTidyURLAddress();
	}
	
	public void setTidyDate(String date){
		//na - just used to get tidy /formatted date for datatable... 
	}
	
	public String getTidyDate(){
		return(DateUtil.getFormattedDate(url.getDate()));
	}

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



	//OLD:
//	public Integer getTargetURLId() {
//		return targetURLId;
//	}
//
//	public void setTargetURLId(Integer targetURLId) {
//		this.targetURLId = targetURLId;
//	}
	
	////////
	//New:
	public URL getTargetURL() {
		return targetURL;
	}
	/////
	
	public void setTargetURL(URL targetURL) {
		this.targetURL = targetURL;
	}

	public String getTidyTargetURLAddress() {
		
		//if we have already parsed the unTidy url address  just return it.
		if(null!= tidyTargetURLAddress)
			return tidyTargetURLAddress;
		
		tidyTargetURLAddress = TextUtil.tidyURLAddress(targetURL.getUrl_address());
		
		return tidyTargetURLAddress;
	}

	public void setTidyTargetURLAddress(String tidyTargetURLAddress) {
		this.tidyTargetURLAddress = tidyTargetURLAddress;
	}

	public String getLinkBuildingActivity() {
		return linkBuildingActivity;
	}

	public void setLinkBuildingActivity(String linkBuildingActivity) {
		this.linkBuildingActivity = linkBuildingActivity;
	}

	
}
