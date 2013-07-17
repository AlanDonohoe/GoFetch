package com.gofetch.entities;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/*
 * 
 * @author alandonohoe
 * This is  a thread safe wrapper for the Entity Manager
 * see: http://stackoverflow.com/questions/7862700/best-practice-to-get-entitymanagerfactory
 * 
 * for the change in project settings, so that we can use Servlet 3.0 features such as WebListener,
 * see: http://stackoverflow.com/questions/7595797/cant-import-javax-servlet-annotation-webservlet
 * 
 * - however this doesn't work - seems that we can't get servlet 3.0 to work even when using the most recent gae api
 * 
 * So finally: used this approach:
 * http://javanotepad.blogspot.co.uk/2007/05/jpa-entitymanagerfactory-in-web.html
 * 
 */
public class PersistenceAppListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		PersistenceManager.getInstance().closeEntityManagerFactory();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		// this is called on start up - should we initialise entity manager here
		// - the once only....???
		// is actually 4 x....:

		/*
		 * 1. INFO: Successfully processed
		 * /Users/alandonohoe/Documents/EclipseWorkspace
		 * /GoFetch/GoFetch/war/WEB-INF/backends.xml May 3, 2013 9:24:15 AM
		 * com.google.apphosting.utils.jetty.JettyLogger info INFO: jetty-6.1.x
		 */

		/*
		 * 2 INFO: Successfully processed
		 * /Users/alandonohoe/Documents/EclipseWorkspace
		 * /GoFetch/GoFetch/war/WEB-INF/backends.xml May 3, 2013 9:22:48 AM
		 * com.google.apphosting.utils.jetty.JettyLogger info INFO: jetty-6.1.x
		 * ---> here.....
		 */

	}
}