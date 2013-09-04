package com.gofetch.controllers;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gofetch.entities.*;

public class DeleteURL extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(DeleteURL.class.getName());


	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

//		Integer noTimesInLinkTable, noOfBackLinks;
//		List <Link> linksInTree = null;
//		List <Link> linksThisURLIsIn = null;
//		boolean deleteURLFlag = true;

//		URLDBService urlsDB = new URLDBService();
//		SEOMozDataDBService seoMozDB = new SEOMozDataDBService();
//		LinkDBService linksDB = new LinkDBService();
//		MiscSocialDataDBService socialDataDB = new MiscSocialDataDBService();

		///////
		// this is the code that actually does something... thses 2 lines
		String deleteURL = req.getParameter("url_to_delete");


		//Seems a bit dangerous.... comment out for now...
		//deleteTreeWithURLAsRoot(deleteURL);
		
		//
		//////////////////
//		
//		//check to see if url is in DB:
//		if(url != null){
//				
//			///////////
//			// new code 8-8-12:
//			
//			//1. get all links in the tree, that have the url we want to delete as their final target url - ie: all links in the tree with this (to be deleted)
//			//	url as the root.
//			linksInTree = linksDB.getLinksInTreePointingTo(url.getUrl_address());
//			
//			// 2. for every link in this particular tree...
//			for(Link link :linksInTree ){
//				
//				deleteURLFlag = true;
//
//				// 3. get the source url in the link
//				Integer sourceURLID = link.getSource_id();
//				
//				//4. check if the source url is in links table more than once (either as source or target)....
//				if(linksDB.noOfTimesURLIsLinkedTo(sourceURLID) > 1){
//				
//					// get all the links that this url is in:
//					linksThisURLIsIn = linksDB.getAllLinks(sourceURLID);
//					
//					// run through them, checking that the only final_target_url is our current one...
//					for(Link linkSub : linksThisURLIsIn){
//						// if there's a link, that has a final target url, that isnt the current target - then do NOT delete this url
//						if(!linkSub.getFinal_target_url().contentEquals(url.getUrl_address())){
//							deleteURLFlag = false;
//							//TODO: write to log info: current backlink is involved in more than just one link.. so can not delete url, social and seomoz, etc, data.
//							
//						}
//					}
//					
//				}else{
//					// else... source URL is only in the links table ONCE - so it can be safely deleted
//					deleteURLFlag = true;
//				}
//				if(deleteURLFlag){ 
//					
//					// delete url and associated data
//					socialDataDB.deleteSocialData(sourceURLID);
//					
//					seoMozDB.deleteSEOMozData(sourceURLID);
//					
//					urlsDB.deleteURL(sourceURLID);
//					
//				}
//			}
//			
//			// end of new code....
//			////////////
//
//			//////////
//			// OLD CODE - DELETE WHEN ABOVE NEW CODE IS TESTED:
//			
//			//get list of ID's of urls that point to this target:
//			List<Integer> backLinkIDList = linksDB.getSourceURLsIDsPointingTo(url);
//
//			noOfBackLinks = backLinkIDList.size();
//
//			linksDB.deleteLinksPointingTo(url.getId());
//
//			for(Integer backLinkID : backLinkIDList){
//				//check that each one only occurs once in links table (either as a target or source), if so then we can delete it and all its associated data...
//				noTimesInLinkTable = linksDB.noOfTimesURLIsLinkedTo(backLinkID);
//				// now all links pointing to the target have been deleted, this will be 0 unless url is in other links
//				if(noTimesInLinkTable > 0){
//
//					//delete social data...
//					socialDataDB.deleteSocialData(backLinkID);
//
//					// delete seomoz data...
//					seoMozDB.deleteSEOMozData(backLinkID);
//
//					// delete twitter_mention data - may not need this whole table anymore..
//
//					// delete url 
//					urlsDB.deleteURL(backLinkID);
//
//				}else{
//					//write to log info: current backlink is involved in more than just one link.. so can not delete url, social and seomoz, etc, data.
//				}
//
//
//			}
//			// end of checking each backlink  
//
//			// check that TARGET only occurs as a target or source in links table the same no of backlinks and if YES - then can delete...
//			//	this indicates that it is not a source url in any other links
//			noTimesInLinkTable = linksDB.noOfTimesURLIsLinkedTo(url.getId());
//
//			if(noTimesInLinkTable == noOfBackLinks){
//
//				//delete social data...
//				socialDataDB.deleteSocialData(url.getId());
//
//				// delete seomoz data...
//				seoMozDB.deleteSEOMozData(url.getId());
//
//				// delete url
//				urlsDB.deleteURL(url.getId());
//
//			}else{
//				//write to log info: current backlink is involved in more than just one link...
//				// ie: it is also a target in some other link relationship... 
//				// so can not delete url, social and seomoz, etc, data.
//			}
//			
//			//end of OLD CODE 
//			////////////////////
//
//		}else{ // alert user that url is not in DB...
//
//		}


		// finally return
		try {
			getServletContext().getRequestDispatcher("/index.jsf").forward(req,resp);

			return;
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		try {
			doPost(req, resp);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

	}
	
	/**
	 * Method deletes target url and all urls linked to this url, and all associated social, etc data, and all links in the links table.
	 * 
	 * If target url is:
	 * 
	 * ---------------
	 * 1) just a single URL with no entry in links table, but does have social data and SEOMoz data.
	 * 
	 * Then post-condition: 
	 * Target: Url entry in �url� table and all its associated social, SEOMoz data (in tables: misc_social_data, twitter_mention, seomoz_data) is deleted.
	 * 						
	 * ---------------
	 * 2) a target in DB, with the number of its occurrences in links table that equals the no of backlinks of this tree of links alone, 
	 * 		(that is, this is a target  URL solely for this tree of links, and does not occur as a target or source url in any where else in the links table).
	 * 	 	Also - every source url, occurs ONLY in this tree of links, once in the links table - and is not associated with any other tree of links.		
	 * 		Target url and source urls in tree have social, etc associated data.
	 * 
	 * Then Post-condition: 
	 * Target: URL plus associated social, etc data are deleted
	 * Source: all urls in this tree of links plus associated social, etc data are deleted from the �url� table, 
	 * Links: All entries with this target url as final target url in links table are deleted.
	 * 
	 * ---------------
	 * 
	 * 3) a target in DB, with the number of its occurrences in links table that is GREATER the no of backlinks of this tree of links alone, 
	 * 		- this implies that the target url is involved with more than one tree of links.
	 * Also - every source url, occurs ONLY in this tree of links, once in the links table - and is not associated with any other tree of links.		
	 * 		Target url and source urls in tree have social, etc associated data.
	 * 
	 * Then post-condition: 
	 * target is not deleted from the �url� table, nor is it's associated social, etc data, 
	 * source all urls in this tree of links plus associated social, etc data are deleted from the �url� table,
	 * Links: every link associated with this tree IS deleted from the link table.
	 * 
	 *  4) a target in DB, with the number of its occurrences in links table that EQUALS the no of backlinks of this tree of links alone, 
	 * 		(that is, this is a target  URL solely for this tree of links, and does not occur as a target or source url in any where else in the links table).
	 *  - some source urls, OCCUR IN MORE THAN JUST this tree of links, in the links table - THUS ARE associated with other trees of links.		
	 * 		Target url and source urls in tree have social, etc associated data.
	 * 
	 * Then post-condition: 
	 * target IS deleted from the �url� table, AND is it's associated social, etc data, 
	 * source: urls plus associated social, etc data, in this tree of links that DO occur more than once in links table are NOT deleted from the �url� table,
	 * 		   urls plus associated social, etc data, in this tree of links that DO NOT occur more than once in links table ARE deleted from the �url� table,
	 * Links: every link associated with this tree IS deleted from the link table.
	 * 
	 * @param urlTarget - url that sits at the root of the tree of links - ie: the final target url - to delete.
	 * @return
	 */
	public String deleteTreeWithURLAsRoot(String urlAddress){
		
		String report = null;
		Integer noTimesInLinkTable, noOfBackLinks;
		List <Link> linksInTree = null;
		List <Link> linksThisURLIsIn = null;
		boolean deleteURLFlag = true;
		
		URLDBService urlsDB = new URLDBService();
		SEOMozDataDBService seoMozDB = new SEOMozDataDBService();
		LinkDBService linksDB = new LinkDBService();
		MiscSocialDataDBService socialDataDB = new MiscSocialDataDBService();

		URL url =  urlsDB.getURL(urlAddress);	
		//check to see if url is in DB:
				if(url != null){
					
					//A: Source URLs
					
					//1. get all links in the tree, that have the url we want to delete as their final target url - ie: all links in the tree with this (to be deleted)
					//	url as the root.
					linksInTree = linksDB.getLinksInTreePointingTo(url.getUrl_address());
					
					noOfBackLinks = linksInTree.size();
					
					// 2. for every link in this particular tree...
					for(Link link :linksInTree ){
						
						deleteURLFlag = true;

						// 3. get the source url in the link
						Integer sourceURLID = link.getSource_id();
						
						//4. check if the source url is in links table more than once (either as source or target)....
						if(linksDB.noOfTimesURLIsLinkedTo(sourceURLID) > 1){
						
							// get all the links that this url is in:
							linksThisURLIsIn = linksDB.getAllLinks(sourceURLID);
							
							// run through them, checking that the only final_target_url is our current one...
							for(Link linkSub : linksThisURLIsIn){
								// if there's a link, that has a final target url, that isnt the current target - then do NOT delete this url
								//OLD: if(!linkSub.getFinal_target_url().contentEquals(url.getUrl_address())){
								if(linkSub.getFinal_target_url_id() != url.getId()){
								 	deleteURLFlag = false;
									//TODO: write to report: current backlink is involved in more than just one link.. so can not delete url, social and seomoz, etc, data.
									
								}
							}
							
						}else{
							// else... source URL is only in the links table ONCE - so it can be safely deleted
							deleteURLFlag = true;
						}
						if(deleteURLFlag){ 
							
							// delete url and associated data
							deleteURLAndAllData(sourceURLID, urlsDB, socialDataDB, seoMozDB);		
							
							//TODO: write to report
						}
					}
					
					// B: Target URL:
					// check that TARGET only occurs as a target or source in links table the same no of backlinks and if YES - then can delete...
					//	this indicates that it is not a source url in any other links
					// OLD CODE: noTimesInLinkTable = linksDB.noOfTimesURLIsLinkedTo(url.getId());
					
					// OLD CODE: if(noTimesInLinkTable == noOfBackLinks){

					//TODO: replace aboce old code logic with check that:
					 	if(0 == linksDB.noOfTimesURLIsSourceURL(url.getId())){// - if NOT a source URL in any links - then can delete

						// delete url and associated data
						deleteURLAndAllData(url.getId(), urlsDB, socialDataDB, seoMozDB);

					}else{
						//TODO: write to report: current url target is involved in more than just one link...
						// ie: it is also a target in some other link relationship... 
						// so can not delete url, social and seomoz, etc, data.
					}

					
					// C: Links:
					linksDB.deleteLinksInTreeWithRoot(url.getUrl_address());
					
				}else{ 
					//TODO: write to report // alert user that url is not in DB...
				}
		
		
		return report;
	}
	
	private void deleteURLAndAllData(int urlID, URLDBService urlsDB, MiscSocialDataDBService socialDataDB, SEOMozDataDBService seoMozDB){
		
		urlsDB.deleteURL(urlID);

		socialDataDB.deleteSocialData(urlID);
		
		seoMozDB.deleteSEOMozData(urlID);

	}


}
