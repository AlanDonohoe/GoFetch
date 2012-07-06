package com.gofetch;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

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
		
		//////////////
		// save the bean to the session object
			//req.getSession().setAttribute("dataBean", goFetchBean);
		//
		//////////////
		
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
