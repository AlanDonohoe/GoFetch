package com.gofetch.entities;

import java.util.List;
//import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

public class SEOMozDataDBService {

	@PersistenceUnit(unitName="GoFetch")
	EntityManagerFactory emf;

	private static Logger log = Logger.getLogger(SEOMozDataDBService.class.getName());

	/**
	 * Checks to see if a SEOMozData object with the same url_id already exists in the DB,
	 * if not - then creates a new SEOMozData entry.
	 * 
	 * @param seoMozDataObject
	 */
	public void createSEOMozData(SEOMozData seoMozDataObject){

		log.info("Entering createSEOMozData");

		//OLD
		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		
		// NEW
//		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
//		EntityManager mgr = emf.createEntityManager();

		//see if SEOMoz object already exists with url_id:
		if(null == getSEOMozDataByURLId(seoMozDataObject.getUrl_id())){
			try {

				mgr.getTransaction().begin();
				mgr.persist(seoMozDataObject);
				mgr.getTransaction().commit();


			}catch(Exception e){
				String msg = "Exception thrown. SEOMozDataDBService: createSEOMozData";
				//logger.logp(Level.SEVERE, "SEOMozDataDBService", "createSEOMozData",msg ,e);
				log.severe(msg + e.getMessage());

			}
			finally {
				mgr.close();
			}

		}else{
			String msg = "URL id: " + seoMozDataObject.getUrl_id() + "already has an SEOMoz object associated with it..";
			//logger.logp(Level.WARNING, "SEOMozDataDBService", "createSEOMozData",msg);
			log.warning(msg);
		}
		log.info("Exiting createSEOMozData");
	}

	public void deleteSEOMozData(int url_id){

		log.info("Entering deleteSEOMozData.");

		SEOMozData SEOMozDataObject = null;

		//OLD
		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		
		// NEW
//		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
//		EntityManager mgr = emf.createEntityManager();

		SEOMozDataObject = getSEOMozDataByURLId(url_id);

		if(SEOMozDataObject != null){

			try{

				mgr.getTransaction().begin();

				mgr.createQuery("DELETE FROM SEOMozData u WHERE u.url_id = :url_id")
				.setParameter("url_id", url_id)
				.executeUpdate();

				mgr.getTransaction().commit();

			}catch(Exception e){
				String msg = "Exception thrown deleting SEOMozData. SEOMozDataDBService: deleteSEOMozData";
				//logger.logp(Level.SEVERE, "SEOMozDataDBService", "deleteSEOMozData",msg ,e);
				log.severe(msg + e.getMessage());

			} finally {
				mgr.close();
			}

		}
		else{
			log.info("SEOMoz object not in DB");
		}

		log.info("Exiting deleteSEOMozData.");

	}

	SEOMozData getSEOMozDataByURLId(int url_id){

		//SEOMozData SEOMozDataObject;
		List<SEOMozData> result = null;

		log.info("Entering getSEOMozDataByURLId.");


		//OLD
		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		
		// NEW
//		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
//		EntityManager mgr = emf.createEntityManager();

		try {
			result = (List<SEOMozData>) mgr.createQuery("SELECT u FROM SEOMozData u WHERE u.url_id = :url_id")
					.setParameter("url_id", url_id)
					.getResultList();
		}catch(Exception e){
			String msg = "Exception thrown. URLService: getSociallyTrackedURLs";
			//logger.logp(Level.SEVERE, "URLService", "getSociallyTrackedURLs",msg ,e);
			log.severe(msg + e.getMessage());
			

		} finally {
			mgr.close();
		}
		log.info("Exiting getSEOMozDataByURLId.");
		if(null == result) 
			return null;
		if(result.isEmpty())
			return null;
		else
			return result.get(0);
	}
}



