package com.gofetch.controllers;

//import java.io.IOException;
//import javax.servlet.ServletException;
//import javax.servlet.http.*;
//import com.gofetch.entities.URLService;
//import com.gofetch.seomoz.Constants;
//import com.gofetch.seomoz.SEOMozWrapper;
//import com.google.appengine.api.rdbms.AppEngineDriver;
//import com.google.cloud.sql.jdbc.Connection;

import com.gofetch.beans.GoFetchRequestBean;
import com.gofetch.entities.URL;
import com.gofetch.entities.URLService;


import com.google.appengine.api.rdbms.AppEngineDriver;
import java.util.logging.Logger;

import java.io.IOException;
import java.sql.*;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class GoFetchServlet extends HttpServlet {
	
	private static Logger logger = Logger.getLogger(GoFetchServlet.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setContentType("text/plain");
		
		//////////
		// get data from page and fill bean....
		GoFetchRequestBean goFetchBean = new GoFetchRequestBean();
		
		goFetchBean.setUrl(req.getParameter("target_url"));
		goFetchBean.setUser_id(req.getParameter("user_id"));
		
		String backlink_data = req.getParameter("backlink_data");
		String facebook_data = req.getParameter("facebook_data");
		String twitter_data =  req.getParameter("twitter_data");
		
		if(null == backlink_data){
			goFetchBean.setBackLinkData(false);
		} else{
			goFetchBean.setBackLinkData(true);
		} 
		
		if(null == facebook_data){
			goFetchBean.setFacebookData(false);
		} else{
			goFetchBean.setFacebookData(true);
		} 
		
		if(null == twitter_data){
			goFetchBean.setTwitterData(false);
		} else{
			goFetchBean.setTwitterData(true);
		} 
		//
		//////////////
		
		
		//TODO: check if url is already added to DB here....
		
		/////////////
		// version - using jdbc - not JPA
//		String urlAddress = goFetchBean.getUrl();
//		boolean getFBData = goFetchBean.isFacebookData(); 
//		boolean getTwitterData = goFetchBean.isTwitterData();
//		boolean getBackLinks = goFetchBean.isBackLinkData();
//		
//		Connection c = null;
//	    try {
//	      DriverManager.registerDriver(new AppEngineDriver());
//	      c = DriverManager.getConnection("jdbc:google:rdbms://kastle2gofetch:url/url");
//	      
//	      //String statement = "INSERT INTO keywords (keyword, search_volume, vertical) VAlUES ('trousers3010',1002,'alan')";   
//      String statement ="INSERT INTO url (url_address, get_fb_data, get_twitter_data, get_backlinks) VALUES(?,?,?,?)";
//      
//	      PreparedStatement stmt = c.prepareStatement(statement);
//	     
//	      stmt.setString (1, urlAddress);
//	      stmt.setBoolean(2, getFBData);
//	      stmt.setBoolean(3, getTwitterData);
//	      stmt.setBoolean(4, getBackLinks);
//	      
//	      int success;
//	      success = stmt.executeUpdate();
//	      stmt.close();
//	      if(1 == success){
//	    	  logger.info(urlAddress + " added successfully");
//	      }else{
//	    	  logger.info(urlAddress + " FAILED to be added to url");
//	      }
//	      
//
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	    } finally {
//	        if (c != null) 
//	          try {
//	            c.close();
//	            } catch (SQLException ignore) {
//	         }
//	      } 
	    
		/////////////
		// JPA version - preferable....
		URL url = new URL();
		
		url.setUrl_address(goFetchBean.getUrl());
		url.setUser_id(goFetchBean.getUser_id());
		
		url.setGet_backlinks(goFetchBean.isBackLinkData());
		url.setGet_fb_Data(goFetchBean.isFacebookData());
		url.setGet_twitter_data(goFetchBean.isTwitterData());
		
		URLService urlDBUnit = new URLService();
		
		urlDBUnit.createURL(url);
		
	    // end JPA persistence...
	    ///////////////
	    
		//TODO: if yes, just return index page/ fwd to error page.
		
		
		//TODO: no - add url to DB.
		
		
		// set up the SEOMoz object...
		//SEOMozWrapper seoMoz = new SEOMozWrapper(Constants.ACCESS_ID, Constants.SECRET_KEY);
		
		/*
		 * until we pay for the API - set this true here - speeds up queries... 
         *   we will only ever get the top (by PA) 1000 links to any URL target...
         *   all SEOMoz API stuff, can be dealt with in quiet periods using backend/cron jobs...
		 */
		//seoMoz.usingSEOMozFreeAPI(true); 
	
		
		try {
			getServletContext().getRequestDispatcher("/index.html").forward(req,resp);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		doGet(req, resp); // just direct to doGet(...)
	}
//	
//	@ButtonMethod(buttonName="confirmButton")
//	public void testMethod(){
//		
//	}
//	


	
}
