package com.gofetch.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gofetch.entities.URL;
import com.gofetch.entities.URLDBService;
import com.gofetch.utils.DateUtil;

import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

/**
 * 
 * @author alandonohoe
 * This	doGet is called as a cron job to pull all of today's URLs that are to have their social data crawled
 * and then divide them into smaller tasks to be processed on the task queue, via the controller: SocialCrawlTask
 */
public class SocialCrawlTaskProducer extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(SocialCrawlTaskProducer.class.getName());


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		log.info("Entering doGet");

		resp.setContentType("text/plain");

		List<URL> urls = null; // urls back from DB to be socially crawled
		List<List<URL>> urlSubsets = new ArrayList<List<URL>>(); // list of url lists, each of which will be used per task
		URLDBService urlDBUnit = null;
		int noOfTasks, noOfURLs, indexStart, indexEnd;
		int noOfURLsPerTask = 120; // this should fit into the 10 min time we have for each task to complete  
									// also anything > 200 urls causes a invalid URL exception 
		Queue queue = QueueFactory.getQueue("SocialCrawlQueue");
		
		TaskOptions taskOptions;

		try{

			// URL database communication
			urlDBUnit = new URLDBService();

			// returns all urls to be crawled ordered by url_id
			urls = urlDBUnit.getAllTodaysSocialCrawlURLs();

			if(null ==urls){
				log.warning("urlDBUnit.getAllTodaysSocialCrawlURLs(): null == urls");
				return;
			}

			noOfURLs = urls.size();

			if(0 ==noOfURLs){
				log.info("No URLs to socially crawl today");
				return;
			}

			// create a new task per 250 urls
			noOfTasks = noOfURLs/ noOfURLsPerTask;

			// check that no of urls exactly fit into quota:
			if(0!=noOfURLs % noOfURLsPerTask){
				noOfTasks++; // add final task to hold the final <250 urls
			}

			log.info("noOfURLs: " + noOfURLs + " noOfURLsPerTask: " + noOfURLsPerTask + " noOfTasks: " + noOfTasks);

			// divide all urls into 190 long lists of urls
			for(int i  = 0; i < noOfTasks; i++){

				indexStart = (i  * noOfURLsPerTask); // 		= 0,  250, 500, etc...
				indexEnd = (((i + 1) * noOfURLsPerTask)); //    = 249,499, 749, etc...

				//check we;re not over the remainders and causing a NPE:
				if(indexEnd >= noOfURLs)
					indexEnd = noOfURLs; // this will be the final subset

				urlSubsets.add(urls.subList(indexStart, indexEnd));	
			}

			// now each subset ==  a task

			// for each task..
			for(int x = 0; x < urlSubsets.size(); x++){

				int firstURLid, lastURLid, subsetSize;
				String taskName;

				subsetSize = urlSubsets.get(x).size();
				
				// give task unique name: todays date and start and end url ids:
				firstURLid = urlSubsets.get(x).get(0).getId();
				lastURLid = urlSubsets.get(x).get((subsetSize -1)).getId();
				taskName = DateUtil.getTodaysDateAsDDMMYYYY() + "_" + String.valueOf(firstURLid)+ "_" +  String.valueOf(lastURLid);
				
				taskOptions = TaskOptions.Builder.withUrl("/socialcrawltask").taskName(taskName).method(Method.GET);

				// add all the urls' ids as params - to each task
				//for(int i = 0; i < subsetSize ; i++){ //TODO: 250 params - too long: throws invalid URL exception
				for(int i = 0; i < subsetSize ; i++){

					String name = "id_"+ i;
					String value = String.valueOf(urlSubsets.get(x).get(i).getId());
					taskOptions.param(name, value);
				}
				// finally, add the task to the queue
				queue.add(taskOptions);
			} // end of: for(List<URL> urlList : urlSubsets){

		}catch(Exception e){
			log.severe(e.getMessage());
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	/**
	 * 
	 * @param urls -  list of urls
	 * @return all the urls' id as param string (eg: id1=123&id2=456...etc)
	 */
	private String getURLidsAsParam(List<URL> urls){

		return "";
	}
	
//	private String getDD_MM_YYDate(Date date){
//		
//		String result;
//		result = String.valueOf(date.()) + String.valueOf(date.getMonth()) + String.valueOf(date.getYear());
//	}

}
