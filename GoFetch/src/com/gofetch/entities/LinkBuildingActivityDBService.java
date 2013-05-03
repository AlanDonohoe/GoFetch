package com.gofetch.entities;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
//import javax.persistence.PersistenceUnit;

public class LinkBuildingActivityDBService {
 	
//	@PersistenceUnit(unitName="GoFetch")
//	EntityManagerFactory emf;

	private static Logger log = Logger.getLogger(UserDBService.class.getName());
	
@SuppressWarnings("unchecked")
public List<LinkBuildingActivity> getAllLinkActivities(){
		
		log.info("Entering getAllLinkActivities");
		
		List<LinkBuildingActivity> linkActivities = null;
		
//		//OLD
//		emf = Persistence.createEntityManagerFactory("GoFetch");
//		EntityManager mgr = emf.createEntityManager();
		
		// NEW
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager mgr = emf.createEntityManager();
		
		try { 																				
			linkActivities= (List<LinkBuildingActivity>) mgr.createQuery("SELECT u FROM LinkBuildingActivity u ORDER BY u.activity ASC").getResultList();
		}catch(Exception e){
			String msg = "Exception thrown. LinkBuildingActivityDBService::getAllLinkActivities";

			log.severe(msg + e.getMessage());

		} finally {
			mgr.close();
		}
		if (linkActivities == null) {
			log.warning("No LinkActivities returned");
		}
		log.info("Exiting getAllLinkActivities");
		
		return linkActivities;
		
	}

}
