package com.gofetch.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.gofetch.GoFetchConstants;
import com.gofetch.entities.URL;
import com.gofetch.entities.URLDBService;
import com.gofetch.utils.DateUtil;
import com.gofetch.utils.TextUtil;

//TODO: move all the logic from the URL tab into this bean, and have this bean as a member of the dataentrybacking bean
// 22-1-13: couldnt get the fields updated for some reason when this ... so moved fields and methods to DataEntryBacking... - hack..
@ManagedBean
@ViewScoped
public class EnterURLBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(EnterURLBean.class
			.getName());
	
	private String urlEntryTargetURL;
	private String urlEntryUserName;
	private boolean urlEntryBackLinkData = true;
	private boolean urlEntrySocialData = true;
	
	public EnterURLBean(){
				
		urlEntryTargetURL = "http://";
		urlEntryUserName = "@propellernet.co.uk";
				
	}

	// action controller method: for adding new urls to monitor
	public String enterNewURLs() {

		List<String> errorReport = new ArrayList<String>();
		List<String> successReport = new ArrayList<String>();
		//		errorReport = new ArrayList<String>();
		//		successReport = new ArrayList<String>();
		//		urlAddressesInDB = new ArrayList<String>();
		List<String> results =  new ArrayList<String>();

		URLDBService urlDB = new URLDBService();

		List<String> urlList = new ArrayList<String>();
		List<String> urlListPassed = new ArrayList<String>();
		List<URL> urlsinDB = null;
		String tempString = null;

		// first split the user entered string into single urls - space
		// separated.
		String[] urls = urlEntryTargetURL.split(" ");

		int noOfURLs = urls.length;

		// //////////
		// check all entries length and "http://" prefix
		for (int i = 0; i < noOfURLs; i++) {

			// remove whitespace
			urls[i] = urls[i].trim();

			if (urls[i].length() < GoFetchConstants.MIN_VALID_URL_LENGTH) {

				tempString = urls[i]
						+ " - is too short to be added to GoFetch's database.";
				errorReport.add(tempString);

			} else if (!urls[i].startsWith("http")) {

				tempString = urls[i]
						+ " -  \"http://\" has been added to the beginning of URL and has been be added to GoFetch.";
				successReport.add(tempString);

				String temp = "http://" + urls[i];

				urls[i] = temp;

				urlList.add(urls[i]);
			}

			else { // its passed both basic checks - then can add to DB

				urlList.add(urls[i]);
			}
		}
		//
		// //////////////



		// /////////////////////
		// run through passed urls and check that url not already in DB....
		for (int x = 0; x < urlList.size(); x++) {
			
			
			// remove https: to avoid duplication of urls 
			String urlAddress = urlList.get(x);
			urlAddress = TextUtil.replaceHttpsWithHttp(urlAddress);	

			if ((urlDB.urlInDB(urlAddress))||(urlDB.urlInDB(TextUtil.addSlashToEndOfString(urlAddress)))) { // check that url not already in DB....

				tempString = urlAddress
						+ " -  is already entered in GoFetch and is being monitored.";
				errorReport.add(tempString);

			} else { // add to final list to add:

				urlListPassed.add(urlAddress);

			}
		}

		//
		// //////////////

		// ////////
		// add the url to the DB

		for (String newURL : urlListPassed) {

			URL url = new URL();

			Date todaysDate = DateUtil.getTodaysDate();

			url.setDate(todaysDate);
			url.setSocial_data_date(todaysDate);
			url.setSocial_data_freq(GoFetchConstants.DAILY_FREQ);
			url.setUrl_address(newURL);
			url.setUser_id(urlEntryUserName);


			url.setGet_backlinks(urlEntryBackLinkData);
			url.setGet_social_data(urlEntrySocialData);
			//url.setNo_of_layers(noOfLayers);
			url.setBacklinks_got(false); // turns to true, when ProcessNewTargets have got this url's backlinks
			url.setData_entered_by(GoFetchConstants.URL_ENTERED_BY_USER); 


			try {
				urlDB.createURL(url);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.severe(e.getMessage());
			}

			// add to report:

			successReport.add(newURL);

		}

		//
		// /////////

		//TODO - do growl / light box instead.......
		return ("submitURLReport");


	}
	
	
	///////////////////////
	// setter and getters

	

	public String getUrlEntryTargetURL() {
		return urlEntryTargetURL;
	}

	public void setUrlEntryTargetURL(String urlEntryTargetURL) {
		this.urlEntryTargetURL = urlEntryTargetURL;
	}

	public String getUrlEntryUserName() {
		return urlEntryUserName;
	}

	public void setUrlEntryUserName(String urlEntryUserName) {
		this.urlEntryUserName = urlEntryUserName;
	}

	public boolean isUrlEntryBackLinkData() {
		return urlEntryBackLinkData;
	}

	public void setUrlEntryBackLinkData(boolean urlEntryBackLinkData) {
		this.urlEntryBackLinkData = urlEntryBackLinkData;
	}

	public boolean isUrlEntrySocialData() {
		return urlEntrySocialData;
	}

	public void setUrlEntrySocialData(boolean urlEntrySocialData) {
		this.urlEntrySocialData = urlEntrySocialData;
	}

	

}

