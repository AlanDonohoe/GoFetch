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

public class URLDBService{

	@PersistenceUnit(unitName="GoFetch")
	EntityManagerFactory emf;

	private static Logger logger = Logger.getLogger(URLDBService.class.getName());
	//TODO: writing URLs to DB is v v slow - takes about 2 secs per record... why????
	//	maybe create a method: void createURLs(List<URL> listURLs) method and have a loop within that to include only: if(!urlInDB(url)){...persist.... }
	//	
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
		}catch(Exception e){
			String msg = "Exception thrown...";
			logger.logp(Level.SEVERE, "URLService", "getURL",msg ,e);

		} finally {
			mgr.close();
		}
		if (result == null) {
			logger.warning("URLDBService: getURL(int id) - No URLs returned, for id:" + id);
		}
		logger.info("Exiting getURL");
		return result;
	}


	/**
	 * Gets a URL given an ID
	 * @param id
	 * @return URL
	 */
	public URL getURL(String address) {
		logger.info("Entering getURL[" + address + "]");

		List<URL> url = null;

		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();

		try {
			url = (List<URL>) mgr.createQuery("SELECT u FROM URL u WHERE u.url_address = :address")
					.setParameter("address", address)
					.getResultList();
		}catch(Exception e){
			String msg = "Exception thrown getting URL: " + address;
			logger.logp(Level.SEVERE, "URLService", "getURL",msg ,e);

		} finally {
			mgr.close();
		}

		logger.info("Exiting getURL");
		
		if(null == url)
			return null;
		else
			return url.get(0);

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
		}catch(Exception e){
			String msg = "Exception thrown...";
			logger.logp(Level.SEVERE, "URLService", "getURLs",msg ,e);

		} finally {
			mgr.close();
		}
		if (result == null) {
			logger.warning("No URLs returned");
		}
		logger.info("Exiting getURLs");
		return result;
	}

	public List<URL> getURLs(Date date){

		logger.info("Entering getURLs");
		List<URL> result = null;

		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();

		try {
			result = mgr.createQuery("SELECT u FROM URL u WHERE u.date = :date")
					.setParameter("date", date, TemporalType.DATE)
					.getResultList();

		}catch(Exception e){
			String msg = "Exception thrown...";
			logger.logp(Level.SEVERE, "URLService", "getURLs",msg ,e);

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

		Date todaysDate = DateUtil.getTodaysDate();

		result = getURLs(todaysDate);

		logger.info("Exiting getTodaysURLs");

		return result;

	}


	/*
	 * Returns a list of URLs that were added to the database today.
	 */
	public List<URL> getTargetURLsFrom(Date date){

		logger.info("Entering getTargetURLsFrom");
		List<URL> result = null;

		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();

		try {
			result = mgr.createQuery("SELECT u FROM URL u WHERE u.date = :date AND u.get_backlinks = TRUE")
					.setParameter("date", date, TemporalType.DATE)
					.getResultList();

		}catch(Exception e){
			String msg = "Exception thrown...";
			logger.logp(Level.SEVERE, "URLService", "getTargetURLsFrom",msg ,e);

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
	 * Returns a list of URLs that were added to the database today.
	 */
	public List<URL> getYesterdaysURLs(){

		logger.info("Entering getYesterdaysURLs");
		List<URL> result = null;

		Date yestDate = DateUtil.getYesterDaysDate();

		result = getURLs(yestDate);

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

		}catch(Exception e){
			String msg = "Exception thrown...";
			logger.logp(Level.SEVERE, "URLService", "getURLsBetween",msg ,e);

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

	public Integer getURLIDFromAddress(String urlAddress){

		Integer url_id;
		List<URL> result = null;

		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();

		try {
			result = mgr.createQuery("SELECT u FROM URL u WHERE u.url_address = :urlAddress")
					.setParameter("urlAddress", urlAddress)
					.getResultList();
		}catch(Exception e){
			String msg = "Exception thrown...";
			logger.logp(Level.SEVERE, "URLService", "getURLIDFromAddress",msg ,e);

		}  finally {
			mgr.close();
		}
		if (result == null || result.isEmpty()) {
			return 0;
		}

		url_id = result.get(0).getId();

		return url_id;
	}

	public String getURLAddressFromID(Integer urlID){

		String address;
		List<URL> result = null;

		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();

		try {
			result = mgr.createQuery("SELECT u FROM URL u WHERE u.url_id = :urlID")
					.setParameter("urlID", urlID)
					.getResultList();
		}catch(Exception e){
			String msg = "Exception thrown...";
			logger.logp(Level.SEVERE, "URLService", "getURLIDFromAddress",msg ,e);

		}  finally {
			mgr.close();
		}
		if (result == null || result.isEmpty()) {
			return null;
		}

		address = result.get(0).getUrl_address();

		return address;
	}

	/**
	 * Updates record with url address with new page authority (pa) & domain authority (da) data
	 * 
	 * @param urlAddress - target url 
	 * @param pa - Page Authority
	 * @param da - Domain Authority
	 */
	public void updateURLAuthorityData(String urlAddress, Integer pa, Integer da){

		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		int result, url_id;

		try {

			url_id = getURLIDFromAddress(urlAddress);

			mgr.getTransaction().begin();

			URL url = mgr.find(URL.class, url_id);
			url.setPage_authority(pa);
			url.setDomain_authority(da);

			mgr.merge(url);
			mgr.getTransaction().commit();

		}catch(Exception e){
			String msg = "Exception thrown...";
			logger.logp(Level.SEVERE, "URLService", "updateURLAuthorityData",msg ,e);

		}  finally {
			mgr.close();
		}


	}

	/**
	 * Updates record with url address with new page authority (pa) & domain authority (da) data
	 * 
	 * @param urlAddress - target url 
	 * @param pa - Page Authority
	 * @param da - Domain Authority
	 * @param domainName - Domain name of url.
	 */
	public void updateURLData(String urlAddress, Integer pa, Integer da, String docTitle){

		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		int result, url_id;

		try {

			url_id = getURLIDFromAddress(urlAddress);

			mgr.getTransaction().begin();

			URL url = mgr.find(URL.class, url_id);
			url.setPage_authority(pa);
			url.setDomain_authority(da);
			url.setDoc_title(docTitle);

			mgr.merge(url);
			mgr.getTransaction().commit();

		}catch(Exception e){
			String msg = "Exception thrown...";
			logger.logp(Level.SEVERE, "URLService", "updateURLData",msg ,e);

		}  finally {
			mgr.close();
		}

	}

	public void updateDomainNames(List<URL> urls){

		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();

		try {

			for(URL currentURL : urls){

				mgr.getTransaction().begin();

				URL url = mgr.find(URL.class, currentURL.getId());
				url.setDomain(currentURL.getDomain());

				mgr.merge(url);
				mgr.getTransaction().commit();

			}

		}catch(Exception e){
			String msg = "Exception thrown...";
			logger.logp(Level.SEVERE, "URLService", "updateDomainNames",msg ,e);

		} finally {
			mgr.close();
		}

	}


	/*
	 * Returns a list of URLs that are to be monitored for social data...
	 */
	public List<URL> getSociallyTrackedURLs(){

		logger.info("Entering getSociallyTrackedURLs");
		List<URL> result = null;

		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();

		try {
			result = mgr.createQuery("SELECT u FROM URL u WHERE u.get_social_data = TRUE")
					.getResultList();

		}catch(Exception e){
			String msg = "Exception thrown...";
			logger.logp(Level.SEVERE, "URLService", "getSociallyTrackedURLs",msg ,e);

		} finally {
			mgr.close();
		}
		if (result == null) {
			logger.warning("No SociallyTracked URLs returned");
		}
		logger.info("Exiting getSociallyTrackedURLs");
		return result;

	}

//	/**
//	 * Method checks that urlAddress is in DB, and if it is, deletes that URL from the DB.
//	 * @param urlAddress string address of url to be deleted
//	 */
//	public void deleteURL(String urlAddress){
//
//		logger.info("Entering deleteURL. Deleting " + urlAddress);
//
//		URL url;
//
//		emf = Persistence.createEntityManagerFactory("GoFetch");
//		EntityManager mgr = emf.createEntityManager();
//
//		url = getURL(urlAddress);
//
//		if(url != null){
//
//			try{
//				mgr.getTransaction().begin();
//
//				mgr.createQuery("DELETE FROM URL u WHERE u.url_id = :url_id")
//				.setParameter("url_id", url_id)
//				.executeUpdate();
//
//				mgr.getTransaction().commit();
//
//			}catch(Exception e){
//				String msg = "Exception thrown deleting: " + urlAddress;
//				logger.logp(Level.SEVERE, "URLService", "deleteURL",msg ,e);
//
//			} finally {
//				mgr.close();
//			}
//
//		}
//		else{
//			logger.info(urlAddress + " Not in DB.");
//		}
//
//		logger.info("Exiting deleteURL.");
//
//	}
//

	public void deleteURL(int url_id){

		logger.info("Entering deleteURL. Deleting " + url_id);


		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();

			try{
				mgr.getTransaction().begin();

				mgr.createQuery("DELETE FROM URL u WHERE u.url_id = :url_id")
				.setParameter("url_id", url_id)
				.executeUpdate();

				mgr.getTransaction().commit();

			}catch(Exception e){
				String msg = "Exception thrown deleting: " + url_id;
				logger.logp(Level.SEVERE, "URLService", "deleteURL",msg ,e);

			} finally {
				mgr.close();
			}


		logger.info("Exiting deleteURL.");

	}
}
