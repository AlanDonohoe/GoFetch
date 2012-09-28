package com.gofetch.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gofetch.entities.LinkDBService;
import com.gofetch.entities.MiscSocialData;
import com.gofetch.entities.MiscSocialDataDBService;
import com.gofetch.entities.URL;
import com.gofetch.entities.URLDBService;
import com.gofetch.entities.URLPlusSocialData;
import com.gofetch.entities.URLTree;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;

/**
 * Servlet implementation class GetSocialData
 */

public class GetSocialData extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetSocialData() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// this works........
		GetSocialDataHelper helper = new GetSocialDataHelper(request, response);
		helper.doGet();
		



		/* moved all this into GetSocialDataHelper.doGet() ...
		
		response.setContentType("application/json");
		// Get the printwriter object from response to write the required json object to the output stream      
		PrintWriter out = response.getWriter();
 		
		String targetURLAddress = request.getParameter("url");
		String startDate = request.getParameter("startdate"); 
		String endDate = request.getParameter("enddate"); 
		
		if(null == targetURLAddress){
			out.print("Error: No URL specified");
			out.flush();
			
			return;
			
		}
			
		//if startdate or enddate empty, omit from request... so all social data for url will be returned.
		
		// used to access the DB:
		URLDBService urlDB = new URLDBService();
		LinkDBService linkDB = new LinkDBService();
		MiscSocialDataDBService socialDataDB = new MiscSocialDataDBService();
		
		URL url;
		List<MiscSocialData> socialData;
		URLPlusSocialData urlPlusSocialData = new URLPlusSocialData();
		
		List <URL> backLinks;
		List<Integer> backLinkIDs;
		
		//TODO: make this 1 call with a join??
		//1 get root node url:   
		url = urlDB.getURL(targetURLAddress);
		
		if(null == url){
			out.print("Error: URL not in GoFetch");
			out.flush();
			
			return;
			
		}
		
		//2. get its social data:
		socialData = socialDataDB.getAllSocialData(url.getId());
		
		urlPlusSocialData.setUrl(url);
		urlPlusSocialData.setSocialDataList(socialData);
		
		// now add url and social data list to root node. - this is the target url of this node....
		URLTree tree = new URLTree(urlPlusSocialData);
		
		// think this has to be moved to a recursive function...
		// getBacklinks if > 0, then add new nested url plus social data for each
		// and call get getBacklinks
		
		GetBackLinks(tree);
		
		backLinkIDs = linkDB.getURLIDsPointingTo(url);
		backLinks = urlDB.getURLsFromIDs(backLinkIDs);
	

		//TODO:
		// so now have backlinks need to loop through and create URLAndSocialData for each backlink and place into node
		// use a recursive method here to run through the backlinks of the backlinks????
		//	then check that we have the same number of backlinks as total urls pointing to: finaltargeturl
		/*
		 * URLNode = 	URLPlusSocialData url; //set already...
	List<URLPlusSocialData> backLinks = null; // still to be set......
	
	
	OR: maybe there's no nodes - just a url plus socialdata, with nested urlandsocialdata(s) = backlinks
	
	OR: each node is 1 urlplussocialdata, with an array of url_ids(integer) that point to it.
		 
		//urlNode.getBackLinks().add(.....)
		
		
		http://www.json.org/js.html
		
		var myJSONObject = {"bindings": [
        {"ircEvent": "PRIVMSG", "method": "newURI", "regex": "^http://.*"},
        {"ircEvent": "PRIVMSG", "method": "deleteURI", "regex": "^delete.*"},
        {"ircEvent": "PRIVMSG", "method": "randomURI", "regex": "^random.*"}
    ]
};

In this example, an object is created containing a single member "bindings", which contains an array containing three objects, each containing "ircEvent", "method", and "regex" members.
		
		so i'd need:
		url = {
					{"url": "http://www.bbc.co.uk/",
					"social_data":[
					{"date":"", "stumble_upon":"",  "delicious":"",  "pinterest":"",  "linkedin":"",  "google_plus":"",  "twitter":"", "fb_total":"","fb_like":"","fb_comment":"","fb_share":"", "fb_click":"","fb_comment_box":"",}
					]
						{"url":"http://www.backlink1.com",
						"social_data":[
						{"date":"", "stumble_upon":"",  "delicious":"",  "pinterest":"",  "linkedin":"",  "google_plus":"",  "twitter":"", "fb_total":"","fb_like":"","fb_comment":"","fb_share":"", "fb_click":"","fb_comment_box":"",}
						] }
						
			};
		*/
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet( request,  response);
	}
	
	

}
