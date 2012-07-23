package com.gofetch.entities;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import com.gofetch.utils.DateUtil;
import com.google.appengine.api.rdbms.AppEngineDriver;

/**
 * Service class for CRUD operations on URL entity
 * @author alandonohoe
 *
 */

public class URLService {

	@PersistenceUnit(unitName="GoFetch")
	EntityManagerFactory emf;

	private static Logger logger = Logger.getLogger(URLService.class.getName());

	public void createURL(URL url){
		logger.info("Entering url[" + url.getUrl_address() + "]");

		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();

		try {

			///////
			// check to see if url already in table
			// TODO: make an efficient look up service here..... see:
			// http://stackoverflow.com/questions/558978/most-efficient-way-to-see-if-an-arraylist-contains-an-object-in-java


			if(!urlInDB(url)){

				mgr.getTransaction().begin();
				mgr.persist(url);
				mgr.getTransaction().commit();

			}else{
				//TODO: send error to the user with the offending url address....

				logger.info("url already exists in DB: "
						+ url.getUrl_address());
			}


		}catch(Exception e){
			String msg = "Exception thrown...";
			logger.logp(Level.SEVERE, "URLService", "createURL",msg ,e);

		}
		finally {
			mgr.close();
		}
		logger.info("Exiting createURL");
	}

	/**
	 * Gets a URL given an ID
	 * @param id
	 * @return URL
	 */
	public URL getURL(int id) {
		logger.info("Entering getURL[" + id + "]");
		URL result = null;

		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();

		try {
			result = mgr.find(URL.class, id);
		} finally {
			mgr.close();
		}
		if (result == null) {
			logger.warning("No URLs returned");
		}
		logger.info("Exiting getURL");
		return result;
	}


	/**
	 * Gets all Contacts
	 * @return List of Contact beans
	 */
	@SuppressWarnings("unchecked")
	public List<URL> getURLs() {
		logger.info("Entering getURLs");
		List<URL> result = null;

		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();

		try {
			result= mgr.createQuery("SELECT u FROM URL u").getResultList();
		} finally {
			mgr.close();
		}
		if (result == null) {
			logger.warning("No URLs returned");
		}
		logger.info("Exiting getURLs");
		return result;
	}

	/*
	 * Returns a list of URLs that were added to the database today.
	 */
	public List<URL> getTodaysURLs(){

		logger.info("Entering getTodaysURLs");
		List<URL> result = null;

		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();

		Date todaysDate = DateUtil.getTodaysDate();

		try {
			result = mgr.createQuery("SELECT u FROM URL u WHERE u.date = :todaysDate")
					.setParameter("todaysDate", todaysDate, TemporalType.DATE)
					.getResultList();
		} finally {
			mgr.close();
		}
		if (result == null) {
			logger.warning("No URLs returned");
		}
		logger.info("Exiting getTodaysURLs");
		return result;

	}

	/*
	 * Returns a list of URLs that were added to the database today.
	 */
	public List<URL> getYesterdaysURLs(){

		logger.info("Entering getYesterdaysURLs");
		List<URL> result = null;

		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();

		Date yesterdaysDate = DateUtil.getYesterDaysDate();

		try {
			result = mgr.createQuery("SELECT u FROM URL u WHERE u.date = :yesterdaysDate")
					.setParameter("yesterdaysDate", yesterdaysDate, TemporalType.DATE)
					.getResultList();

		} finally {
			mgr.close();
		}
		if (result == null) {
			logger.warning("No URLs returned");
		}
		logger.info("Exiting getYesterdaysURLs");
		return result;

	}

	/*
	 * Returns a list of URLs that were added between the 2 dates
	 */
	public List<URL> getURLsBetween(Date startDate, Date endDate){

		logger.info("Entering getURLsBetween");
		List<URL> result = null;

		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();

		try {
			result = mgr.createQuery("SELECT u FROM URL u WHERE u.date BETWEEN :startDate AND :endDate")
					.setParameter("startDate", startDate, TemporalType.DATE)
					.setParameter("endDate", endDate, TemporalType.DATE)
					.getResultList();

		} finally {
			mgr.close();
		}
		if (result == null) {
			logger.warning("No URLs returned");
		}
		logger.info("Exiting getURLsBetween");
		return result;

	}

	// TODO: make an efficient look up service here..... see:
	// http://stackoverflow.com/questions/558978/most-efficient-way-to-see-if-an-arraylist-contains-an-object-in-java
	private boolean urlInDB(URL urlCheck){

		List<URL> urlList = getURLs();

		for(URL urlCurrent: urlList){
			if(urlCurrent.getUrl_address().equals(urlCheck.getUrl_address()))
				return true;
		}

		return false;
	}

	// TODO: make an efficient look up service here..... see:
	// http://stackoverflow.com/questions/558978/most-efficient-way-to-see-if-an-arraylist-contains-an-object-in-java
	public boolean urlInDB(String urlAddress){

		List<URL> urlList = getURLs();

		for(URL urlCurrent: urlList){
			if(urlCurrent.getUrl_address().equals(urlAddress))
				return true;
		}

		return false;
	}
}
