package com.gofetch.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
//import javax.persistence.TemporalType;

public class LinkDBService{

	@PersistenceUnit(unitName="GoFetch")
	EntityManagerFactory emf;

	private static Logger log = Logger.getLogger(URLDBService.class.getName());
	
	public void createLink(Link link, boolean checkAnchorText){
		
		if(!checkAnchorText){ // no need to check link's anchor text - just call createLink - 
			createLink(link);
		}
		else{
			if(null == getLink(link.getSource_id(), link.getTarget_id(), link.getAnchor_text())){
				createLink(link);
			}
		}
		
	}
	
	// 5-2-13: made this private - so it can only be called by wrapper method that can be switched to check for anchor text uniqueness or not.
	private void createLink(Link link){

		log.info("Entering new link. Source: [" + link.getSource_id() + " Target: " + link.getTarget_id() + "]");

		//OLD
		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		
		// NEW
//		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
//		EntityManager mgr = emf.createEntityManager();
		
		// check that link does not already exist in DB with this target and source ID
		// 5-2-13: removed this check and moved it to wrapper method : createLink(Link link, boolean checkAnchorText)
		//		which includes a switch to check for anchor text or not....
		//if(null == getLink(link.getSource_id(), link.getTarget_id()) ){
			try {

				mgr.getTransaction().begin();
				mgr.persist(link);
				mgr.getTransaction().commit();

			}catch(Exception e){
				String msg = "Exception thrown. " + "LinkDBService: createLink";
				//logger.logp(Level.SEVERE, "LinkDBService", "createLink",msg ,e);
				log.severe(msg + e.getMessage());
			}
			finally {
				mgr.close();
			}
		//}
	}

	/**
	 * 
	 * @param finalTargetURLAddress
	 * @return list of Links that all have their final_target_url as finalTargetURLAddress
	 */
	@SuppressWarnings("unchecked")
	public List<Link> getLinksInTreePointingTo(String finalTargetURLAddress){

		log.info("Entering getSourceURLsIDsPointingTo [" + finalTargetURLAddress + "]");

		//OLD
		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		
		// NEW
//		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
//		EntityManager mgr = emf.createEntityManager();

		//Integer targetURLID = targetURL.getId();

		//List<Integer> backLinkIDs = new ArrayList<Integer>();
		List<Link> links = null;

		try {
			links =  (List<Link>) mgr.createQuery("SELECT u FROM Link u WHERE u.final_target_url = :final_target_url")
					.setParameter("final_target_url",  finalTargetURLAddress)
					.getResultList();

		}catch(Exception e){
			String msg = "Exception thrown getting data for: " + finalTargetURLAddress + ". LinkDBService: getLinksInTreePointingTo";
			//logger.logp(Level.SEVERE, "LinkDBService", "getLinksInTreePointingTo",msg ,e);
			log.severe(msg + e.getMessage());
		} finally {
			mgr.close();
		}

		log.info("Exiting getSourceURLsIDsPointingTo [" + finalTargetURLAddress + "]"); 

		return links;

	}

