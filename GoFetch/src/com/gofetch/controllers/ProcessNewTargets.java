package com.gofetch.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gofetch.GoFetchConstants;
import com.gofetch.email.AdminEmailHelper;
import com.gofetch.entities.Link;
import com.gofetch.entities.LinkDBService;
import com.gofetch.entities.URL;
import com.gofetch.entities.URLDBService;
import com.gofetch.seomoz.Constants;
import com.gofetch.seomoz.SEOMoz;
import com.gofetch.seomoz.SEOMozImplFreeAPI;
import com.gofetch.seomoz.URLPlusDataPoints;
import com.gofetch.utils.DateUtil;
import com.gofetch.utils.TextUtil;

public class ProcessNewTargets extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 

	private static Logger log =  Logger.getLogger(ProcessNewTargets.class
			.getName());

	/**
	 * Pre-conditions:
	 * 
	 * Post-conditions: upon exit, all the target urls that were entered by
	 * user(s) will: 
	 * 1) have all their backlinks (known by SEOMoz server at least) entered into the url table. And if the call to SEOMoz is successful, then these target url's 
	 * 		backlink_got field's are set to true.
	 * 2) all these backlinks will
	 * have the same get_social_data value as their target url. true/false. 
	 * 3)
	 * for every backlink, an entry will be made into the links table, which
	 * records source and target url for each link 
	 * 4) for every entry into the
	 * link table, a final_target_url will be assigned from each target url to
	 * source url. - this will be the id of the final target url in the
	 * tree of linked urls. = The root of the tree, which is - the url that was
	 * originally entered by the user. This connects the cascade of urls which
	 * simplifies deleting - or modifying the series of links pointing to
	 * user-entered target url.
	 * 
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		log.info("in doGet");

		URLDBService urlDBUnit = new URLDBService();
		SEOMoz seoMoz = new SEOMozImplFreeAPI();
		AdminEmailHelper adminEmail = new AdminEmailHelper();
		List<URLPlusDataPoints> backLinks = null;
		String currentTargetAddress;
		int counter = 1, processedURLs =0, totalUnprocessedURLs, totalUnprocessedURLsFinal = 0;
		boolean firstRun = true; // only here for the free SEOMoz API limiter.
		AdminEmailHelper emailAdmin = new AdminEmailHelper();

		resp.setContentType("text/plain");

		//returns all the urls that have get_backlinks = true && backlinks_got = false
		// currently 20 - per day
		List<URL> unprocessedURLs = urlDBUnit.getUnproccessedTargetURLs(GoFetchConstants.NO_OF_URLS_TO_PROCESS);
		totalUnprocessedURLs = urlDBUnit.getNoOfUnProccessedTUrls();

		if (!unprocessedURLs.isEmpty()) {

			log.info("Number of unprocessed URLs in this session: " + String.valueOf(unprocessedURLs.size()));
			log.info("Number of Total unprocessed URLs: " + totalUnprocessedURLs);
			
			for (URL currentURL : unprocessedURLs) {

				boolean getLinksSuccessful = true; // reset this flag, in case preceding url failed.
				currentTargetAddress = currentURL.getUrl_address(); 

				log.info("Current URL: " + counter++ +": " +currentTargetAddress);

				if (!firstRun) {
					try {
						Thread.sleep(Constants.FREE_API_SEOMOZ_SERVER_DELAY);
					} catch (InterruptedException e) {
						// from: http://stackoverflow.com/questions/9139128/a-sleeping-thread-is-getting-interrupted-causing-loss-of-connection-to-db
						Thread.currentThread().interrupt(); // restore interrupted status
					}
				}

				firstRun = false;
				
				try {
					backLinks = seoMoz.getLinks(currentTargetAddress);
				} catch (Exception e) {
					String msg = "Exception thrown getting backlink data for: "
							+ currentTargetAddress + ": " + e.getMessage();
					log.warning(msg);

					getLinksSuccessful = false;
				}
				// this section below ONLY called if we have has a successful call to SEOMoz - even if there's no links for this target.
				if(getLinksSuccessful){

					if (!backLinks.isEmpty()) { // if we have successfully  got some data from SEOmoz

						log.info("Got backlinks for " + currentURL.getUrl_address() + ". No of links = " + String.valueOf(backLinks.size()));

						try{
							//write each backlink to url table. - with same
							// data as target, BUT  get_backlink = false.
							linksToNewSEOMozURLs(backLinks, currentURL);

							// for each new source url - retrieve it's id and
							// the target's id from DB and
							// create new entry in link table, with target id
							// and source id, anchor text, todays date and
							// domain name extracted from url address.
							linksToNewLinks(backLinks, currentURL);

						}catch(Exception e){
							log.warning("Error in creating new links / urls. "  + e.getMessage());
						}

					} else {
						log.info("No backlinks from SEOMoz for: " + currentTargetAddress);
					}

					// change flag - so this will not be included in further calls for backlinks
					urlDBUnit.updateBackLinksGot(currentURL, true);
					processedURLs++;

				}else{ // getLinksSuccessful == false
					urlDBUnit.updateBackLinksGot(currentURL, false); // problem getting backlinks - try again on next sweep.
	
					try {
						adminEmail.sendWarningEmailToAdministrator("Problem getting backlinks for: " + currentURL.getUrl_address());
					} catch (Exception e) {
						log.warning("Problem sending email: " + e.getMessage());
					}
				}
			} // end of: for (URL currentURL : unprocessedURLs)
			
			totalUnprocessedURLsFinal = urlDBUnit.getNoOfUnProccessedTUrls();

			try {
				emailAdmin.sendInfoEmailToAdministrator("No Of Unprocessed URLs initial: " + totalUnprocessedURLs
						+ ". No Of Unprocessed URLs final: " + totalUnprocessedURLsFinal + ". No of processed URLs" + unprocessedURLs);
			} catch (Exception e) {
				log.warning("Problem Sending email");
			}

		} else {
			log.info("No Target URLs");
		}
		
		
		// send "summary" to admin if there's been significant error with crawl
		// 1: there's been a problem processing any of the urls
		if(processedURLs < unprocessedURLs.size()){
			try {
				adminEmail.sendWarningEmailToAdministrator("No of processed URLs: " + processedURLs + " out of: " + unprocessedURLs.size());
			} catch (Exception e) {
				log.warning("Problem sending email: " + e.getMessage());
			}
		}
		
		//2: if there's a discrepancy btwn how many ought to have been updated and the no that actually were in the DB...
		// moved this up...
//		totalUnprocessedURLsFinal = urlDBUnit.getNoOfUnProccessedTUrls();
		log.info("totalUnprocessedURLs: " + totalUnprocessedURLs + " processedURLs: " + processedURLs + " totalUnprocessedURLsFinal: " + totalUnprocessedURLsFinal);
		if(totalUnprocessedURLsFinal != (totalUnprocessedURLs - processedURLs)){
			try {
				adminEmail.sendWarningEmailToAdministrator("Unprocessed URLs in DB originally: " + totalUnprocessedURLs + "\n"
						+ " Now: " + totalUnprocessedURLsFinal + "\n Proccessed URLs in this session: " + processedURLs );
			} catch (Exception e) {
				log.warning("Problem sending email: " + e.getMessage());
			}
		}

		try {
			getServletContext().getRequestDispatcher("/index.jsf").forward(
					req, resp);
		} catch (ServletException e) {}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		doGet(req, resp);
	}

	/**
	 * Writes backLinks list as new URLs to url table in database. There may be
	 * a case where a backlink may already exist as a url in the table, - when
	 * it points to another target already. - In this case, then do not add it
	 * again to the url table, but just use its url_id later to add just the new
	 * link to the link table.
	 * 
	 * @param backLinks
	 * @param currentURL
	 * @param urlDBUnit
	 */
	private void linksToNewSEOMozURLs(List<URLPlusDataPoints> backLinks,
			URL currentURL) {

		Integer noOfLinks = backLinks.size();
		Integer counter = 1;
		Integer noNewURLs = 0; //TODO: include these in a report to monitor how many new links we have this time
		Integer noPreviouslyEnteredURLs = 0; 	// ..compared to last time.


		URLDBService urlDBUnit = new URLDBService();
		String domainName;
		String urlPlusHttp;
		Date todaysDate = DateUtil.getTodaysDate();

		for (URLPlusDataPoints currentBackLink : backLinks) {

			domainName = TextUtil.returnDomainName(currentBackLink
					.getBackLinkURL());
			// SEOMoz returns urls with no http:// prefix
			urlPlusHttp = TextUtil.addHTTPToURL(currentBackLink
					.getBackLinkURL());

			log.info(String.valueOf(counter++) + " of " + String.valueOf(noOfLinks) + " links.");

			URL url = new URL();
			url.setUrl_address(urlPlusHttp);
			url.setDate(todaysDate);
			url.setGet_social_data(false);
			url.setGet_backlinks(false);

			url.setSocial_data_freq(GoFetchConstants.DAILY_FREQ);
			url.setSocial_data_date(todaysDate);

			url.setDomain(domainName);
			url.setSeomoz_url(true); //TODO: delete this field
			url.setBacklinks_got(false);
			url.setData_entered_by(GoFetchConstants.URL_ENTERED_BY_SEOMOZ); 

			url.setGet_authority_data(true); // add to get PA and DA crawl....

			if(null != currentURL.getClient_category_id())
				url.setClient_category_id(currentURL.getClient_category_id());

			if(null != currentURL.getUsers_user_id())
				url.setUsers_user_id(currentURL.getUsers_user_id());

			try {
				if(urlDBUnit.createURL(url))
					noNewURLs++;
				else
					noPreviouslyEnteredURLs++;
			} catch (Exception e) {
				log.severe(e.getMessage());
			}
		}
	}

	/**
	 * Retrieves the id assigned to each link in backLinks list, and the id
	 * assigned to target current URL and writes both as new link to the links
	 * table in the DB, with today's date as date detected parameter.
	 * 
	 * The link table can have entries with duplicate backlink entries and
	 * duplicate target entries but NOT in the same link entry. Every link entry
	 * has to be unique in terms of combined source and target url.
	 * 
	 * 
	 * @param backLinks
	 * @param currentURL
	 * @param urlDBUnit
	 * 
	 *            Pre-conditions: the target url has not been entered before.
	 *            the links to the target url have not been entered into the
	 *            links table already.
	 * 
	 *            Post-conditions: any new backlinks not already entered in the
	 *            links table will be now entered into the table any backlinks
	 *            already in the table will be unaffected.
	 */
	//@SuppressWarnings("unused") 
	private void linksToNewLinks(List<URLPlusDataPoints> urlBackLinks,
			URL currentURL) {

		if (urlBackLinks.isEmpty())
			return;

		LinkDBService linksDBUnit = new LinkDBService();
		URLDBService urlDBUnit = new URLDBService();

		//List<Integer> currentLinkIDs;
		List<String> currentLinksAddresses=null;// = new ArrayList<String>();
		URL backLinkURL;
		String httpURL;

		Date today = DateUtil.getTodaysDate();

		// TODO: this seems v inefficent - the two steps....- need to look into
		// alternatives... using SQL... join?? inner query?

//		// get all the source URLs pointing to target
//		currentLinkIDs = linksDBUnit.getSourceURLIDsPointingTo(currentURL);
//
//		// fill list of all url addresses of links pointing at target
//		if (!currentLinkIDs.isEmpty()) {
//			for (Integer currentLinkID : currentLinkIDs) {
//
//				currentLinksAddresses.add(urlDBUnit
//						.getURLAddressFromID(currentLinkID));
//			}
//		}
		
		//TODO: replaced the above loop, with the call below, delete the above after testing.
		currentLinksAddresses = getURLsAddressesPointingTo(currentURL);

		// run through all the backlinks....
		for (URLPlusDataPoints currentURLBackLink : urlBackLinks) {

			// dealing with SEOMoz supplied urls - so add http://...
			httpURL = TextUtil.addHTTPToURL(currentURLBackLink
					.getBackLinkURL());

			log.info("Entering new link: " + httpURL);

			// check that current backLink does not exist in target's links list
			if (!currentLinksAddresses.contains(httpURL)) {

				// then finally... create new link...
				Link link = new Link();
				backLinkURL = urlDBUnit.getURL(httpURL);
				link.setSource_id(backLinkURL.getId());
				link.setTarget_id(currentURL.getId());
				link.setAnchor_text(currentURLBackLink.getBackLinkAnchorText());
				link.setDate_detected(today);
				link.setFinal_target_url_id(currentURL.getId());
				link.setData_entered_by(GoFetchConstants.URL_ENTERED_BY_SEOMOZ);

				//set the client this link is associated with and the campaign if any...
				if(null!=currentURL.getUsers_user_id())
					link.setUsers_user_id(currentURL.getUsers_user_id());

				if(null!=currentURL.getClient_category_id())
					link.setClient_category_id(currentURL.getClient_category_id());

				linksDBUnit.createLink(link, false);
			}
			else{
				log.info("Link already exists in DB: " + httpURL);
			}

		}
	}
	

	//TODO: turn this into a join / nested query 
	private List<String> getURLsAddressesPointingTo(URL targetURL) {

		List<Integer> ids;
		List<String> urls = new ArrayList<String>();
		LinkDBService linkDBUnit = new LinkDBService();
		URLDBService urlDBUnit = new URLDBService();

		ids = linkDBUnit.getSourceURLsIDsPointingTo(targetURL);

		for (Integer i : ids) {
			urls.add(urlDBUnit.getURL(i).getUrl_address());
		}

		return urls;

	}

