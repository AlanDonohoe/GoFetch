package com.gofetch.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;

import com.gofetch.GoFetchConstants;
import com.gofetch.LinkReportConstants;
import com.gofetch.entities.ClientCategory;
import com.gofetch.entities.ClientCategoryDBService;
import com.gofetch.entities.Link;
import com.gofetch.entities.LinkBuildingActivity;
import com.gofetch.entities.LinkBuildingActivityDBService;
import com.gofetch.entities.LinkDBService;
import com.gofetch.entities.URL;
import com.gofetch.entities.URLDBService;
import com.gofetch.entities.User;
import com.gofetch.entities.UserDBService;
import com.gofetch.fileio.ExcelWrapper;
import com.gofetch.utils.DateUtil;
import com.gofetch.utils.TextUtil;

@ManagedBean
@ViewScoped
public class EnterLinkBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(EnterURLBean.class
			.getName());

	// ////
	// dynamic list of links
	private List<ManualLink> links;

	private Integer user_id;
	private String targetURL;
	private String campaign;

	private boolean uploadingExcelLinks;

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

	private Integer activeTabIndex;
	
	
//	// this are to get the params that will be passed if this page is called from the chrome plug in
//	HttpServletResponse res = null;
//	HttpServletRequest req = null;


	public EnterLinkBean(){

		uploadingExcelLinks = false;

		links = new ArrayList<ManualLink>();

		clients = new LinkedHashMap<String, Integer>();
		campaigns = new LinkedHashMap<String, Integer>();
		targetURLs = new LinkedHashMap<String, Integer>();
		linkActivity = new LinkedHashMap<String, Integer>();

		selectedClientsKey = "";
		selectedCampaignKey = "";

		activeTabIndex = 0;
		
//		req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
//		res = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
	}

	private void ResetClientCampaignAndT_URL(){

		if(null != selectedCampaigns)
			selectedCampaigns.clear();
		
		if(null != selectedClients)
			selectedClients.clear();
		
		if(null != selectedTargetURLs)
			selectedTargetURLs.clear();

		campaigns.clear();
		targetURLs.clear();

		activeTabIndex = 0;

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
		
		boolean success = true; // at the moment, when we hit a problem - cancel the whole process then. though report to user that x no of links have been proccessed and what they need to do to fix this one to continue...
		boolean multipleLinks;
		Integer usersIDFromDB;
		Integer noOfCurrentLink= 0;
		Integer noOfLinks = links.size();
		Integer noOfLinksToCheck;
		String sourceURLAddress = "";
		String targetURLAddress = "";
		boolean failure = false;

		String successReport = "";
		String errorReport = "";
		
		/////////////////
		// amend reporting if theres multiple links - and used to validate more than one links' data
		if(noOfLinks > 2) 
			multipleLinks = true;
		else
			multipleLinks = false;
		
		
		///////////
		// basic validation - check that user has selected: client, link activity, target url, backlink url, first link has anchor text.
		//	can leave blank - campaign
		
//		selectedClientsKey = LinkReportConstants.clientPlaceHolderText;
//		manualLink.setTargetUrl(LinkReportConstants.targetURLPlaceHolderText);
//		manualLink.setCampaign(LinkReportConstants.campaignPlaceHolderText);
		
		if(!uploadingExcelLinks){
			
			//case where user hasn't entered any anchor text and just pressed Add Links - will need to give feedback to user
			if(1 == noOfLinks)
				noOfLinksToCheck = 2;
			else
				noOfLinksToCheck = noOfLinks;
			
			for(int i = 0; i < (noOfLinksToCheck -1); i++){

				// 1. check client is not null:
				//if(null == links.get(i).getClient()){
				//selectedClientsKey ;//= LinkReportConstants.clientPlaceHolderText;
				if(LinkReportConstants.clientPlaceHolderText.contentEquals(selectedClientsKey)){

					Integer errorTab = i +1;
					errorReport = ("Missing client in tab number " + errorTab);
					FacesMessage msg = new FacesMessage(errorReport);
					FacesContext.getCurrentInstance().addMessage(null, msg);
					return null; 

				}
				
				//2: link activity:
				if(null == links.get(i).getLinkActivityKey()){
					
					Integer errorTab = i +1;
					errorReport = ("Missing Link Activity in tab number " + errorTab);
					FacesMessage msg = new FacesMessage(errorReport);
					FacesContext.getCurrentInstance().addMessage(null, msg);
					return null; 
				
				}
				
				// 3: target url
				if(LinkReportConstants.targetURLPlaceHolderText.contentEquals(links.get(i).getTargetUrl())){
					
					Integer errorTab = i +1;
					errorReport = ("Missing Target URL in tab number " + errorTab);
					FacesMessage msg = new FacesMessage(errorReport);
					FacesContext.getCurrentInstance().addMessage(null, msg);
					return null; 
				
				}
				
				//4. backlink url
				if(links.get(i).getSourceUrl().isEmpty()){
					
					Integer errorTab = i +1;
					errorReport = ("Missing Backlink URL in tab number " + errorTab);
					FacesMessage msg = new FacesMessage(errorReport);
					FacesContext.getCurrentInstance().addMessage(null, msg);
					return null; 
				
				}
				
				// 5. anchor text - in any tab other than the last tab:
				if(links.get(i).getAnchorText().isEmpty()){

					Integer errorTab = i +1;
					errorReport = ("Missing Anchor Text in tab number " + errorTab);
					FacesMessage msg = new FacesMessage(errorReport);
					FacesContext.getCurrentInstance().addMessage(null, msg);
					return null; 

				}
				
				// 6. if there's no campaign selected - assign to client's default:
				if(LinkReportConstants.campaignPlaceHolderText.contentEquals(links.get(i).getCampaign())){
					
					//selectedClientsKey - ??
					ClientCategoryDBService clientCatDB = new ClientCategoryDBService();
					ClientCategory clientDefaultCat = null;
							
					clientDefaultCat = clientCatDB.getClientsDefaultCategory(user_id);
					
					if(null != clientDefaultCat){
						links.get(i).setCampaignID(clientDefaultCat.getId());
						links.get(i).setCampaign(clientDefaultCat.getCategory());
					}
					
					else{
						

						Integer errorTab = i +1;
						errorReport = ("Problem with Client's Campaign in tab number " + errorTab);
						FacesMessage msg = new FacesMessage(errorReport);
						FacesContext.getCurrentInstance().addMessage(null, msg);
						return null; 
					}
				}
			}
		}

		//////////////////
		if(uploadingExcelLinks)
			usersIDFromDB = getUsersIDFromDB(links.get(0).getClient());
		else
			usersIDFromDB = user_id;

		// check user is in the Db as a client...

		if(null == usersIDFromDB)
			usersIDFromDB = 0;

		if(0 == usersIDFromDB){

			errorReport = ("Client not in GoFetch. Please add this client and then add their links.");
			FacesMessage msg = new FacesMessage(errorReport);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null; 
		}
		
		//When uploading links from excel spreadsheet - there isnt a campaignID for each link... 
		if(uploadingExcelLinks){
			
			filterCampaigns(usersIDFromDB); // this will fill campaigns member data with client's categories
			
			// use above line to fill campaigns - replaces two lines below.....
//			ClientCategoryDBService clientCategoryDBSevice = new ClientCategoryDBService();			
//			List<ClientCategory> clientsCategories = clientCategoryDBSevice.getClientsCategories(usersIDFromDB);
			
			/*
			 * some duplication here - tidy this up:
			 * need to get currentlink's campaign = string and set currentlinks campaignID using the string to get correct ID....
			 * 
			 * 	for (Entry<String, Integer> entry : campaigns.entrySet()) {

			String strValue = String.valueOf(entry.getValue());
			Integer intValue = entry.getValue();
			//old code:
			//Integer selCamp = getSelectedCampaignInteger(linksCounter);
			//new code: 18-1-13:
			Integer selCamp = getSelectedCampaignInteger();
			// or??
			// Integer selCamp = getSelectedCampaignInteger(activeTabIndex);  ??


			if(selCamp.equals(intValue)){
				String key = entry.getKey();
				selectedCampaignKey = key;
				links.get(activeTabIndex).setCampaign(selectedCampaignKey);
				links.get(activeTabIndex).setCampaignID(intValue);
				return;
			}

		}
			 */
		}

		// main loop for parsing each link
		for(ManualLink currentLink: links){

			//*1: ignore any links with no backlink URL - this will be the last link entry tab - and means we done processing...
			if(!currentLink.getAnchorText().isEmpty()){

				noOfCurrentLink++;	

				targetURLAddress = currentLink.getTargetUrl();
				sourceURLAddress = currentLink.getSourceUrl();
				URL targetURLDB, sourceURLDB;
				
				//replace: remove trailing slash and remove any WS:
				targetURLAddress = TextUtil.standardiseURL(targetURLAddress);
				sourceURLAddress = TextUtil.standardiseURL(sourceURLAddress);
				
				
				//Integer usersIDFromDB;
				//success = true;

				//TODO: check this default... assuming all urls entered as part of links ought to be monitored for social data...
				// no leave it to user..
				//currentLink.setSocialData(true);

				// *2: get target url from url table:
				targetURLDB = urlDBService.getURL(targetURLAddress);

				// if null try with slash:
				if(null == targetURLDB){
					targetURLDB = urlDBService.getURL(TextUtil.addSlashToEndOfString(targetURLAddress));
				}
				
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

				// now work on source URL:
				
				sourceURLDB = urlDBService.getURL(sourceURLAddress);
				
				// if null try with slash:
				if(null == sourceURLDB){
					sourceURLDB = urlDBService.getURL(TextUtil.addSlashToEndOfString(sourceURLAddress));
				}

				//if doesnt exist in url table - create url

				if(null == sourceURLDB){

					//successReport += "New Source URL: " + sourceURLAddress;

					try{
						//TODO: need to check with a lot of the assumptions made when creating this URL - no backlinks no social monitoring.. etc... 
																																// old: currentLink.isSocialData()
						sourceURLDB = createNewURL(sourceURLAddress, usersIDFromDB, currentLink.getCampaignID(), currentLink.getDate(), true, currentLink.isBackLinks(), GoFetchConstants.URL_ENTERED_BY_USER, false);

						urlDBService.createURL(sourceURLDB);

					}catch(Exception e){

						//TODO: what to do here???  - cancel the whole process - set success to false and the next level, set check this - if false - return with error message
						//newReport += ". Problem creating: " + sourceURLAddress + " This link will not be entered into GoFetch";
						//success = false;

					}


				} else{ // source URL DOES already exist....
					
					boolean sourceURLUpdated = false;

					if(0==sourceURLDB.getUsers_user_id()){
						sourceURLDB.setUsers_user_id(usersIDFromDB);
						sourceURLUpdated= true;
					}

					if(0 == sourceURLDB.getClient_category_id()){
						sourceURLDB.setClient_category_id(currentLink.getCampaignID());
						sourceURLUpdated = true;
					}

					if(sourceURLUpdated){
						urlDBService.updateURLClientAndCampaign(sourceURLDB);
					}
					
				}
				
				
				// now have target and source url....
				// check that the two do not already exist as together as a link.
				//TODO: what if the target or source URL address are present in DB, but with a slash????
				Integer targetID = urlDBService.getURLIDFromAddress(targetURLAddress);

				Integer sourceID = urlDBService.getURLIDFromAddress(sourceURLAddress);
				
				// 29-1-13: changed this to get link that is uniquely differentiated from other links by all three: source, target and anchor text
				Link linkDB = linkDBService.getLink(sourceID, targetID, currentLink.getAnchorText());

				// check if link already exists....

				if(null == linkDB){  // link does exist in table - create

					linkDB =createNewLink(targetID, sourceID, currentLink.getAnchorText(), currentLink.getDate(), targetURLAddress, usersIDFromDB, currentLink.getCampaignID(), currentLink.getLinkActivityKey(), GoFetchConstants.URL_ENTERED_BY_USER );

					linkDBService.createLink(linkDB, true); 

				}else{ // link already in table...

					errorReport += "Target URL: "+ targetURLAddress 
							+ "Source URL: " + sourceURLAddress + " Link already exists in DB.";
					
					FacesMessage msg = new FacesMessage(errorReport);
					FacesContext.getCurrentInstance().addMessage(null, msg);
					successReport = "";

				}

			} // end of: if(!currentLink.getAnchorText().isEmpty())

		} // end of: for(ManualLink currentLink: links) - processing each link...


		//
		///////////////

		// 3. return a growl message - with summary using errorReport and successReport..
		// if using excel sheet upload - have different, summary, than for uploading manual links...

		// 3.a Summary shows: no of new urls, new domains, how many links are to each..???
		if(!multipleLinks){
			FacesMessage msg = new FacesMessage(sourceURLAddress + " Added to " + selectedClientsKey + " Link Profile");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			successReport = "";
		}

		//TODO: bug here - this is causing null pointer exception....
		ResetClientCampaignAndT_URL();
		
		if(uploadingExcelLinks){
			
			Integer linksCounter = links.size() -1;
			String successMsg = linksCounter.toString() + " links added to " + selectedClientsKey + " Link Profile";
			FacesMessage msg = new FacesMessage(successMsg);
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			// reset flag:
			uploadingExcelLinks = false;
		}

		links.clear();

		ManualLink manualLink = new ManualLink();

		selectedClientsKey = LinkReportConstants.clientPlaceHolderText;
		manualLink.setTargetUrl(LinkReportConstants.targetURLPlaceHolderText);
		manualLink.setCampaign(LinkReportConstants.campaignPlaceHolderText);

		links.add(manualLink);
		
		

		return null;

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


	private URL createNewURL(String targetURLAddress, Integer usersIDFromDB, Integer categoryFromDB, Date date, boolean getSocialdata, boolean getBacklinks, Integer dataEnteredBy, boolean clientTargetURL) {

		URL url = new URL();

		Date todaysDate = DateUtil.getTodaysDate();
		url.setDate(date);
		url.setUrl_address(targetURLAddress);
		url.setUsers_user_id(usersIDFromDB);
		//url.setClient_category_users_user_id(usersIDFromDB); // - removing this from URL and the url table...and index...
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


	private Link createNewLink(Integer targetID, Integer sourceID,
			String anchorText, Date date, String targetURLAddress,
			Integer usersIDFromDB, Integer campaignID, Integer linkBuildingKey, Integer dataEnteredBy) {

		Link link = new Link();

		link.setAnchor_text(anchorText);
		link.setClient_category_id(campaignID);
		link.setData_entered_by(dataEnteredBy);
		link.setDate_detected(date);
		
		//6 - 2- 13: legacy code - replace with subsequent line
		//link.setFinal_target_url(targetURLAddress);
		link.setFinal_target_url_id(targetID);
		
		link.setSource_id(sourceID);
		link.setTarget_id(targetID);
		link.setUsers_user_id(usersIDFromDB);
		//link.setClient_category_users_user_id(usersIDFromDB);
		link.setLink_building_activity_id(linkBuildingKey);
		

		return link;

	}


	public void fillClientsList() {

		//Strings for holding the params that may have been passed to the page this bean is backing -
		//	these will be from the chrome plugin
		
		String targetURL, sourceURL, anchorText, clientName;
		Boolean gotParams = false;

		UserDBService userDB = new UserDBService();
		LinkBuildingActivityDBService linkActivityDB = new LinkBuildingActivityDBService();

		List<User> clientsDB = userDB.getAllClients();
		List<LinkBuildingActivity> linkActivitiesDB = linkActivityDB
				.getAllLinkActivities();

		for (User user : clientsDB) {
			clients.put(user.getDisplayed_name(), user.getId());
		}

		for (LinkBuildingActivity linkActivityLocal : linkActivitiesDB) {

			linkActivity.put(linkActivityLocal.getActivity(),
					linkActivityLocal.getId());
		}

		ManualLink manualLink = new ManualLink();

		////////////////////
		//TODO: check here if we have been passed some params - ie: this has been called from the chrome plug in
		//	use these to fill target & source URLs and get client's name from clients list:
		
		// targetURL, sourceURL, anchorText;
		targetURL = getPassedParameter("target");
		sourceURL = getPassedParameter("source");
		anchorText = getPassedParameter("anchor");
		
		
		if(null!= targetURL){
			gotParams = true;
			
			//TODO: strip client name from domain and check if there's a client in DB that matches
			clientName = getClientFromDomainText(targetURL);
			
		}
		
		
		

		//
		////////////

		if(gotParams){
			manualLink.setTargetUrl(targetURL);
			manualLink.setSourceUrl(sourceURL);
			manualLink.setAnchorText(anchorText);
			
			//TODO: how to set client's: name, ID / key, etc..???
		}
		else
			manualLink.setTargetUrl(LinkReportConstants.targetURLPlaceHolderText);
		
		
		manualLink.setCampaign(LinkReportConstants.campaignPlaceHolderText);

		links.add(manualLink);
		
		

 		selectedClientsKey = LinkReportConstants.clientPlaceHolderText;
		//campaign = "Optionally select a campaign from the drop down list above";

		// couldnt get the active tab updater to work.....
		//		accordianTabs.put("0",1);	
		//		//activeTabIndex = accordianTabs.get("0");
		//		secondActiveTabIndex = activeTabIndex= 0; // initially we only have one tab open - so need to copy this to prevent error

	}


	private String getClientFromDomainText(String targetURL) {
		// TODO Auto-generated method stub
		return null;
	}

	public void filterSelectedClientsCampaignsAndT_URLs() {

		// TODO: make sure only one client is selected: / make it a select one
		// drop down list....
		// nothing is selected in the drop down menu...
		if (selectedClients.isEmpty()) {
			campaigns.clear();
			targetURLs.clear();
			selectedClientsKey = LinkReportConstants.clientPlaceHolderText;

			return;
		}

		user_id = getSelectedClientInteger();

		filterCampaigns(user_id);

		// at this step, when user has altered the client selected, get all the	
		// client's campaigns and all targetURLs
		// filterTargetURLs(clientSelected);
		getClientsTargetURLs(user_id);

		//update the client label in accordion:	

		for (Entry<String, Integer> entry : clients.entrySet()) {

			String value = String.valueOf(entry.getValue());

			if (selectedClients.get(0).equals(value)) {
				String key = entry.getKey();
				selectedClientsKey = key;

			}
		}

	}


	private Integer getSelectedClientInteger() {

		String clientSelectedString = String.valueOf(selectedClients.get(0));
		Integer clientSelected = Integer.valueOf(clientSelectedString);

		return clientSelected;

	}


	private void filterCampaigns(Integer clientSelected) {

		ClientCategoryDBService clientCategoryDBService = new ClientCategoryDBService();

		List<ClientCategory> clientCategories = clientCategoryDBService
				.getClientsCategories(clientSelected);

		if (null == clientCategories) {
			campaigns.clear();
			return;
		}

		if (clientCategories.isEmpty()) {
			campaigns.clear();
			return;
		}

		for (ClientCategory category : clientCategories) {

			campaigns.put(category.getCategory(), category.getId());

		}
	}


	private void getClientsTargetURLs(Integer clientSelected) {

		// TODO: 
		// first: select * client_category where user_id = clientSelected
		// 2nd: pass the list of Categories to urldbservice
		// then: make repeated calls to select * from url where
		// url.user_catefory.... think we need to use
		// client_category_users_users_id here.....
		// TODO: refactor entry of new target urls, to include the writing of
		// the client id AND client category id....

		URLDBService URLDBService = new URLDBService();

		List<URL> clientsTargetURLs = URLDBService
				.getClientsTargetURLs(clientSelected, true);

		if (null == clientsTargetURLs) {
			targetURLs.clear();
			return;
		}

		if (clientsTargetURLs.isEmpty()) {
			targetURLs.clear();
			return;
		}

		for (URL url : clientsTargetURLs) {

			targetURLs.put(url.getUrl_address(), url.getId());

		}

	}


	//called when user changes campaign from drop down list....
	public void campaignSelectionChange() {

		

		// reset the list: 
		//TODO: if the campaigns list is not empty - means we have already made the hit to the DB - then keep a copy of the list in memory, and use this to repopulate...????
		//	repop... selected targetURLs from this list in memory and not hit DB every time user changes the campaign.... only when they change user???
		// 		so changin the user selection causes a refresh from the DB.

		targetURLs.clear();

		if (selectedCampaigns.isEmpty()) {
			
			selectedTargetURLs.clear();

			// fill the list with ALL target URls associated with this client.
			getClientsTargetURLs(getSelectedClientInteger());

			//TODO: need to update links(currentlyActiveTab).setCampaign(...)....
			selectedCampaignKey = ""; // reset

			// OLD CODE: - only updated last tab
			//links.get(linksCounter).setCampaign(selectedCampaignKey);
			//links.get(linksCounter).setCampaignID(getSelectedCampaignInteger());

			// 11-1-13 - new code: - to update campaign in active tab - not just last tab
			links.get(activeTabIndex).setCampaign(selectedCampaignKey);
			links.get(activeTabIndex).setCampaignID(getSelectedCampaignInteger());
			
			// 28-1-13: and clear current link's target URL.
			links.get(activeTabIndex).setTargetUrl(LinkReportConstants.targetURLPlaceHolderText);
			links.get(activeTabIndex).setCampaign(LinkReportConstants.campaignPlaceHolderText);

			return;
		}

		URLDBService URLDBService = new URLDBService();

		List<URL> localTargetURLs = URLDBService
				.getURLsOfClientCategory(getSelectedCampaignInteger(), true);

		if (null == localTargetURLs) {
			targetURLs.clear();
			return;
		}

		if (localTargetURLs.isEmpty()) {
			targetURLs.clear();
			return;
		}

		for (URL url : localTargetURLs) {

			targetURLs.put(url.getUrl_address(), url.getId());

		}

		// update the most recent link entry's campaign field:
		selectedCampaignKey = ""; // reset in case there's no match....

		for (Entry<String, Integer> entry : campaigns.entrySet()) {

			String strValue = String.valueOf(entry.getValue());
			Integer intValue = entry.getValue();
			//old code:
			//Integer selCamp = getSelectedCampaignInteger(linksCounter);
			//new code: 18-1-13:
			Integer selCamp = getSelectedCampaignInteger();
			// or??
			// Integer selCamp = getSelectedCampaignInteger(activeTabIndex);  ??


			if(selCamp.equals(intValue)){
				String key = entry.getKey();
				selectedCampaignKey = key;
				links.get(activeTabIndex).setCampaign(selectedCampaignKey);
				links.get(activeTabIndex).setCampaignID(intValue);
				return;
			}

		}
	}


	private Integer getSelectedCampaignInteger(Integer index){

		//TODO: check if selectedCampaigns is empty here - keep getting index out of bounds error
		if(selectedCampaigns.isEmpty())
			return 0;

		String selectedCampaign = String.valueOf(selectedCampaigns.get(index));
		Integer campaignSelected = Integer.valueOf(selectedCampaign);

		return campaignSelected;

	}

	private Integer getSelectedCampaignInteger() {

		return getSelectedCampaignInteger(0);

	}



	public void updateLinksTargetURL() {

		if (selectedTargetURLs.isEmpty()) {
			links.get(activeTabIndex).setTargetUrl(LinkReportConstants.targetURLPlaceHolderText);
			return;
		}

		String selectURL = selectedTargetURLs.get(0);

		
		//ManualLink link = links.get(0);

		// //////TODO: move this to a static util method.
		// get key from value: see:
		// http://stackoverflow.com/questions/10655349/jsf-2-fselectitems-with-map-does-not-display-itemlabel
		for (Entry<String, Integer> entry : targetURLs.entrySet()) {
			String value = String.valueOf(entry.getValue());

			// if (0 == entry.getKey().compareTo(
			// GoFetchConstants.contentCreationText)) {

			if (selectURL.equals(value)) {
				String key = entry.getKey();
				links.get(activeTabIndex).setTargetUrl(entry.getKey());
			}
		}

	}



	public void upDateLinkBuildingActivity() {

		// ///////////////////////
		// - check if we user has selected Content Creation as Link Activity
		// option - update link if so..

		// //////TODO: move this to a static util method.
		// get key from value: see:
		// http://stackoverflow.com/questions/10655349/jsf-2-fselectitems-with-map-does-not-display-itemlabel
		for (Entry<String, Integer> entry : linkActivity.entrySet()) {
			String value = null;
			String key = String.valueOf(entry.getKey());

			if (key.equalsIgnoreCase(LinkReportConstants.contentCreationText)) {
				value = String.valueOf(entry.getValue());

				Integer intValue = entry.getValue();
				// couldnt get the active tab updater to work.....
				//String currentTabId = currentTab;

				//new:
				if (intValue.equals(links.get(activeTabIndex).getLinkActivityKey())) {
					links.get(activeTabIndex).setContentCreation(true);
				} else {
					links.get(activeTabIndex).setContentCreation(false);
					//bug fix: no 6: when preceding tab was content creation and user changes current link to non-content creation
					//	current link remains as: getBackinks == true and getSocialData = true.
					links.get(activeTabIndex).setBackLinks(false);
					links.get(activeTabIndex).setSocialData(true);
				}
				return;
				// old
				//				if (links.get(currentTab).getLinkActivity()
				//						.equalsIgnoreCase(value)) {
				//					links.get(currentTab).setContentCreation(true);
				//				} else {
				//					links.get(currentTab).setContentCreation(false);
				//				}

			}

		}
		//
		// ////////////////////////

	}


	//general ajax listener for all elements on add new link form
	public void upDateLinkData() {
		// TODO: this is called when user enters new data - so add.. ??
		// ajax checking.... and parse data - present user with error msg here?
		// hide . show monitor this backlink url if link activity is content
		// creation...

		// check if current tab's link activity is Content Creation...

	}


	public void addNewLinkTab() {

		// get the preceding manualLink's data and copy into new manualLink and thus tab....
		Integer listSize = links.size();
		Integer lastEntry = --listSize;
		// make sure that this is the last tab...
		if(!(activeTabIndex.equals(lastEntry)))
			return;

		ManualLink manualLink = new ManualLink();



		manualLink.setTargetUrl(links.get(lastEntry).getTargetUrl());
		//manualLink.setLinkActivity(links.get(lastEntry).getLinkActivity());
		manualLink.setLinkActivityKey(links.get(lastEntry).getLinkActivityKey());
		manualLink.setDate(links.get(lastEntry).getDate());
		manualLink.setContentCreation(links.get(lastEntry).isContentCreation());
		manualLink.setBackLinks(links.get(lastEntry).isBackLinks());
		manualLink.setSocialData(links.get(lastEntry).isSocialData());
		manualLink.setCampaign(links.get(lastEntry).getCampaign()); 
		manualLink.setCampaignID(links.get(lastEntry).getCampaignID());


		// leave this out - so user has to enter the link specific details each time.
		// manualLink.setSourceUrl(links.get(lastEntry).getSourceUrl());
		//manualLink.setAnchorText(links.get(lastEntry).getAnchorText());

		links.add(manualLink);

		// and update tabs display.
	}


	public void onTabChange(TabChangeEvent event){

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

	/*
	 * returns the value of the named parameter: name, 
	 * else returns null - if not found
	 */
	private String getPassedParameter(String name) {
		
		String value = null;
		
		HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		
		
		value = req.getParameter(name);
		
		return value;
		/*
		 * req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		res = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
		 */
		
//		FacesContext facesContext = FacesContext.getCurrentInstance();
//		this.passedParameter = (String) facesContext.getExternalContext()
//				.getRequestParameterMap().get("id");
//		return this.passedParameter;
		
	}

	////////////////////////////////////////////////////////
	// getters and setters:

	public List<ManualLink> getLinks() {
		return links;
	}

	public void setLinks(List<ManualLink> links) {
		this.links = links;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getTargetURL() {
		return targetURL;
	}

	public void setTargetURL(String targetURL) {
		this.targetURL = targetURL;
	}

	public String getCampaign() {
		return campaign;
	}

	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}

	public boolean isUploadingExcelLinks() {
		return uploadingExcelLinks;
	}

	public void setUploadingExcelLinks(boolean uploadingExcelLinks) {
		this.uploadingExcelLinks = uploadingExcelLinks;
	}

	public List<String> getSelectedClients() {
		return selectedClients;
	}

	public void setSelectedClients(List<String> selectedClients) {
		this.selectedClients = selectedClients;
	}

	public LinkedHashMap<String, Integer> getClients() {
		return clients;
	}

	public void setClients(LinkedHashMap<String, Integer> clients) {
		this.clients = clients;
	}

	public List<Integer> getSelectedCampaigns() {
		return selectedCampaigns;
	}

	public void setSelectedCampaigns(List<Integer> selectedCampaigns) {
		this.selectedCampaigns = selectedCampaigns;
	}

	public LinkedHashMap<String, Integer> getCampaigns() {
		return campaigns;
	}

	public void setCampaigns(LinkedHashMap<String, Integer> campaigns) {
		this.campaigns = campaigns;
	}

	public String getSelectedLinkActivity() {
		return selectedLinkActivity;
	}

	public void setSelectedLinkActivity(String selectedLinkActivity) {
		this.selectedLinkActivity = selectedLinkActivity;
	}

	public LinkedHashMap<String, Integer> getLinkActivity() {
		return linkActivity;
	}

	public void setLinkActivity(LinkedHashMap<String, Integer> linkActivity) {
		this.linkActivity = linkActivity;
	}

	public List<String> getSelectedTargetURLs() {
		return selectedTargetURLs;
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

	public Integer getLinkUploadProgress() {
		return linkUploadProgress;
	}

	public void setLinkUploadProgress(Integer linkUploadProgress) {
		this.linkUploadProgress = linkUploadProgress;
	}

	public Integer getActiveTabIndex() {
		return activeTabIndex;
	}

	public void setActiveTabIndex(Integer activeTabIndex) {
		this.activeTabIndex = activeTabIndex;
	}

}
