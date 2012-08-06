package com.gofetch.controllers;

import com.gofetch.GoFetchConstants;
import com.gofetch.beans.GoFetchRequestBean;
import com.gofetch.entities.*;
import com.gofetch.seomoz.Constants;
import com.gofetch.utils.DateUtil;

import com.google.appengine.api.rdbms.AppEngineDriver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
		
		URLDBService urls = new URLDBService();

		//////////
		// get data from page and fill bean....
		GoFetchRequestBean goFetchBean = new GoFetchRequestBean();

		goFetchBean.setUrl(req.getParameter("target_url"));

		// initial error checking, in case JS is disabled in browser:
		if((goFetchBean.getUrl().length() < GoFetchConstants.MIN_VALID_URL_LENGTH) || 
				(!goFetchBean.getUrl().startsWith("http://"))){

			//TODO: add feedback to user here in case of error...

			try {
				getServletContext().getRequestDispatcher("/index.html").forward(req,resp);

				return;
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		
		///////////////////////
		// check that url not already in DB....
		if(urls.urlInDB(goFetchBean.getUrl())){

			//TODO: add feedback to user here...
			try {
				getServletContext().getRequestDispatcher("/index.html").forward(req,resp);

				return;
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//
		////////////////////////////

		//////////////////////
		// url entered is valid and does not already exist in the DB - continue with retrieving rest of data...
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

		/////////////
		// write url to DB.
		URL url = new URL();

		url.setDate(DateUtil.getTodaysDate());
		url.setUrl_address(goFetchBean.getUrl());
		url.setUser_id(goFetchBean.getUser_id());

		url.setGet_backlinks(goFetchBean.isBackLinkData());
		url.setGet_fb_data(goFetchBean.isFacebookData());
		url.setGet_twitter_data(goFetchBean.isTwitterData());

		////////
		// testing SEOMoz data....
		//		SEOMozData seoMoz = new SEOMozData();
		//		
		//		seoMoz.setDa(100);
		//		seoMoz.setPa(50);
		//		
		//		url.setSeoMozObject(seoMoz);

		/*
		 * Exception here:
		 * 
		 * Internal Exception: java.sql.SQLException: Field 'url_id' doesn't have a default value
Error Code: 1364
Call: INSERT INTO seomoz_data (SEOMOZ_ID, AUDITOR_ID, AUDITOR_RANK, COMMENT, DA, LAST_QUESTION, PA, RGA_SCORE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
	bind => [8 parameters bound]

	- debug and check that url object is assigned an id on creation here?? or is it when passed to teh DB??
		 */

		//
		//////////////
		URLDBService urlDBUnit = new URLDBService();
		urlDBUnit.createURL(url);


		// end JPA persistence...
		///////////////


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

}
