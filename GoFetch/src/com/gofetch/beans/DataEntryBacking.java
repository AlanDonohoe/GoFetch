package com.gofetch.beans;

/*
 * bean backing the link entry page
 - use this as a temp hold all, until we break down the page into a dynamic list of entries, 
 - and various beans retresenting sensible entity relationships...
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;


import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;


import com.gofetch.GoFetchConstants;
import com.gofetch.entities.*;
import com.gofetch.fileio.ExcelWrapper;
import com.gofetch.utils.DateUtil;


@ManagedBean
@ViewScoped
public class DataEntryBacking implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(DataEntryBacking.class
			.getName());
	
	private URLDBService urlDB = null;

	// bean for each tab content within this bean
	private EnterLinkBean linkBean = new EnterLinkBean();
	
	private NewClientBean newClientBean = new NewClientBean();

	///////////////////////////////////////////////////////////////////
	////////////////
	//
	private EnterURLBean urlBean = new EnterURLBean();

	
	public void upDateLinkData() {
		// TODO: this is called when user enters new data - so add.. ??
		// ajax checking.... and parse data - present user with error msg here?
		// hide . show monitor this backlink url if link activity is content
		// creation...

		// check if current tab's link activity is Content Creation...

	}

	// used in autocomplete
	private List<String> urlAddressesInDB = new ArrayList<String>();
	private List<URL> urlsinDB = null;
	private List<String> resultsAutoComplete = new ArrayList<String>();

	// ////
	// dynamic list of links
	private List<ManualLink> links;

	private boolean uploadingExcelLinks;
	private Integer user_id;
	
	public NewClientBean getNewClientBean() {
		return newClientBean;
	}

	public void setNewClientBean(NewClientBean newClientBean) {
		this.newClientBean = newClientBean;
	}

	public EnterLinkBean getLinkBean() {
		return linkBean;
	}

	public void setLinkBean(EnterLinkBean linkBean) {
		this.linkBean = linkBean;
	}

	public EnterURLBean getUrlBean() {
		return urlBean;
	}

	public void setUrlBean(EnterURLBean urlBean) {
		this.urlBean = urlBean;
	}

	// /////////
	// used in drop down box selections
	private List<String> selectedClients;

	private LinkedHashMap<String, Integer> clients;

	private List<Integer> selectedCampaigns;

	private LinkedHashMap<String, Integer> campaigns;

	private String selectedLinkActivity;

	private LinkedHashMap<String, Integer> linkActivity;

	private List<String> selectedTargetURLs;

	private LinkedHashMap<String, Integer> targetURLs;

	private String selectedClientsKey;

	private String selectedCampaignKey;

	private Integer linkUploadProgress;


	//
	// ////////////




	public DataEntryBacking() {

		uploadingExcelLinks = false;

		links = new ArrayList<ManualLink>();

		clients = new LinkedHashMap<String, Integer>();
		campaigns = new LinkedHashMap<String, Integer>();
		targetURLs = new LinkedHashMap<String, Integer>();
		linkActivity = new LinkedHashMap<String, Integer>();

		selectedClientsKey = "";
		selectedCampaignKey = "";
	}

	public List<String> getSelectedTargetURLs() {
		return selectedTargetURLs;
	}

	public Integer getLinkUploadProgress() {
		return linkUploadProgress;
	}

	public void setLinkUploadProgress(Integer linkUploadProgress) {
		this.linkUploadProgress = linkUploadProgress;
	}

	public void setSelectedTargetURLs(List<String> selectedTargetURLs) {
		this.selectedTargetURLs = selectedTargetURLs;
	}

	public LinkedHashMap<String, Integer> getTargetURLs() {
		return targetURLs;
	}

	public void setTargetURLs(LinkedHashMap<String, Integer> targetURLs) {
		this.targetURLs = targetURLs;
	}

	public String getSelectedLinkActivity() {
		return selectedLinkActivity;
	}

	public void setSelectedLinkActivity(String selectedLinkActivity) {
		this.selectedLinkActivity = selectedLinkActivity;
	}

	public Map<String, Integer> getLinkActivity() {
		return linkActivity;
	}

	public void setLinkActivity(LinkedHashMap<String, Integer> linkActivity) {
		this.linkActivity = linkActivity;
	}

	public List<Integer> getSelectedCampaigns() {
		return selectedCampaigns;
	}

	public void setSelectedCampaigns(List<Integer> selectedCampaigns) {
		this.selectedCampaigns = selectedCampaigns;
	}

	public Map<String, Integer> getCampaigns() {
		return campaigns;
	}

	public void setCampaigns(LinkedHashMap<String, Integer> campaigns) {
		this.campaigns = campaigns;
	}

	public List<String> getSelectedClients() {
		return selectedClients;
	}

	public void setSelectedClients(List<String> selectedClients) {
		this.selectedClients = selectedClients;
	}

	public Map<String, Integer> getClients() {
		return clients;
	}

	public void setClients(LinkedHashMap<String, Integer> clients) {
		this.clients = clients;
	}


	public String getSelectedClientsKey() {
		return selectedClientsKey;
	}

	public void setSelectedClientsKey(String selectedClientsKey) {
		this.selectedClientsKey = selectedClientsKey;
	}

	public String getSelectedCampaignKey() {
		return selectedCampaignKey;
	}

	public void setSelectedCampaignKey(String selectedCampaignKey) {
		this.selectedCampaignKey = selectedCampaignKey;
	}

	public Integer getUser_id() {
		return user_id;
	}


	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public List<ManualLink> getLinks() {
		return links;
	}

	public void setLinks(List<ManualLink> links) {
		this.links = links;
	}

	public List<String> getUrlAddressesInDB() {
		return urlAddressesInDB;
	}

	public void setUrlAddressesInDB(List<String> urlAddressesInDB) {
		this.urlAddressesInDB = urlAddressesInDB;
	}

	public List<URL> getUrlsinDB() {
		return urlsinDB;
	}

	public void setUrlsinDB(List<URL> urlsinDB) {
		this.urlsinDB = urlsinDB;
	}

	public List<String> getResultsAutoComplete() {
		return resultsAutoComplete;
	}



	public void setResultsAutoComplete(List<String> resultsAutoComplete) {
		this.resultsAutoComplete = resultsAutoComplete;
	}



	public void onTabChange(TabChangeEvent event){

	}

	// action controller method: for manually adding links tab
	// here we convert the view focused manaualLinks list into DB friendly Links
	// no need for any creation of new: client, campaign, or target URLs - this will have to have been done in the respective methods
	// just add new backlink URLs and links.
	// first check if link (with same T url and BL URL) exists in DB, if it does, then present error msg to user 
	// if just Backlink URL exists, though not as a link to this target URL, then add new link, using pre-existing id of URL
	public String enterNewLinks() {

		URLDBService urlDBService = new URLDBService();
		LinkDBService linkDBService = new LinkDBService();

		// TODO:
		// 1. progress bar.....
		// http://www.primefaces.org/showcase/ui/progressBar.jsf

		//boolean success = true; // at the moment, when we hit a problem - cancel the whole process then. though report to user that x no of links have been proccessed and what they need to do to fix this one to continue...
		//Integer noOfLinksProcessed;		
		boolean multipleLinks;
		Integer usersIDFromDB;
		Integer noOfCurrentLink= 0;

		String successReport = "";
		String errorReport = "";

		/////////////////
		// amend reporting if theres multiple links
		if(links.size() > 2) 
			multipleLinks = true;
		else
			multipleLinks = false;

		//////////////////
		if(uploadingExcelLinks)
			usersIDFromDB = getUsersIDFromDB(links.get(0).getClient());
		else
			usersIDFromDB = user_id;

		// check user is in the Db as a client...
		if(usersIDFromDB > 0){

			errorReport = ("Client not in GoFetch. Please add this client and then add their links.");
			FacesMessage msg = new FacesMessage(errorReport);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null; //why are we returning a string here???
		}

		// main loop for parsing each link
		for(ManualLink currentLink: links){

			//*1: ignore any links with no backlink URL - this will be the last link entry tab - and means we done processing...
			if(!currentLink.getAnchorText().isEmpty()){

				noOfCurrentLink++;	

				String targetURLAddress = currentLink.getTargetUrl();
				String sourceURLAddress = currentLink.getSourceUrl();
				URL targetURLDB, sourceURLDB;


				//TODO: check this default... assuming all urls entered as part of links ought to be monitored for social data...
				currentLink.setSocialData(true);

				// *2: get target url from url table:
				targetURLDB = urlDBService.getURL(targetURLAddress);

				// if the Target URL already exists in DB but does not have client or campaign associated with it, then update with current entered user data
				if(null != targetURLDB){

					boolean targetURLUpdated = false;

					if(0==targetURLDB.getUsers_user_id()){
						targetURLDB.setUsers_user_id(usersIDFromDB);
						targetURLUpdated= true;
					}

					if(0 == targetURLDB.getClient_category_id()){
						targetURLDB.setClient_category_id(currentLink.getCampaignID());
						targetURLUpdated = true;
					}

					if(targetURLUpdated){
						urlDBService.updateURLClientAndCampaign(targetURLDB);
					}
				}


				// we should not have a null Target URL if user manually entering links unless they are using an excel workbook - they will need to create new target URL if its not currently present
				if(!uploadingExcelLinks && null == targetURLDB){

					FacesMessage msg = new FacesMessage("Problem with Target URL: " + targetURLAddress);
					FacesContext.getCurrentInstance().addMessage(null, msg);

					return null;
				}


				// if null and we're uploadingExcelLinks - then create new Target URL and associate with client...
				if(uploadingExcelLinks && null == targetURLDB){

					try{
						//TODO: need to check with a lot of the assumptions made when creating this URL - no backlinks no social monitoring.. etc... 
						// getSocial, getBacklinks
						targetURLDB = createNewURL(targetURLAddress, usersIDFromDB, currentLink.getCampaignID(), currentLink.getDate(), true, true, GoFetchConstants.URL_ENTERED_BY_USER, true);

						urlDBService.createURL(targetURLDB);

					}catch(Exception e){
						//TODO: what to do here???  - cancel the whole process - set success to false and the next level, set check this - if false - return with error message
						//newReport += ". Problem creating: " + sourceURLAddress + " This link will not be entered into GoFetch";
						//success = false;

					}
				}

				successReport += "Target: " + targetURLAddress;
				successReport += "Source:" + sourceURLAddress;

				// now work on source URL:

				sourceURLDB = urlDBService.getURL(sourceURLAddress);

				//if doesnt exist in url table - create url

				if(null == sourceURLDB){

					try{
						//TODO: need to check with a lot of the assumptions made when creating this URL - no backlinks no social monitoring.. etc... 

						sourceURLDB = createNewURL(sourceURLAddress, usersIDFromDB, currentLink.getCampaignID(), currentLink.getDate(), currentLink.isSocialData(), currentLink.isBackLinks(), GoFetchConstants.URL_ENTERED_BY_USER, false);

						urlDBService.createURL(sourceURLDB);

					}catch(Exception e){

						//TODO: what to do here???  - cancel the whole process - set success to false and the next level, set check this - if false - return with error message
						//newReport += ". Problem creating: " + sourceURLAddress + " This link will not be entered into GoFetch";
						//success = false;

					}


				} 
				//if(success){ // now have target and source url....

				// check that the two do not already exist as together as a link.

				Integer targetID = urlDBService.getURLIDFromAddress(targetURLAddress);

				Integer sourceID = urlDBService.getURLIDFromAddress(sourceURLAddress);

				Link linkDB = linkDBService.getLink(sourceID, targetID, currentLink.getAnchorText());

				// check if link already exists....

				if(null == linkDB){  // link does exist in table - create

					linkDB =createNewLink(targetID, sourceID, currentLink.getAnchorText(), currentLink.getDate(), targetURLAddress, usersIDFromDB, currentLink.getCampaignID(), currentLink.getLinkActivityKey(), GoFetchConstants.URL_ENTERED_BY_USER );

					linkDBService.createLink(linkDB, true); 

				}else{ // link already in table...

					errorReport += "Target URL: "+ targetURLAddress 
							+ "Source URL: " + sourceURLAddress + "Link already exists in DB.";

				}

			} // end of: if(!currentLink.getAnchorText().isEmpty())

		} // end of: for(ManualLink currentLink: links) - processing each link...


		//
		///////////////

		// 3. return a growl message ?? - with summary using errorReport and successReport..

		// if using excel sheet upload - have different, summary, than for uploading manual links...

		// 3.a Summary shows: no of new urls, new domains, how many links are to
		// each..???

		if(!multipleLinks){
			FacesMessage msg = new FacesMessage("New Link Added Successfully: " +  successReport);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			successReport = "";
		}


		links.clear();
		links.add(new ManualLink());

		return null;

	}

	private Link createNewLink(Integer targetID, Integer sourceID,
			String anchorText, Date date, String targetURLAddress,
			Integer usersIDFromDB, Integer campaignID, Integer linkBuildingKey, Integer dataEnteredBy) {

		Link link = new Link();

		link.setAnchor_text(anchorText);
		link.setClient_category_id(campaignID);
		link.setData_entered_by(dataEnteredBy);
		link.setDate_detected(date);
		
		link.setFinal_target_url_id(targetID);
		
		link.setSource_id(sourceID);
		link.setTarget_id(targetID);
		link.setUsers_user_id(usersIDFromDB);
		link.setLink_building_activity_id(linkBuildingKey);

		return link;

	}

	private URL createNewURL(String targetURLAddress, Integer usersIDFromDB, Integer categoryFromDB, Date date, boolean getSocialdata, boolean getBacklinks, Integer dataEnteredBy, boolean clientTargetURL) {

		URL url = new URL();

		Date todaysDate = DateUtil.getTodaysDate();
		url.setDate(date);
		url.setUrl_address(targetURLAddress);
		url.setUsers_user_id(usersIDFromDB);
		url.setClient_category_id(categoryFromDB);
		url.setData_entered_by(dataEnteredBy); 

		//TODO: need to check this.....
		url.setGet_backlinks(getBacklinks);
		url.setGet_social_data(getSocialdata);

		url.setSocial_data_date(todaysDate);
		url.setSocial_data_freq(GoFetchConstants.DAILY_FREQ);
		url.setClient_target_url(clientTargetURL);

		return url;
	}

	private Integer getUsersIDFromDB(String clientName) {

		UserDBService UserDBService = new UserDBService();

		List<User> clients = UserDBService.getUserByDisplayedName(clientName);

		if(null == clients )
			return 0;

		if(clients.isEmpty())
			return 0;

		return(clients.get(0).getId());


	}


	public void handleFileUpload(FileUploadEvent event) {

		// parse the data from the xls file here and add to links table in the
		// form....

		ExcelWrapper excel = new ExcelWrapper();
		uploadingExcelLinks = true;

		// user will not be able to select a client - this will be got from WorkBook
		//clients.clear(); - not seeing this in the form... 

		try {

			links = excel.SheetToLinksArray(event, linkActivity, campaigns);

			//need to update selectedClientsKey

			// fills the client name with teh client key eg: "1"
			// selectedClientsKey = getClientsKeyFromName(links.get(0).getClient());

			selectedClientsKey = links.get(0).getClient();

		} catch (Exception e) {

			FacesMessage msg = new FacesMessage("Error uploading ", event
					.getFile().getFileName() + ". Error: " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, msg);

			return;	
		}

		FacesMessage msg = new FacesMessage("Succesful", event.getFile()
				.getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);

		//TODO: here...  need to map each link's linkactivity to the correct key to update the drop down list...
		//can remove this??? campaigns are set already???
		//		for(ManualLink link : links){
		//
		//			Set<Entry<String, Integer>> entry = linkActivity.entrySet();
		//
		//			String entrySetString = entry.toString();
		//
		//
		//		}

		/*		from above method: upDateLinkBuildingActivity

		int currentTab = links.size();
		currentTab--;


		// //////TODO: move this to a static util method.
		// get key from value: see:
		// http://stackoverflow.com/questions/10655349/jsf-2-fselectitems-with-map-does-not-display-itemlabel
		for (Entry<String, Integer> entry : linkActivity.entrySet()) {
			String value = null;
			String key = String.valueOf(entry.getKey());

			if (key.equalsIgnoreCase(LinkReportConstants.contentCreationText)) {
				value = String.valueOf(entry.getValue());

				if (links.get(currentTab).getLinkActivity()
						.equalsIgnoreCase(value)) {
					links.get(currentTab).setContentCreation(true);
				} else {
					links.get(currentTab).setContentCreation(false);
				}

			}

		}
		 */



	}




	@PostConstruct
	public void fillClientsList() {

		linkBean.fillClientsList();

		//15-5-13 - moved to get get adresses like - with maxresults... limit etc which is called when needed..
//		if (urlAddressesInDB.isEmpty()) {
//			URLDBService urlDB = new URLDBService();
//			urlAddressesInDB = urlDB.getURLAddresses();
//		}

		// //////
		// TODO: put all Link Activity fetching in this method.. and called above:

		// links = new ArrayList<ManualLink>();
		//
		// clients = new LinkedHashMap<String,Integer>();
		// campaigns = new LinkedHashMap<String,Integer>();
		// targetURLs = new LinkedHashMap<String,Integer>();
		// linkActivity = new LinkedHashMap<String,Integer>();

		///////////////////////////////
		//Moved to EnterLinkBean - fillClientList
		//	UserDBService userDB = new UserDBService();
		//	LinkBuildingActivityDBService linkActivityDB = new LinkBuildingActivityDBService();
		//
		//	List<User> clientsDB = userDB.getAllClients();
		//	List<LinkBuildingActivity> linkActivitiesDB = linkActivityDB
		//			.getAllLinkActivities();
		//
		//	for (User user : clientsDB) {
		//		clients.put(user.getDisplayed_name(), user.getId());
		//	}
		//
		//	for (LinkBuildingActivity linkActivityLocal : linkActivitiesDB) {
		//
		//		linkActivity.put(linkActivityLocal.getActivity(),
		//				linkActivityLocal.getId());
		//	}
		//
		//	ManualLink manualLink = new ManualLink();
		//
		//	manualLink.setTargetUrl(LinkReportConstants.targetURLPlaceHolderText);
		//	manualLink.setCampaign(LinkReportConstants.campaignPlaceHolderText);
		//
		//	links.add(manualLink);
		//
		//	selectedClientsKey = LinkReportConstants.clientPlaceHolderText;

		//	
		//////////	
		//campaign = "Optionally select a campaign from the drop down list above";

		// couldnt get the active tab updater to work.....
		//		accordianTabs.put("0",1);	
		//		//activeTabIndex = accordianTabs.get("0");
		//		secondActiveTabIndex = activeTabIndex= 0; // initially we only have one tab open - so need to copy this to prevent error

	}
	//
	//public void processSelectedClient() {
	//
	//	// TODO: make sure only one client is selected: / make it a select one
	//	// drop down list....
	//	// nothing is selected in the drop down menu...
	//	if (selectedClients.isEmpty()) {
	//		campaigns.clear();
	//		targetURLs.clear();
	//		selectedClientsKey = LinkReportConstants.clientPlaceHolderText;
	//
	//		return;
	//	}
	//
	//	user_id = getSelectedClientInteger();
	//
	//	filterCampaigns(user_id);
	//
	//	// at this step, when user has altered the client selected, get all the	
	//	// client's campaigns and all targetURLs
	//	// filterTargetURLs(clientSelected);
	//	getClientsTargetURLs(user_id);
	//
	//	//update the client label in accordion:	
	//
	//	for (Entry<String, Integer> entry : clients.entrySet()) {
	//
	//		String value = String.valueOf(entry.getValue());
	//
	//		if (selectedClients.get(0).equals(value)) {
	//			String key = entry.getKey();
	//			selectedClientsKey = key;
	//
	//		}
	//	}
	//
	//}
	//
	//private String getClientsKeyFromName(String clientName){
	//
	//	String keyReturn = "";
	//
	//	for (Entry<String, Integer> entry : clients.entrySet()) {
	//
	//		String value = entry.getKey();
	//
	//		if (clientName.equals(value)) {
	//			keyReturn = String.valueOf(entry.getValue());
	//			//return keyReturn;
	//		}
	//	}
	//
	//	return keyReturn;
	//}
	//
	//private void getClientsTargetURLs(Integer clientSelected) {
	//
	//	// TODO: here,
	//	// first: select * client_category where user_id = clientSelected
	//	// 2nd: pass the list of Categories to urldbservice
	//	// then: make repeated calls to select * from url where
	//	// url.user_catefory.... think we need to use
	//	// client_category_users_users_id here.....
	//	// TODO: refactor entry of new target urls, to include the writing of
	//	// the client id AND client category id....
	//
	//	URLDBService URLDBService = new URLDBService();
	//
	//	List<URL> clientsTargetURLs = URLDBService
	//			.getClientsTargetURLs(clientSelected, true);
	//
	//	if (null == clientsTargetURLs) {
	//		targetURLs.clear();
	//		return;
	//	}
	//
	//	if (clientsTargetURLs.isEmpty()) {
	//		targetURLs.clear();
	//		return;
	//	}
	//
	//	for (URL url : clientsTargetURLs) {
	//
	//		targetURLs.put(url.getUrl_address(), url.getId());
	//
	//	}
	//
	//}
	//
	//public void upDateLinkBuildingActivity() {
	//
	//	// ///////////////////////
	//	// - check if we user has selected Content Creation as Link Activity
	//	// option - update link if so..
	//
	//	// //////TODO: move this to a static util method.
	//	// get key from value: see:
	//	// http://stackoverflow.com/questions/10655349/jsf-2-fselectitems-with-map-does-not-display-itemlabel
	//	for (Entry<String, Integer> entry : linkActivity.entrySet()) {
	//		String value = null;
	//		String key = String.valueOf(entry.getKey());
	//
	//		if (key.equalsIgnoreCase(LinkReportConstants.contentCreationText)) {
	//			value = String.valueOf(entry.getValue());
	//
	//			Integer intValue = entry.getValue();
	//			// couldnt get the active tab updater to work.....
	//			//String currentTabId = currentTab;
	//
	//			//new:
	//			if (intValue.equals(links.get(activeTabIndex).getLinkActivityKey())) {
	//				links.get(activeTabIndex).setContentCreation(true);
	//			} else {
	//				links.get(activeTabIndex).setContentCreation(false);
	//				//bug fix: no 6: when preceding tab was content creation and user changes current link to non-content creation
	//				//	current link remains as: getBackinks == true and getSocialData = true.
	//				links.get(activeTabIndex).setBackLinks(false);
	//				links.get(activeTabIndex).setSocialData(true);
	//			}
	//			return;
	//			// old
	//			//				if (links.get(currentTab).getLinkActivity()
	//			//						.equalsIgnoreCase(value)) {
	//			//					links.get(currentTab).setContentCreation(true);
	//			//				} else {
	//			//					links.get(currentTab).setContentCreation(false);
	//			//				}
	//
	//		}
	//
	//	}
	//	//
	//	// ////////////////////////
	//
	//}

	// general ajax listener for all elements on add new link form
	//public void upDateLinkData() {
	//	// TODO: this is called when user enters new data - so add..
	//	// ajax checking.... and parse data - present user with error msg here?
	//	// hide . show monitor this backlink url if link activity is content
	//	// creation...
	//
	//	// check if current tab's link activity is Content Creation...
	//
	//}
	//
	//public void addNewLinkTab() {
	//
	//	// get the preceding manualLink's data and copy into new manualLink and thus tab....
	//	Integer listSize = links.size();
	//	Integer lastEntry = --listSize;
	//	// make sure that this is the last tab...
	//	if(!(activeTabIndex.equals(lastEntry)))
	//		return;
	//
	//	ManualLink manualLink = new ManualLink();
	//
	//
	//
	//	manualLink.setTargetUrl(links.get(lastEntry).getTargetUrl());
	//	manualLink.setLinkActivity(links.get(lastEntry).getLinkActivity());
	//	manualLink.setLinkActivityKey(links.get(lastEntry).getLinkActivityKey());
	//	manualLink.setDate(links.get(lastEntry).getDate());
	//	manualLink.setContentCreation(links.get(lastEntry).isContentCreation());
	//	manualLink.setBackLinks(links.get(lastEntry).isBackLinks());
	//	manualLink.setSocialData(links.get(lastEntry).isSocialData());
	//	manualLink.setCampaign(links.get(lastEntry).getCampaign()); 
	//	manualLink.setCampaignID(links.get(lastEntry).getCampaignID());
	//
	//
	//	// leave this out - so user has to enter the link specific details each time.
	//	// manualLink.setSourceUrl(links.get(lastEntry).getSourceUrl());
	//	//manualLink.setAnchorText(links.get(lastEntry).getAnchorText());
	//
	//	links.add(manualLink);
	//
	//	// and update tabs display.
	//
	//
	//
	//}
	//
	//private Integer getSelectedClientInteger() {
	//
	//	String clientSelectedString = String.valueOf(selectedClients.get(0));
	//	Integer clientSelected = Integer.valueOf(clientSelectedString);
	//
	//	return clientSelected;
	//
	//}
	//
	//private Integer getSelectedCampaignInteger(Integer index){
	//
	//	//TODO: check if selectedCampaigns is empty here - keep getting index out of bounds error
	//	if(selectedCampaigns.isEmpty())
	//		return 0;
	//
	//	String selectedCampaign = String.valueOf(selectedCampaigns.get(index));
	//	Integer campaignSelected = Integer.valueOf(selectedCampaign);
	//
	//	return campaignSelected;
	//
	//}
	//
	//private Integer getSelectedCampaignInteger() {
	//
	//	return getSelectedCampaignInteger(0);
	//
	//}

	//
	////called when user changes campaign from drop down list....
	//public void campaignSelectionChange() {
	//
	//	int linksCounter = links.size() -1;
	//
	//	// reset the list: 
	//	//TODO: if the campaigns list is not empty - means we have already made the hit to the DB - then keep a copy of the list in memory, and use this to repopulate...????
	//	//	repop... selected targetURLs from this list in memory and not hit DB every time user changes the campaign.... only when they change user???
	//	// 		so changin the user selection causes a refresh from the DB.
	//
	//	targetURLs.clear();
	//
	//	if (selectedCampaigns.isEmpty()) {
	//
	//		// fill the list with ALL target URls associated with this client.
	//		getClientsTargetURLs(getSelectedClientInteger());
	//
	//		//TODO: need to update links(currentlyActiveTab).setCampaign(...)....
	//		selectedCampaignKey = ""; // reset
	//
	//		// OLD CODE: - only updated last tab
	//		//links.get(linksCounter).setCampaign(selectedCampaignKey);
	//		//links.get(linksCounter).setCampaignID(getSelectedCampaignInteger());
	//
	//		// 11-1-13 - new code: - to update campaign in active tab - not just last tab
	//		links.get(activeTabIndex).setCampaign(selectedCampaignKey);
	//		links.get(activeTabIndex).setCampaignID(getSelectedCampaignInteger());
	//
	//		return;
	//	}
	//
	//	URLDBService URLDBService = new URLDBService();
	//
	//	List<URL> localTargetURLs = URLDBService
	//			.getURLsOfClientCategory(getSelectedCampaignInteger(), true);
	//
	//	if (null == localTargetURLs) {
	//		targetURLs.clear();
	//		return;
	//	}
	//
	//	if (localTargetURLs.isEmpty()) {
	//		targetURLs.clear();
	//		return;
	//	}
	//
	//	for (URL url : localTargetURLs) {
	//
	//		targetURLs.put(url.getUrl_address(), url.getId());
	//
	//	}
	//
	//	// update the most recent link entry's campaign field:
	//	selectedCampaignKey = ""; // reset in case there's no match....
	//
	//	for (Entry<String, Integer> entry : campaigns.entrySet()) {
	//
	//		String strValue = String.valueOf(entry.getValue());
	//		Integer intValue = entry.getValue();
	//		Integer selCamp = getSelectedCampaignInteger(linksCounter);
	//
	//
	//		if(selCamp.equals(intValue)){
	//			String key = entry.getKey();
	//			selectedCampaignKey = key;
	//			links.get(linksCounter).setCampaign(selectedCampaignKey); //TODO: change this to: activeTabIndex ??
	//			links.get(linksCounter).setCampaignID(intValue);
	//			return;
	//		}
	//
	//	}
	//
	//
	//
	//}
	//
	//private void filterCampaigns(Integer clientSelected) {
	//
	//	ClientCategoryDBService clientCategoryDBService = new ClientCategoryDBService();
	//
	//	List<ClientCategory> clientCategories = clientCategoryDBService
	//			.getClientsCategories(clientSelected);
	//
	//	if (null == clientCategories) {
	//		campaigns.clear();
	//		return;
	//	}
	//
	//	if (clientCategories.isEmpty()) {
	//		campaigns.clear();
	//		return;
	//	}
	//
	//	for (ClientCategory category : clientCategories) {
	//
	//		campaigns.put(category.getCategory(), category.getId());
	//
	//	}
	//
	//
	//}
	//
	//public void updateLinksTargetURL() {
	//
	//	if (selectedTargetURLs.isEmpty()) {
	//		links.get(0).setTargetUrl("");
	//		return;
	//	}
	//
	//	String selectURL = selectedTargetURLs.get(0);
	//
	//	ManualLink link = links.get(0);
	//
	//	// //////TODO: move this to a static util method.
	//	// get key from value: see:
	//	// http://stackoverflow.com/questions/10655349/jsf-2-fselectitems-with-map-does-not-display-itemlabel
	//	for (Entry<String, Integer> entry : targetURLs.entrySet()) {
	//		String value = String.valueOf(entry.getValue());
	//
	//		// if (0 == entry.getKey().compareTo(
	//		// GoFetchConstants.contentCreationText)) {
	//
	//		if (selectURL.equals(value)) {
	//			String key = entry.getKey();
	//			links.get(0).setTargetUrl(entry.getKey());
	//		}
	//	}
	//
	//}

	// //////
	// utility methods

	// TODO: change this to something faster like:
	// http://igoro.com/archive/efficient-auto-complete-with-a-ternary-search-tree/
	// also - just return the addresses from Target URLs - not all.... remove 1000s of irrelevant 
	//	backlink urls....

	public List<String> complete(String query) {
		
		urlDB = new URLDBService();
		
		resultsAutoComplete = urlDB.getURLAddressesStartingWith(query, 5);

		return resultsAutoComplete;
	}

}
