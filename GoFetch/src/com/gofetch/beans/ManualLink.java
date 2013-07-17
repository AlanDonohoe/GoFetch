package com.gofetch.beans;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.gofetch.LinkReportConstants;
import com.gofetch.utils.DateUtil;

/**
 * Represents the interface between a user manually entering a new Link
 * and the Link -> ORM level.
 * 
 * @author alandonohoe
 *
 */
@ManagedBean
@ViewScoped
public class ManualLink implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String targetUrl;
	private String sourceUrl;
	private String anchorText;
	private String campaign;
	private String client;
	private String linkActivity;
	private Integer linkActivityKey; // used in the link activity drop down box
	private Integer campaignID; // the id of the campaign in the DB.
	private Date date;
	
	private boolean backLinks;
	private boolean socialData;
	private boolean contentCreation;
	
	
	public ManualLink(){
		
		this.date = DateUtil.getTodaysDate();
		
		contentCreation = false;
		socialData = false;
		backLinks = false;
		
		anchorText = "";
		
	}
	
	
	


	public Integer getCampaignID() {
		return campaignID;
	}





	public void setCampaignID(Integer campaignID) {
		this.campaignID = campaignID;
	}





	public String getCampaign() {
		return campaign;
	}



	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}



	public String getClient() {
		return client;
	}



	public void setClient(String client) {
		this.client = client;
	}



	public boolean isContentCreation() {
		return contentCreation;
	}

	public void setContentCreation(boolean contentCreation) {
		this.contentCreation = contentCreation;
	}

	public boolean isBackLinks() {
		return backLinks;
	}



	public void setBackLinks(boolean backLinks) {
		this.backLinks = backLinks;
	}



	public boolean isSocialData() {
		return socialData;
	}



	public void setSocialData(boolean socialData) {
		this.socialData = socialData;
	}



	public String getTargetUrl() {
		return targetUrl;
	}


	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}


	public String getSourceUrl() {
		return sourceUrl;
	}


	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}


	public String getAnchorText() {
 		return anchorText;
	}
	public void setAnchorText(String anchorText) {
		this.anchorText = anchorText;
	}
	public String getLinkActivity() {
		return linkActivity;
	}
	public void setLinkActivity(String linkActivity) {
		
		this.linkActivity = linkActivity;
		
		if(LinkReportConstants.contentCreationText.contentEquals(linkActivity))
			contentCreation = true;
		else
			contentCreation = false;
	}
	
	
	public Integer getLinkActivityKey() {
		return linkActivityKey;
	}

	public void setLinkActivityKey(Integer linkActivityKey) {
		this.linkActivityKey = linkActivityKey;
	}

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}
