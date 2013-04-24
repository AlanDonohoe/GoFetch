package com.gofetch.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gofetch.entities.LinkDBService;
import com.gofetch.entities.MiscSocialData;
import com.gofetch.entities.MiscSocialDataDBService;
import com.gofetch.entities.URL;
import com.gofetch.entities.URLDBService;
import com.gofetch.entities.URLNode;
import com.gofetch.entities.URLPlusSocialData;
import com.gofetch.entities.URLTree;
import com.gofetch.utils.DateUtil;

/*
 * Class performs all the work of the GetSocialData servlet - but allows us to have thread-safe member variables
 * - inlcuding those needed to access the Db.
 */
public class GetSocialDataHelper {

	private static Logger log = Logger.getLogger(GetSocialDataHelper.class.getName());

	private HttpServletRequest request; 
	private HttpServletResponse response;

	// used to access the DB:
	private URLDBService urlDB;
	private LinkDBService linkDB;
	private MiscSocialDataDBService socialDataDB;
	private Date dateStart;
	private Date dateEnd;

	public GetSocialDataHelper(HttpServletRequest request, HttpServletResponse response){
		this.request = request;
		this.response = response;

		urlDB = new URLDBService();
		linkDB = new LinkDBService();
		socialDataDB = new MiscSocialDataDBService();

		try { // set start date as early date as reasonably poss, in case user doesnt set date- then get all data
			dateStart = DateUtil.getDateFromString("2000-01-01");
			dateEnd = DateUtil.getTodaysDate();
		} catch (ParseException e) {
			String msg = "Exception with system default date data. ";

			log.warning(msg + e.getMessage());
		}
	}


	protected void doGet() throws ServletException, IOException {


		response.setContentType("application/json");
		// Get the printwriter object from response to write the required json object to the output stream      
		PrintWriter out = response.getWriter();

		String targetURLAddress = request.getParameter("url");
		String startDate = request.getParameter("startdate"); 
		String endDate = request.getParameter("enddate"); 

		//////////////////////////////////
		//TODO: put this url validation stuff somewhere tidy...
		if(null == targetURLAddress){
			out.print("Error: No URL specified");
			out.flush();

			return;
		}


		if(targetURLAddress.isEmpty()){
			out.print("Error: No URL specified");
			out.flush();

			return;
		}


		if(targetURLAddress.length() < 10){
			out.print("Error: " + targetURLAddress + ": not long enough to be a valid URL");
			out.flush();

			return;
		}
		/////////////////

		//if startdate or enddate empty, omit from request... so all social data for url will be returned.

		URL url;
		List<MiscSocialData> socialData;
		URLPlusSocialData urlPlusSocialData = new URLPlusSocialData();

		//TODO: make this 1 call with a join??
		//1 get root node url:   
		url = urlDB.getURL(targetURLAddress);

		if(null == url){
			out.print("Error: " + targetURLAddress + " : not in GoFetch");
			out.flush();

			return;

		}

		//2. get its social data:
		//  if there's no end date then end date = today.
		// 	if there's no start date then start date = all data.

		if(null != startDate){
			try {
				dateStart = DateUtil.getDateFromString(startDate);

			} catch (ParseException e) {
				String msg = "Exception with user submitted start date . ";

				log.warning(msg + e.getMessage());
			}
		}

		if(null != endDate){
			try {
				dateEnd = DateUtil.getDateFromString(endDate);

			} catch (ParseException e) {

				String msg = "Exception with user submitted end date. ";

				log.warning(msg + e.getMessage());
			}
		}


		socialData = socialDataDB.getSocialDataBetween(url.getId(), dateStart, dateEnd);


		urlPlusSocialData.setUrl(url);
		urlPlusSocialData.setSocialDataList(socialData);

		/////////////////////////////////////
		// now add url and social data list to root node. - this is the target url of this node....
		URLTree tree = new URLTree(urlPlusSocialData);

		
		GetBackLinks(tree.getRoot());

		//output as JSON:
		String jsonTree = TreeToJson(tree);

		out.print(jsonTree);

		out.flush();

	}

	private String TreeToJson(URLTree tree){

		String jsonTree = "{"; // start of tree object

		jsonTree += printNodeAndChildren(tree.getRoot());

		jsonTree += "}"; // end of the tree object....

		return jsonTree;
	}

	/*
	 *  prints out current node and then recursively prints out children 
	 */
	private String printNodeAndChildren(URLNode node){

		String jsonNode;// = "{"; // start of this URL object

		//write out this current nodes data:
		jsonNode = node.getUrlPlusSocialData().toString();

		List<URLNode> children = node.getChildren();
		
		if(!children.isEmpty()){
			jsonNode += ",";

			jsonNode += "\"backlinks\": [";

			for(int i = 0; i < children.size(); i++){
			//for(URLNode childNode : children){
				// new nested child URL here
				// get its data

				jsonNode += "{";

				// now get its children's data...
				//jsonNode += printNodeAndChildren(childNode);
				jsonNode += printNodeAndChildren(children.get(i));

				jsonNode += "}"; // end of URL finish with } 
				
				// add comma apart from at end of list:
				if(i < (children.size() -1)){
					jsonNode += ",";
				}
			}

			jsonNode += "]"; //end of backlinks
		}

		return jsonNode;

	}

	/*
	 * recursively calls the DB for children of current node until we are at a leaf node.
	 */
	private void GetBackLinks(URLNode node){

		List<URL> backLinks;
		List<Integer> backLinkIDs;

		backLinkIDs = linkDB.getURLIDsPointingTo(node.getUrlPlusSocialData().getUrl());

		if(!backLinkIDs.isEmpty()){
			backLinks = urlDB.getURLsFromIDs(backLinkIDs);

			// for each backlink, get social data and then add as a new node pointing to this as the parent...
			for(URL backLink : backLinks){

				try{

					URLPlusSocialData urlPlusSocialData = new URLPlusSocialData();
					URLNode childNode;

					//get social data:							//  socialDataDB.getAllSocialData(backLink.getId());
					List<MiscSocialData> backLinkSocialData = socialDataDB.getSocialDataBetween(backLink.getId(), dateStart, dateEnd);

					urlPlusSocialData.setUrl(backLink);
					urlPlusSocialData.setSocialDataList(backLinkSocialData);

					childNode = node.addChild(urlPlusSocialData);

					// now for each child node created recursively fill up the tree
					GetBackLinks(childNode);
					
				}catch(Exception e){

					String msg = "Exception in GetBackLinks.";
					
					if(null != backLink )
					{
						msg += " Backlink Address: " + backLink.getUrl_address();
						msg += " Backlink ID: " + backLink.getId() + " ";

					}

					log.warning(msg + e.getMessage());
				}
			}
		}
	}
}