	/**
	 * 
	 * @param targetURL 
	 * @return - list of ids of each url pointing to the target.
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> getSourceURLIDsPointingTo(URL targetURL){

		log.info("Entering getSourceURLIDsPointingTo [" + targetURL.getUrl_address() + "]");

		//OLD
		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		
		// NEW
//		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
//		EntityManager mgr = emf.createEntityManager();

		Integer targetURLID = targetURL.getId();

		List<Integer> sourceURLIDs = new ArrayList<Integer>();
		List<Link> links = null;

		try {
			links = (List<Link>) mgr.createQuery("SELECT u FROM Link u WHERE u.target_id = :targetURLID")
					.setParameter("targetURLID",  targetURLID)
					.getResultList();

		}catch(Exception e){
			String msg = "Exception thrown getting data for: " + targetURL.getUrl_address() + "LinkDBService: getLinkIDsPointingTo \n";
			log.severe(msg + e.getMessage());
		} finally {
			mgr.close();
		}

		// add all links' source id to list of ids
		for(Link currentLink : links){
			sourceURLIDs.add(currentLink.getSource_id());
		}

		log.info("Exiting getSourceURLIDsPointingTo [" + targetURL + "]"); 

		return sourceURLIDs;
	}

	/**
	 * 
	 * @param targetURL 
	 * @return - list of ids of each url pointing to the target.
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> getSourceURLsIDsPointingTo(URL targetURL){

		log.info("Entering getSourceURLsIDsPointingTo [" + targetURL.getUrl_address() + "]");

		//OLD
		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		
		// NEW
//		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
//		EntityManager mgr = emf.createEntityManager();

		Integer targetURLID = targetURL.getId();

		List<Integer> backLinkIDs = new ArrayList<Integer>();
		List<Link> links;

		try {
			links = (List<Link>) mgr.createQuery("SELECT u FROM Link u WHERE u.target_id = :targetURLID")
					.setParameter("targetURLID",  targetURLID)
					.getResultList();

		} finally {
			mgr.close();
		}

		//TODO: replace this with just retreiving the id field in the above query: eg: select u.id from ....
		// add all links' source id to list of ids
		for(Link currentLink : links){
			backLinkIDs.add(currentLink.getSource_id());
		}

		log.info("Exiting getSourceURLsIDsPointingTo [" + targetURL + "]"); 

		return backLinkIDs;
	}

	/**
	 * returns the number of times the url with the urlID param occurs in the links table. Either as a source or target.
	 * - useful for checking when you want to delete the url, if it occurs more than once, then the url is linked to
	 * 	multiple times, so avoid deleting...
	 * @param urlID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer noOfTimesURLIsLinkedTo(Integer urlID){

		log.info("Entering noOfTimesURLIsLinkedTo: " + urlID);

		Integer result =0;

		//OLD
		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		
		// NEW
//		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
//		EntityManager mgr = emf.createEntityManager();

		List<Link> links;

		try {
			links = (List<Link>) mgr.createQuery("SELECT u FROM Link u WHERE u.target_id = :urlID OR u.source_id = :urlID")
					.setParameter("urlID",  urlID)
					.getResultList();

		} finally {
			mgr.close();
		}

		result = links.size();

		log.info("Exiting noOfTimesURLIsLinkedTo(...)");

		return result;

	}

	/**
	 * returns the number of times the url with the urlID param occurs in the links table as a source.
	 * @param urlID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Integer noOfTimesURLIsSourceURL(Integer urlID){

		log.info("Entering noOfTimesURLIsSourceURL: " + urlID);

		Integer result =0;

		//OLD
		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		
		// NEW
//		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
//		EntityManager mgr = emf.createEntityManager();

		List<Link> links;

		try {
			links = (List<Link>) mgr.createQuery("SELECT u FROM Link u WHERE u.source_id = :urlID")
					.setParameter("urlID",  urlID)
					.getResultList();

		} finally {
			mgr.close();
		}

		result = links.size();

		log.info("Exiting noOfTimesURLIsSourceURL(...)");

		return result;

	}


	public void deleteLinksInTreeWithRoot(String finalTargetURL){

		log.info("Entering deleteLinksInTreeWithRoot. Deleting all links pointing to " + finalTargetURL);

		//Link link;

		//OLD
		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		
		// NEW
//		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
//		EntityManager mgr = emf.createEntityManager();

		try{

			mgr.getTransaction().begin();

			mgr.createQuery("DELETE FROM Link u WHERE u.final_target_url = :final_target_url")
			.setParameter("final_target_url",  finalTargetURL)
			.executeUpdate();

			mgr.getTransaction().commit();


		}catch(Exception e){
			String msg = "Exception thrown deleting: " + finalTargetURL + "LinkDBService: deleteLinksInTreeWithRoot";
			//logger.logp(Level.SEVERE, "LinkDBService", "deleteLinksInTreeWithRoot",msg ,e);
			log.severe(msg + e.getMessage());

		} finally {
			mgr.close();
		}

		log.info("Exiting deleteLinksInTreeWithRoot.");

	}

	public void deleteLinksPointingTo(int targetURLID){

		log.info("Entering deleteLinksPointingTo. Deleting all links pointing to " + targetURLID);

		//Link link;

		//OLD
		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		
		// NEW
//		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
//		EntityManager mgr = emf.createEntityManager();

		try{

			mgr.getTransaction().begin();

			mgr.createQuery("DELETE FROM Link u WHERE u.target_id = :targetURLID")
			.setParameter("targetURLID",  targetURLID)
			.executeUpdate();

			mgr.getTransaction().commit();


		}catch(Exception e){
			String msg = "Exception thrown deleting: " + targetURLID + " LinkDBService + deleteLinksPointingTo";
			//logger.logp(Level.SEVERE, "LinkDBService", "deleteLinksPointingTo",msg ,e);
			log.severe(msg + e.getMessage());

		} finally {
			mgr.close();
		}

		log.info("Exiting deleteLinksPointingTo.");

	}


	public void deleteLinkByLinkID(Integer links_id){

		log.info("Entering deleteLinkByLinkID. Deleting " + links_id);

		//Link link;

		//OLD
		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		
//		// NEW
//		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
//		EntityManager mgr = emf.createEntityManager();

		try{
			mgr.getTransaction().begin();
			mgr.createQuery("DELETE FROM Link u WHERE u.links_id = :links_id")
			.setParameter("links_id",  links_id)
			.executeUpdate();

			mgr.getTransaction().commit();

		}catch(Exception e){
			String msg = "Exception thrown deleting: " + links_id + ". LinkDBService: deleteLinkByLinkID";
			//logger.logp(Level.SEVERE, "LinkDBService", "deleteLinkByLinkID",msg ,e);
			log.severe(msg + e.getMessage());

		} finally {
			mgr.close();
		}

		log.info("Exiting deleteLinkByLinkID.");
	}


	public void deleteLinkBySourceID(Integer source_id){

		log.info("Entering deleteLinkBySourceID. Deleting " + source_id);

		//Link link;

		//OLD
		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		
		// NEW
//		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
//		EntityManager mgr = emf.createEntityManager();


		try{
			mgr.getTransaction().begin();
			mgr.createQuery("DELETE FROM Link u WHERE u.source_id = :source_id")
			.setParameter("source_id",  source_id)
			.executeUpdate();

			mgr.getTransaction().commit();

		}catch(Exception e){
			String msg = "Exception thrown deleting: " + source_id + ". LinkDBService + deleteLinkBySourceID";
			//logger.logp(Level.SEVERE, "LinkDBService", "deleteLinkBySourceID",msg ,e);
			log.severe(msg + e.getMessage());

		} finally {
			mgr.close();
		}


		log.info("Exiting deleteLinkBySourceID.");
	}

	/**
	 * returns list of all links that this link (id) occurs in as target_id
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Link> getAllLinks(Integer id){

		log.info("Entering getAllLinks");

		//OLD
		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		
		// NEW
//		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
//		EntityManager mgr = emf.createEntityManager();

		List<Link> links;

		try {
			links = (List<Link>) mgr.createQuery("SELECT u FROM Link u WHERE u.target_id = :urlID OR u.source_id = :urlID")
					.setParameter("urlID",  id)
					.getResultList();

		} finally {
			mgr.close();
		}

		log.info("Exiting getAllLinks"); 

		return links;
	}

	public Link getLink(Integer id){
		log.info("Entering getLink[" + id + "]");
		Link result = null;

		//OLD
		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		
		// NEW
//		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
//		EntityManager mgr = emf.createEntityManager();

		try {
			result = mgr.find(Link.class, id);
		}catch(Exception e){
			String msg = "Exception thrown...";
			log.logp(Level.SEVERE, "LinkDBService", "getLink",msg ,e);

		} finally {
			mgr.close();
		}
		if (result == null) {
			log.warning("No link returned");
		}
		log.info("Exiting getLink");
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getURLIDsPointingTo(URL url) {

		log.info("Entering getURLIDsPointingTo");

		//OLD
		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		
		// NEW
//		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
//		EntityManager mgr = emf.createEntityManager();

		List<Integer> result;

		try {
			result = (List<Integer>)mgr.createQuery("SELECT u.source_id FROM Link u WHERE u.target_id = :urlID")
					.setParameter("urlID",  url.getId())
					.getResultList();

		} finally {
			mgr.close();
		}


		log.info("Exiting getURLIDsPointingTo");

		return result;

	}
	
	
	@SuppressWarnings("unchecked")
	public Link getLink(Integer sourceID, Integer targetID, String anchortext){

		log.info("Entering getLink");

		//OLD
		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		
		// NEW
//		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
//		EntityManager mgr = emf.createEntityManager();

		List<Link> links = null;

		try {
			links = (List<Link>) mgr.createQuery("SELECT u FROM Link u WHERE u.target_id = :targetID AND u.source_id = :sourceID and u.anchor_text = :anchortext")
					.setParameter("sourceID",  sourceID)
					.setParameter("targetID",  targetID)
					.setParameter("anchortext",  anchortext)
					.getResultList();
		}catch(Exception e){
			String msg = "Exception thrown getting Link: Target id: " + targetID  + ". Source ID: " + sourceID + ". Anchor Text: " + anchortext + ". LinkDBService: getLink(...). ";
			
			log.severe(msg + e.getMessage());

		} finally {
			mgr.close();
		}

		log.info("Exiting getURL");

		if((null == links) || (links.isEmpty()))
			return null;
		else
			return links.get(0);

	}

	@SuppressWarnings("unchecked")
	public Link getLink(Integer sourceID, Integer targetID){

		log.info("Entering getLink");

		//OLD
		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		
		// NEW
//		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
//		EntityManager mgr = emf.createEntityManager();
		
		List<Link> links = null;



		try {
			links = (List<Link>) mgr.createQuery("SELECT u FROM Link u WHERE u.target_id = :targetID AND u.source_id = :sourceID")
					.setParameter("sourceID",  sourceID)
					.setParameter("targetID",  targetID)
					.getResultList();
		}catch(Exception e){
			String msg = "Exception thrown getting Link: Target id: " + targetID  + ". Source ID: " + sourceID + ". LinkDBService: getLink(...). ";
			//logger.logp(Level.SEVERE, "URLService", "getURL",msg ,e);
			log.severe(msg + e.getMessage());

		} finally {
			mgr.close();
		}

		log.info("Exiting getURL");

		if((null == links) || (links.isEmpty()))
			return null;
		else
			return links.get(0);

	}

	public Integer getLinkID(Integer sourceID, Integer targetID){

		log.info("Entering getLink");

		//OLD
		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		
		// NEW
//		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
//		EntityManager mgr = emf.createEntityManager();

		//List<Integer> result;
		Integer result = 0;

		try {
			result = (Integer)mgr.createQuery("SELECT u.links_id FROM Link u WHERE u.target_id = :targetID AND u.source_id = :sourceID")
					.setParameter("sourceID",  sourceID)
					.setParameter("targetID",  targetID)
					.getSingleResult();

		} finally {
			mgr.close();
		}


		log.info("Exiting getLink");

		return result;

	}
}
