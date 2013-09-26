package com.gofetch.controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gofetch.GoFetchConstants;
import com.gofetch.email.AdminEmailHelper;
import com.gofetch.entities.MiscSocialData;
import com.gofetch.entities.MiscSocialDataDBService;
import com.gofetch.entities.URL;
import com.gofetch.entities.URLDBService;
import com.gofetch.socialdata.ManualSocialDataImpl;
import com.gofetch.socialdata.SocialData;
import com.gofetch.utils.DateUtil;

/*
 * 14/1/2013 - moved the social data crawl to this servlet to separate processing new SEOMoz urls and social data crawl.
 * So now either can be called separately.
 */

public class URLsSocialDataCrawl extends HttpServlet {

	private static final long serialVersionUID = 1L; 

	private static Logger log = Logger.getLogger(ProcessNewTargets.class
			.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		resp.setContentType("text/plain");

		//TODO: put this in the admin table in DB.
		int noOfURLsToCheck = 500; //GAE seems to have an issue getting more than 500...

		getURLSocialData(noOfURLsToCheck);


		try {
			getServletContext().getRequestDispatcher("/index.jsf").forward(
					req, resp);
		} catch (ServletException e) {

			String msg = "Exception thrown in URLsSocialDataCrawl\n";
			log.severe(msg + e.getMessage());

		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		doGet(req, resp);
	}


	/**
	 * retrieves a list of all urls in DB that have had their get "getSocialData
	 * checkbox checked then checks each url to see if their current associated
	 * data (FB & twitter) is different to what is saved previously in the DB.
	 * If it is different: a new social entry is created with yesterdays date,
	 * and the url's current data is persisted.
	 * @param noOfURLsToCheck 
	 */
	private void getURLSocialData(int noOfURLsToCheck) {

		// 1: get list of all urls that have "get_social_data" checked...

		// urls back from DB
		List<URL> urls = null;

		MiscSocialData socialDataAPINoSlash = null;
		MiscSocialData socialDataAPISlash = null;
		MiscSocialData assimilatedSocialData = null;
		MiscSocialData socialDataFromDB = null;
		SocialData manualSocialData = null;
		MiscSocialDataDBService socialDataDBUnit = null;
		URLDBService urlDBUnit = null;

		int i = 0, noOfMiscSocialDataStart = 0, noOfMiscSocialDataEnd, noOfSocialURLsStart, noOfSocialURLsEnd;

		String urlAddressNoSlash, urlAddressSlash, urlAddress, noOfSocialURLs = null, urlAddressEncoded;

		try{

			// URL database communication
			urlDBUnit = new URLDBService();

			// used for social data database communication
			socialDataDBUnit = new MiscSocialDataDBService();
			socialDataFromDB = null;	

			// used as the more costly, manual backup social data service	
			manualSocialData = new ManualSocialDataImpl();
			


			// get urls where get social data = true 
			//	and social data date <= todays date (not just = today's date, in case there was an error with updating the social date some urls would be left behind & lost)
			// 11-1-13 - changed query so that most out of date urls are at the top of the list - ie: select ..... from url order by social_data_date asc;

			urls = urlDBUnit.getTodaysSocialCrawlURLs(noOfURLsToCheck);

			// 2: loop through checking to see if their social data has changed from
			// previous entry... update if true.

			noOfSocialURLs = String.valueOf(urls.size());
			
			//For testing that we are actually successfully increasing the number of social data entry
			noOfMiscSocialDataStart = socialDataDBUnit.getNoOfTotalSocialData();


		}catch(Exception e){

			log.severe("Exception: " + e.getMessage());
		}

		for (URL currentURL : urls) {

			// reset on every loop
			socialDataAPINoSlash = null;
			socialDataAPISlash = null;
			assimilatedSocialData = null;
			boolean manualSocialDataGot = true; 
			double pcDifferenceBtwnSocialData;

			urlAddress = currentURL.getUrl_address();

			//TODO: there;s some issues with hashtags, and other non-text characters not getting any data back from any of the data providers
			//	need to : encode: and test.. ex: http://www.visitnorway.com/uk/the-scream/#content - becomes - http%3A%2F%2Fwww.visitnorway.com%2Fuk%2Fthe-scream%2F%23content
			// 	but still not getting all the data back  - but some - so for now, better than nothing...

			//TODO: add other chars here we want to encode: " ' " seemed to make subsequent calls to social APIs fail..
			if(urlAddress.contains("#")){
				log.info("URL contains #. Encoding: " + urlAddress);
				try {
					urlAddress = URLEncoder.encode(urlAddress, "UTF-8");
				} catch (UnsupportedEncodingException e1) {

					log.warning("Problem encoding: " + urlAddress + "" + e1.getMessage());
				}
				log.info("Is now: " + urlAddress);

			}

			i++;

			log.info(String.valueOf(i) + " of " + noOfSocialURLs + " : " + urlAddress);

			// remove final / if present...
			if(urlAddress.endsWith("/")){
				urlAddress = urlAddress.substring(0,(urlAddress.length() -1));
			}

			urlAddressNoSlash = urlAddress;
			urlAddressSlash = urlAddress + "/";



			try {
				//TODO: need to break calls into respective social services, then if first call to delicious and / or stumbled fails,
				// 	then we can skip them in the subsequent call using urlAddressNoSlash.
				//		need to bring up the construction of the miscsocialdata object up to this layer....
				// actually - might just best to move the call to twitter here - and any other APIS that
				// 	have the same data for both slashed and non-slashed urls.
				socialDataAPISlash = manualSocialData.getAllSocialData(urlAddressSlash);


			} catch (Exception e) {
				manualSocialDataGot = false;
			}

			try {

				socialDataAPINoSlash = manualSocialData.getAllSocialData(urlAddressNoSlash);

			} catch (Exception e) {
				//					String msg = "Manual Implementation Social data for: "
				//							+ urlAddressNoSlash + " failed"
				//							+ ". URLsSocialDataCrawl: getURLSocialData. \n";
				//					log.severe(msg + e.getMessage());

				manualSocialDataGot = false;
			}


			if(manualSocialDataGot){ // this is ONLY false, if all calls to the social APIs have failed - v unlikely..

				//////
				// assimilate the two collection of Social data metrics (from both url + slash and url no slash)
				//	
				assimilatedSocialData = assimilateSocialData(socialDataAPINoSlash, socialDataAPISlash);

				// get most recently persisted social data.....
				socialDataFromDB = socialDataDBUnit.getMostRecentSocialData(currentURL.getId());

				// if social data from DB, is empty or different from most
				// recent social data from APIs... then persist new social data

				pcDifferenceBtwnSocialData = persistSocialData(socialDataFromDB, assimilatedSocialData, socialDataDBUnit, currentURL.getId());

				updateSocialCrawlFrequency(currentURL, pcDifferenceBtwnSocialData, urlDBUnit);

				//TODO: here add the code that will check and set the frequency of check_freq: for every url.
				//	daily = 1, weekly = 2, monthly =3, daily - get all urls that have check_freq < 2, weekly get all urls with check_freq < 3, etc
				// checkURLsSocialCheckFreq() {update url increase / decrease freq. and email "owner" if there's a spike - or on monthly
				//	when there's no change from one month to another - email owner - to ask if they want to delete URL}

			} else { // if all calls to the social APIs have failed

				// TODO: record urls and email/ alert user (using their email) /
				// error screen somehow that of this list.....
				String msg = "Retrieving social data for: "
						+ urlAddress + " completely failed.";

				log.severe(msg);

				// maybe check here for a 404 - and if true - then delete url?
			}
		}
		
		noOfMiscSocialDataEnd = socialDataDBUnit.getNoOfTotalSocialData();
		
		if(noOfMiscSocialDataStart >= noOfMiscSocialDataEnd){
			
			AdminEmailHelper emailAdmin = new AdminEmailHelper();
			
			try {
				emailAdmin.sendWarningEmailToAdministrator("URLsSocialCrawl broken: noOfMiscSocialDataStart >= noOfMiscSocialDataEnd \n"
						+ "noOfMiscSocialDataStart: " + noOfMiscSocialDataStart + " noOfMiscSocialDataEnd: " + noOfMiscSocialDataEnd);
			} catch (Exception e) {
				log.warning("Problem Sending email");
			}
		}
	}

	private MiscSocialData assimilateSocialData(MiscSocialData data1, MiscSocialData data2){

		MiscSocialData assimilatedData = new MiscSocialData();	

		/////////////////////////

		// run through every data type... using assimilateHelper method to return one or sum of the values from the sharedCount objects...
		assimilatedData.setDelicious(			assimilateHelper(data1.getDelicious(),		 data2.getDelicious(),GoFetchConstants.ALLOWED_SOCIAL_DATA_DIFFERENCE));
		assimilatedData.setGoogle_plus_one(		assimilateHelper(data1.getGoogle_plus_one(), data2.getGoogle_plus_one(),GoFetchConstants.ALLOWED_SOCIAL_DATA_DIFFERENCE));
		assimilatedData.setLinkedin(			assimilateHelper(data1.getLinkedin(),		 data2.getLinkedin(),GoFetchConstants.ALLOWED_SOCIAL_DATA_DIFFERENCE));
		assimilatedData.setPinterest(			assimilateHelper(data1.getPinterest(),		 data2.getPinterest(),GoFetchConstants.ALLOWED_SOCIAL_DATA_DIFFERENCE));
		assimilatedData.setStumble_upon(		assimilateHelper(data1.getStumble_upon(),	 data2.getStumble_upon(),GoFetchConstants.ALLOWED_SOCIAL_DATA_DIFFERENCE));
		assimilatedData.setTwitter(				assimilateHelper(data1.getTwitter(),		 data2.getTwitter(),GoFetchConstants.ALLOWED_SOCIAL_DATA_DIFFERENCE));	
		//facebook data:
		assimilatedData.setFb_click_Count(			assimilateHelper(data1.getFb_click_Count(),		 data2.getFb_click_Count(),GoFetchConstants.ALLOWED_SOCIAL_DATA_DIFFERENCE));
		assimilatedData.setFb_comment_Count(		assimilateHelper(data1.getFb_comment_Count(), 	 data2.getFb_comment_Count(),GoFetchConstants.ALLOWED_SOCIAL_DATA_DIFFERENCE));
		assimilatedData.setFb_commentsbox_count(	assimilateHelper(data1.getFb_commentsbox_count(),data2.getFb_commentsbox_count(),GoFetchConstants.ALLOWED_SOCIAL_DATA_DIFFERENCE));
		assimilatedData.setFb_like_Count(			assimilateHelper(data1.getFb_like_Count(),		 data2.getFb_like_Count(),GoFetchConstants.ALLOWED_SOCIAL_DATA_DIFFERENCE));
		assimilatedData.setFb_share_Count(			assimilateHelper(data1.getFb_share_Count(),		 data2.getFb_share_Count(),GoFetchConstants.ALLOWED_SOCIAL_DATA_DIFFERENCE));
		assimilatedData.setFb_total_Count(			assimilateHelper(data1.getFb_total_Count(),		 data2.getFb_total_Count(),GoFetchConstants.ALLOWED_SOCIAL_DATA_DIFFERENCE));

		return assimilatedData;

	}


	/**
	 * 
	 * @param data1
	 * @param data2
	 * @return - if data1 and data2 are the same, then returns that value,
	 * if different - returns the sum of the two values
	 */
	private int assimilateHelper(int data1, int data2){

		if(data1 != data2 )
			return (data1 + data2);
		else
			return data1;
	}

	/**
	 * 
	 * @param data1 - integer no 1 to compare
	 * @param data2 - integer no 2 to compare
	 * @param pcAllowedVariation - maximum allowed %age (inclusively) allowed before the two values passed are returned as a sum. 
	 *  	to pass 1%, pass '1', not '0.01'.
	 * @return -
	 * case 1:  if the difference between data1 and data2 is no more than pcAllowedVariation % then returns the greater of the two values, 
	 * 				- this represents that the data is the same, it just grew over time btwn the two API calls and the greater value is the most recent data.
	 * 			Useful in facebook data for popular urls  - like bbc - where a minor % difference btwn facebook data may be due to the difference in 
	 * 			time between calls to the sharedcount api...
	 * 
	 * case 2: if the difference between data1 and data2 is greater than pcAllowedVariation %, then they are summed and this value is returned      
	 * 
	 * case 3: if data1 and data2 are the same, then this value is returned...
	 * 
	 * /////////
	 * ex1 : data1 = called first. (facebook totalcount = 1200 )
	 * 	     data2 = called 2nd: Facebook totalcount = 1212)
	 * 
	 * % difference btwn the 2 = ((1212-1200) / 1212) * 100 = 1% - method returns 1212 (taking the maximum allowed pc = 1%)
	 * /////////
	 * 
	 * /////////
	 * ex2: data1 = fb total count = 100
	 * 		data2 = fb total count = 1200
	 * 		% difference btwn the 2 = ((1200-100) / 1200) * 100 = 91.6 (91.6%). - method returns sum = 1300
	 * 
	 * 
	 * /////////
	 * 
	 * ex3: data1 = fb total count = 100
	 * 		data2 = fb total count = 100
	 * 		% difference btwn the 2 = ((100-100) / 100) * 100 = 0%. - method returns one, (...the largest value) = 100
	 * 
	 * 
	 */
	//TODO: test this - can use unit testing here - assert return values based on params.
	private int assimilateHelper(int data1, int data2, double pcAllowedVariation){

		if(data1 != data2 ){

			int largestValue, smallestValue;
			double difference;

			/////////////
			//1. assign largest and smallest values
			if(data1 > data2){
				largestValue = data1;
				smallestValue = data2;
			}
			else{
				largestValue = data2;
				smallestValue = data1;
			}
			/////////////////

			//2. calc %age difference
			difference = (double)(((double)largestValue - (double)smallestValue)/(double)largestValue) *100.0;

			//3.a: return sum - if difference is greater than allowed %age
			if(difference > pcAllowedVariation){
				return (data1 + data2);

				//3.b:	its under the allowed %age - return the largest value
			}else{ 
				return largestValue;
			}

		}
		else // they are the same so just return one of them.
			return data1;

	}


	/* If there is new social data - then persists this...
	 * returns the percentage difference between the new social data and the previous entry. Returns 0, if no change and -1 if no previous entry in DB for this URL. 
	 */
	private double persistSocialData(MiscSocialData socialDataFromDB, MiscSocialData assimilatedSocialData, MiscSocialDataDBService socialDataDBUnit,  Integer currentURLID){


		//used as running total of all social data- so we can check the %age difference below
		double totalSocialDataDB, totalSocialDataToday; 
		double pcDifference;
		Integer deliciousDB, deliciousToday, fbTotalCountDB, fbTotalCountToday, googlePlusDB, googlePlusToday, 
		linkedInDB, linkedInToday, pinterestDB, pinterestToday, stumbleUponDB, stumbleUponToday, twitterDB, twitterToday;

		// if there's no social data for this URL in DB yet - just add this as first entry
		if (null == socialDataFromDB){

			assimilatedSocialData.setDate(DateUtil.getTodaysDate());

			assimilatedSocialData.setUrl_id(currentURLID);

			socialDataDBUnit.createNewSocialData(assimilatedSocialData);

			return -1;
		}

		// if there's no difference between DB data and today's data: then don't create a new entry in DB:
		if(socialDataFromDB.equals(assimilatedSocialData)){

			log.info("Todays social data no different from last saved data. url id:  " + String.valueOf(currentURLID));

			return 0;
		}

		// check individual properties of the assimilated social data against the socialDataDBUnit
		//	- if the assimilated data has any entry that is a 0, and if the DBdata is NOT 0, then use the 
		//	DB data in the new entry - this means the call to that particular social API has failed, 
		// so rather than let the rest of the data go to waste, just use previous record and update the 
		//	the social calls that DID work...

		MiscSocialData newSocialData = new MiscSocialData();

		newSocialData.setDate(DateUtil.getTodaysDate());
		newSocialData.setUrl_id(currentURLID);

		/////////Assimilate individual social metrics//////////////////////////////////////////
		//TODO: here we can get the new data and old data and look for a spike...
		// if DB data is Less than most recent API data this means the social data has increased - use API data

		deliciousDB = socialDataFromDB.getDelicious();
		deliciousToday = assimilatedSocialData.getDelicious();

		fbTotalCountDB = socialDataFromDB.getFb_total_Count(); 
		fbTotalCountToday = assimilatedSocialData.getFb_total_Count();

		googlePlusDB = socialDataFromDB.getGoogle_plus_one(); 
		googlePlusToday = assimilatedSocialData.getGoogle_plus_one();

		linkedInDB = socialDataFromDB.getLinkedin(); 
		linkedInToday = assimilatedSocialData.getLinkedin();

		pinterestDB = socialDataFromDB.getPinterest(); 
		pinterestToday = assimilatedSocialData.getPinterest();

		stumbleUponDB = socialDataFromDB.getStumble_upon(); 
		stumbleUponToday = assimilatedSocialData.getStumble_upon();

		twitterDB = socialDataFromDB.getTwitter(); 
		twitterToday = assimilatedSocialData.getTwitter();

		// get summation of social data for both DB and current social data
		totalSocialDataDB = deliciousDB + fbTotalCountDB + googlePlusDB + linkedInDB + pinterestDB + stumbleUponDB + twitterDB;
		totalSocialDataToday = deliciousToday + fbTotalCountToday + googlePlusToday + linkedInToday + pinterestToday + stumbleUponToday + twitterToday;

		// work out %age difference...
		pcDifference = (double) ((totalSocialDataToday - totalSocialDataDB)/ totalSocialDataDB) * 100;


		////////////////////////
		// delicious.....
		if(deliciousDB < deliciousToday)
			newSocialData.setDelicious(deliciousToday);
		else
			newSocialData.setDelicious(deliciousDB); // else API call has failed or its not changed from DB data

		// Facebook
		if(fbTotalCountDB < fbTotalCountToday){
			newSocialData.setFb_click_Count(assimilatedSocialData.getFb_click_Count());
			newSocialData.setFb_comment_Count(assimilatedSocialData.getFb_comment_Count());
			newSocialData.setFb_commentsbox_count(assimilatedSocialData.getFb_commentsbox_count());
			newSocialData.setFb_like_Count(assimilatedSocialData.getFb_like_Count());
			newSocialData.setFb_share_Count(assimilatedSocialData.getFb_share_Count());
			newSocialData.setFb_total_Count(fbTotalCountToday);
		}else{ 
			newSocialData.setFb_click_Count(socialDataFromDB.getFb_click_Count());
			newSocialData.setFb_comment_Count(socialDataFromDB.getFb_comment_Count());
			newSocialData.setFb_commentsbox_count(socialDataFromDB.getFb_commentsbox_count());
			newSocialData.setFb_like_Count(socialDataFromDB.getFb_like_Count());
			newSocialData.setFb_share_Count(socialDataFromDB.getFb_share_Count());
			newSocialData.setFb_total_Count(fbTotalCountDB);
		}

		//Google+
		if(googlePlusDB < googlePlusToday)
			newSocialData.setGoogle_plus_one(googlePlusToday);
		else
			newSocialData.setGoogle_plus_one(googlePlusDB);

		//LinkedIn
		if(linkedInDB < linkedInToday)
			newSocialData.setLinkedin(linkedInToday);
		else
			newSocialData.setLinkedin(linkedInDB);

		// Pinterest
		if(pinterestDB < pinterestToday)
			newSocialData.setPinterest(pinterestToday);
		else
			newSocialData.setPinterest(pinterestDB);

		//StumbleUpon
		if(stumbleUponDB < stumbleUponToday)
			newSocialData.setStumble_upon(stumbleUponToday);
		else
			newSocialData.setStumble_upon(stumbleUponDB);

		//Twitter
		if(twitterDB < twitterToday)
			newSocialData.setTwitter(twitterToday);
		else
			newSocialData.setTwitter(twitterDB);

		///////////////////////////////////////////////

		log.info("New social data entry for url: " + String.valueOf(currentURLID));
		socialDataDBUnit.createNewSocialData(newSocialData);


		return pcDifference;

	}

	private void updateSocialCrawlFrequency(URL currentURL,
			double pcDifferenceBtwnSocialData, URLDBService urlDBUnit) {

		log.info("Entering updateSocialCrawlFrequency");

		int socialFreq = currentURL.getSocial_data_freq();

		//need to check if it just new URL with no previous social data - so pcDifference would be set to 0, but shouldnt be...
		if(pcDifferenceBtwnSocialData < 0){
			currentURL.setSocial_data_date(DateUtil.getTommorrowsDate());
			urlDBUnit.updateSocialFrequencyData(currentURL);
			return;
		}

		if(pcDifferenceBtwnSocialData < GoFetchConstants.SOCIAL_DATA_RANGE_MIN){

			currentURL.decreaseSocialCrawlFrequency();

		}

		if(pcDifferenceBtwnSocialData > GoFetchConstants.SOCIAL_DATA_RANGE_MAX){

			currentURL.setSocial_data_freq(GoFetchConstants.DAILY_FREQ);
		}

		// now update the URL's next date to be included in the social data crawl
		// if may of been increased or decreased:
		socialFreq = currentURL.getSocial_data_freq();

		if(GoFetchConstants.DAILY_FREQ==socialFreq){
			// then make social data date tmw.
			currentURL.setSocial_data_date(DateUtil.getTommorrowsDate());

		}

		if(GoFetchConstants.WEEKLY_FREQ==socialFreq){
			// then make social data date next week.
			currentURL.setSocial_data_date(DateUtil.getNextWeeksDate());
		}

		if(GoFetchConstants.MONTHLY_FREQ==socialFreq){

			//TODO: implement this...
			if(0 ==pcDifferenceBtwnSocialData){
				//then.. check difference btwn dates?? and if there's x days or weeks.. set getSocialData(false)???
				// this is typically socially dead backlinks...

			}

			// then make social data date next month.
			currentURL.setSocial_data_date(DateUtil.getNextMonthsDate());
		}

		urlDBUnit.updateSocialFrequencyData(currentURL);

	}

}


