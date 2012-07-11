package com.gofetch.entities;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

/**
 * Service class for CRUD operations on URL bean
 * @author alandonohoe
 *
 */

public class URLService {
	
	@PersistenceUnit(unitName="GoFetch")
	EntityManagerFactory emf;
	
	//TODO: check to see if this works here - and remove from each of the method bodies...
	//EntityManager mgr = emf.createEntityManager();
	
	private static Logger logger = Logger.getLogger(URLService.class.getName());
	
	public void createURL(URL url){
		logger.info("Entering url["
				+ url.getUrl_address());
		
		//TODO: remove line if mgr can be instantiated at class member level...
		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		
        try {
            mgr.getTransaction().begin();
            mgr.persist(url);
            mgr.getTransaction().commit();
        } finally {
            mgr.close();
        }
        logger.info("Exiting createContact");
	}
	
	/**
     * Gets a URL given an ID
     * @param id
     * @return URL
     */
    public URL getURL(Long id) {
        logger.info("Entering getURL[" + id + "]");
        URL result = null;
        
        //TODO: remove line if mgr can be instantiated at class member level...
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
        logger.info("Entering getContacts");
        List<URL> result = null;
        
        //TODO: remove line if mgr can be instantiated at class member level...
        EntityManager mgr = emf.createEntityManager();
        
        try {
            Query q = mgr.createQuery("SELECT * from url");
            result = (List<URL>) q.getResultList();
        } finally {
            mgr.close();
        }
        if (result == null) {
            logger.warning("No contacts returned");
        }
        logger.info("Exiting getContacts");
        return result;
    }


}
