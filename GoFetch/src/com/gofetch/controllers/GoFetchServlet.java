package com.gofetch.controllers;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.gofetch.seomoz.Constants;
import com.gofetch.seomoz.SEOMozWrapper;

@SuppressWarnings("serial")
public class GoFetchServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		resp.setContentType("text/plain");
		
		//////////
		// get data from page and fill bean....
		GoFetchRequestBean goFetchBean = new GoFetchRequestBean();
		
		goFetchBean.setUrl(req.getParameter("target_url"));
		
		String backlink_data = req.getParameter("backlink_data");
		String facebook_data = req.getParameter("facebook_data");
		String twitter_data = req.getParameter("twitter_data");
		
		if("ON".equals(backlink_data)){
			goFetchBean.setBackLinkData(true);
		} else{
			goFetchBean.setBackLinkData(false);
		} 
		
		if("ON".equals(facebook_data)){
			goFetchBean.setFacebookData(true);
		} else{
			goFetchBean.setFacebookData(false);
		} 
		
		if("ON".equals(twitter_data)){
			goFetchBean.setTwitterData(true);
		} else{
			goFetchBean.setTwitterData(false);
		} 
		//
		//////////////
		
		
		//TODO: check if url is already added to DB here....
		// research...
		
		//TODO: if yes, just return index page/ fwd to error page.
		
		
		//TODO: no - add url to DB.
		
		
		// set up the SEOMoz object...
		SEOMozWrapper seoMoz = new SEOMozWrapper(Constants.ACCESS_ID, Constants.SECRET_KEY);
		
		/*
		 * until we pay for the API - set this true here - speeds up queries... 
         *   we will only ever get the top (by PA) 1000 links to any URL target...
		 */
		seoMoz.usingSEOMozFreeAPI(true); 
		
		
		
		
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