//	private List<URL> getURLsPointingTo(URL targetURL) {
//
//		List<Integer> ids;
//		List<URL> urls = new ArrayList<URL>();
//		LinkDBService linkDBUnit = new LinkDBService();
//		URLDBService urlDBUnit = new URLDBService();
//
//		ids = linkDBUnit.getSourceURLsIDsPointingTo(targetURL);
//
//		for (Integer i : ids) {
//			urls.add(urlDBUnit.getURL(i));
//		}
//
//		return urls;
//
//	}
	

	/**
	 * Fills the first link from each unique domain pointing to the target url
	 * // Get PA and DA for single link from each unique domain.... // use
	 * getURLsPointingTo(currentURL) and retrieve first url, get its PA and
	 * DA... then loop through the next urls until currentBackLinkdomainName !=
	 * latestBackLinkDomainName // then get PA and DA for that..... // then
	 * queries to only unique domain backlinks can query those with only PA & DA
	 * that != null ????
	 * 
	 * 
	 * @param targetURL
	 *            - target url
	 * @param seoMoz
	 */
	//	private void getDataForUniqueDomains(URL targetURL, SEOMoz seoMoz) {
	//
	//		List<URL> urls = getURLsPointingTo(targetURL);
	//		List<String> uniqueDomains = new ArrayList<String>();
	//
	//		String currentDomain;
	//
	//		if (null == urls)
	//			return;
	//
	//		if (urls.isEmpty())
	//			return;
	//
	//		// initialise unique domains with first domain and get its data
	//		uniqueDomains.add(urls.get(0).getDomain());
	//		getAuthorityAndDomainData(urls.get(0).getUrl_address(), seoMoz);
	//
	//		// loop through rest of the urls and get authority data for first link
	//		// from unique domain encountered
	//
	//		for (int i = 1; i < urls.size(); i++) {
	//
	//			currentDomain = urls.get(i).getDomain();
	//
	//			if (!uniqueDomains.contains(currentDomain)) {
	//				// add to unique domains
	//				uniqueDomains.add(currentDomain);
	//
	//				// and get data
	//				try {
	//					Thread.sleep(Constants.FREE_API_SEOMOZ_SERVER_DELAY);
	//				} catch (InterruptedException e) {
	//					String msg = "Exception thrown: ProcessNewTargets - getDataForUniqueDomains \n";
	//					// logger.logp(Level.SEVERE, "ProcessNewTargets",
	//					// "getDataForUniqueDomains",msg ,e);
	//					log.warning(msg + e.getMessage());
	//
	//				}
	//
	//				try {
	//					getAuthorityAndDomainData(urls.get(i).getUrl_address(),
	//							seoMoz);
	//				} catch (Exception e) {
	//					String msg = "Exception thrown getting data for "
	//							+ urls.get(i).getUrl_address()
	//							+ " ProcessNewTargets: getDataForUniqueDomains \n";
	//					log.warning(msg + e.getMessage());
	//
	//				}
	//			}
	//		}
	//
	//	}

	
}
