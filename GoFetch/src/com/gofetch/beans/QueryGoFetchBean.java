package com.gofetch.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.gofetch.entities.Link;
import com.gofetch.entities.LinkDBService;
import com.gofetch.entities.MiscSocialData;
import com.gofetch.entities.MiscSocialDataDBService;
import com.gofetch.entities.URL;
import com.gofetch.entities.URLDBService;
import com.gofetch.entities.URLPlusSocialData;

@ManagedBean
@RequestScoped
public class QueryGoFetchBean implements Serializable{

	private static Logger log = Logger.getLogger(QueryGoFetchBean.class.getName());
	private static final long serialVersionUID = 1L;

	private Integer linksToShow;
	private String urlTarget = "http://";
	private String userID;
	private Date startDate;
	private Date endDate;

	List<Link> linksList = null;
	List<URLPlusSocialData> urlPlusSocialList = new ArrayList<URLPlusSocialData>();

	// used to access the DB:
	private URLDBService urlDB = new URLDBService();
	private LinkDBService linkDB = new LinkDBService();
	private MiscSocialDataDBService socialDataDB = new MiscSocialDataDBService();

	// used in autocomplete 
	private List<String> urlAddressesInDB = new ArrayList<String>();
	private List<URL> urlsinDB = null;
	private List<String> results = new ArrayList<String>();

	public QueryGoFetchBean(){

		//		urlDB = new URLDBService();
		//		urlsinDB = urlDB.getURLs();
		//
		//		for (URL url : urlsinDB) {
		//			urlAddressesInDB.add(url.getUrl_address());
		//		}
	}



	public boolean isListSize() {


		if(null == urlPlusSocialList)
			return false;

		if(urlPlusSocialList.isEmpty())
			return false;

		int size = urlPlusSocialList.get(0).getSocialDataList().size();

		if(size > 0)
			return true;
		else
			return false;
	}

	public String visualiseData(){
		//TODO: enter the visualisation logic here...
		return "ChooseVisual.html";
	}

	public String exportToExcel(){
		//TODO: enter the excel logic here...
		return "QueryGoFetch";

	}


	/**
	 * Fills urlTree with all the urls and assoc'd social data that point to urlTarget
	 *  - also includes urlTarget and all its social data as first entry.
	 * @return
	 */
	public String retrieveURLData() {

		//TODO: add error message here to feedback to user
		if(urlTarget.isEmpty())
			return "QueryGoFetch";

		log.info("Entering QueryGoFetchBean: retrieveURLData");

		URL currentURL;
		List <MiscSocialData> socialData = null;
		URLPlusSocialData urlPlusSocialData = new URLPlusSocialData();

		urlPlusSocialList.clear(); //refresh on each call...

		linksList = linkDB.getLinksInTreePointingTo(urlTarget);

		/////////////////////////
		//get target URL and associated social data first:	

		currentURL = urlDB.getURL(urlTarget);

		//TODO: add error message here to feedback to user
		if(null==currentURL) // url not in DB.
			return "QueryResults";

		socialData = socialDataDB.getAllSocialData(currentURL.getId());

		urlPlusSocialData.setUrl(currentURL);	
		urlPlusSocialData.setSocialDataList(socialData);

		// then add to member data
		urlPlusSocialList.add(urlPlusSocialData);

		//
		//////////////////////

		String linksTotal = Integer.toString(linksList.size());
		int linksCounter = 1;

		//Commented this out - so this means we will only ever get the target data - not backlinks
		//	to get them, use API or need to program in pagination and ajax - as large datasets cause a timeout
		//run through all links. getting url and assoc'd social data....
//		for(Link link: linksList){
//
//			log.info("Processing link " + Integer.toString(linksCounter) + " of " + linksTotal );
//
//			URLPlusSocialData urlLinksPlusSocialData = new URLPlusSocialData();
//
//			currentURL = urlDB.getURL(link.getSource_id());
//			socialData = socialDataDB.getAllSocialData(link.getSource_id());
//
//			urlLinksPlusSocialData.setUrl(currentURL);
//			urlLinksPlusSocialData.setSocialDataList(socialData);	
//
//			// then add to member data
//			//urlTree.getUrlPlusSocialData().add(urlPlusSocialData);
//			urlPlusSocialList.add(urlLinksPlusSocialData);
//
//		}

		return "QueryResults";

	}

	//TODO: change this to something faster like: http://igoro.com/archive/efficient-auto-complete-with-a-ternary-search-tree/
	//	public List<String> complete(String query) {
	//
	////		urlDB = new URLDBService();
	////		urlsinDB = urlDB.getURLs();
	////
	////		for (URL url : urlsinDB) {
	////			urlAddressesInDB.add(url.getUrl_address());
	////		}
	//
	//		
	//
	//		for (String possibleURL : urlAddressesInDB) {
	//
	//			if (possibleURL.startsWith(query)) {
	//				results.add(possibleURL);
	//			}
	//		}
	//
	//		return results;
	//	}
	//	
	//	public URLTree getUrlTree() {
	//		return urlTree;
	//	}

	public List<Link> getLinksList() {
		return linksList;
	}

	public void setLinksList(List<Link> linksList) {
		this.linksList = linksList;
	}

	public List<URLPlusSocialData> getUrlPlusSocialList() {
		return urlPlusSocialList;
	}

	public void setUrlPlusSocialList(List<URLPlusSocialData> urlPlusSocialList) {
		this.urlPlusSocialList = urlPlusSocialList;
	}
	public Integer getLinksToShow() {
		return linksToShow;
	}

	public void setLinksToShow(Integer linksToShow) {
		this.linksToShow = linksToShow;
	}

	public String getUrlTarget() {
		return urlTarget;
	}

	public void setUrlTarget(String urlTarget) {
		this.urlTarget = urlTarget;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	// ////////
	// use JSF navigation to return/refresh the main page.
	public String refresh() {
		return "index";
	}

	//TODO: change this to something faster like: http://igoro.com/archive/efficient-auto-complete-with-a-ternary-search-tree/
	public List<String> complete(String query) {
		
		// new code:
		//urlDB = new URLDBService();
		
		results = urlDB.getURLAddressesStartingWith(query, 5);
		
		return results;
	
//		old code
//		//TODO: replace this with one call - get URLaddresses and use a native query in the URLDB's JPQL....
//		if(urlAddressesInDB.isEmpty()){
//			urlDB = new URLDBService();
//			urlsinDB = urlDB.getURLs();
//
//			for (URL url : urlsinDB) {
//				urlAddressesInDB.add(url.getUrl_address());
//			}
//
//		}
//
//		for (String possibleURL : urlAddressesInDB) {
//
//			if (possibleURL.startsWith(query)) {
//				results.add(possibleURL);
//			}
//		}
//
//		return results;
	}
}
