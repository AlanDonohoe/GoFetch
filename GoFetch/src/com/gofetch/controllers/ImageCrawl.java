package com.gofetch.controllers;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gofetch.entities.Image;
import com.gofetch.entities.ImageDBService;
import com.gofetch.entities.URL;
import com.gofetch.entities.URLDBService;
import com.gofetch.images.ImageConstants;
import com.gofetch.images.LinkPeekImageWrapper;
import com.gofetch.utils.DateUtil;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

/**
 * 
 * @author alandonohoe Servlet retreives all urls that have get_image > 0 from
 *         DB and uses LinkPeek service to get their webpage as an image This is
 *         then saved in the datastore and the handle of the datastore object is
 *         saved in image entry in the google cloud SQL table, with the id of
 *         the url associated with image also saved.
 */
public class ImageCrawl extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(ImageCrawl.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("text/plain");

		// Get all urls with get_image > 0;

		/*
		 * Linkpeek - actually hosts the images... so you can just point to the
		 * url it provides... and then save that img on your server if you
		 * want... after...
		 * 
		 * example code: http://linkpeek.com/api/v1?uri=[target]&apikey=[your
		 * key]&token=[md5(secret+uri+size)]&size=[Width x Height]
		 * 
		 * https://linkpeek.com/api/v1?uri=http%3A//lostquery.com&apikey=9fhvyH9KP
		 * &token=32122c5ab9fc5cb63381e84f58135fbc&size=300x150
		 * 
		 * use: size=original - to get full screen
		 */

		// testing:
		String imgURL;
		LinkPeekImageWrapper linkPeek = new LinkPeekImageWrapper();
		byte[] imageByteArry;
		Blob imageBlob;
		Image newImage;

		URLDBService urlDB = new URLDBService();
		ImageDBService imageDB = new ImageDBService();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		List<URL> urls = null;

		urls = urlDB.getURLsWithImage();

		// if there's none - just return:

		if (null == urls) {
			try {
				getServletContext().getRequestDispatcher("/index.jsf").forward(
						req, resp);
			} catch (ServletException e) {

				String msg = "Exception thrown in ImageCrawl \n";
				log.severe(msg + e.getMessage());

			}
		}

		for (URL url : urls) {

			try {

				imageByteArry = linkPeek.getURLsImage(url.getUrl_address(),
						ImageConstants.FULLPAGE_IMAGE);

				// Blob contains an array of bytes. This byte array can be no bigger than 1MB. To store files, particularly files larger than this 1MB limit, look at the Blobstore API.
				// see: https://developers.google.com/appengine/docs/java/blobstore/overview#Writing_Files_to_the_Blobstore
				//imageBlob = new Blob("",imageByteArry);
				
				// no longer writing the byte array to the sql table...
				//newImage = new Image("newImageV_1", imageBlob);
				newImage = new Image();
				
				newImage.setObject_id(url.getId());
				newImage.setDate(DateUtil.getTodaysDate());
				newImage.setVersion(1); // need a way of incrementing count of same images, like a static ref??? 
										// or need to cound the previous no of versions using SQL and then set this at that plus 1...
				newImage.setSize(ImageConstants.GET_FULLPAGE);
				
				// format the name string... format = id of object _ version number
				String imageName = Integer.toString(url.getId());
				imageName += "_";
				imageName += "1";
				newImage.setName(imageName);

				// save to datastore.... somehow.... and get key to save in
				// image table...
				Entity employee = new Entity("Employee");
				
				imageDB.createImage(newImage);

				// then if OK - clear get image flag on url...
				urlDB.updateURLImageGot(url.getId(),
						ImageConstants.GET_NO_IMAGE);

			} catch (Exception e1) {

				log.warning(e1.getMessage());

			}
		}

		try {
			getServletContext().getRequestDispatcher("/index.jsf").forward(req,
					resp);
		} catch (ServletException e) {

			String msg = "Exception thrown in ImageCrawl \n";
			log.severe(msg + e.getMessage());

		}
	}

}
