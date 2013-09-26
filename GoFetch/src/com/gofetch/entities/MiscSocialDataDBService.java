package com.gofetch.entities;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
//import javax.persistence.PersistenceUnit;
import javax.persistence.TemporalType;

public class MiscSocialDataDBService {

//	@PersistenceUnit(unitName="GoFetch")
//	EntityManagerFactory emf;

	private static Logger log = Logger.getLogger(MiscSocialDataDBService.class.getName());

	public void createNewSocialData(MiscSocialData socialData){

		//log.info("Entering new MiscSocialData.");

		//OLD
//		emf = Persistence.createEntityManagerFactory("GoFetch");
//		EntityManager mgr = emf.createEntityManager();
		
		// NEW
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager mgr = emf.createEntityManager();

		try {

			mgr.getTransaction().begin();
			mgr.persist(socialData);
			mgr.getTransaction().commit();

		}catch(Exception e){
			String msg = "Exception thrown. MiscSocialDataDBService: createNewSocialData. \n";

			log.severe(msg + e.getMessage());
		}
		finally {
			mgr.close();
		}

		//log.info("Exiting MiscSocialData"); 
	}

	@SuppressWarnings("unchecked")
	public MiscSocialData getMostRecentSocialData(int url_id){

		//log.info("Entering getMostRecentSocialData");
		List<MiscSocialData> result = null;

		//OLD
//		emf = Persistence.createEntityManagerFactory("GoFetch");
//		EntityManager mgr = emf.createEntityManager();
		
		// NEW
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager mgr = emf.createEntityManager();
		
		try {
			result = (List<MiscSocialData>) mgr.createQuery("SELECT u FROM MiscSocialData u WHERE u.url_id = :url_id ORDER BY u.date desc")
					.setParameter("url_id", url_id)
					.getResultList();

		} finally {
			mgr.close();
		}
		if (result == null) {
			log.info("Exiting getMostRecentSocialData. NULL repsonse from DB");
			return null;
		}else if(result.isEmpty()){
			log.info("Exiting getMostRecentSocialData. No social data in DB");
			return null;
		}else{
			//log.info("Exiting getMostRecentSocialData");
			return result.get(0);
		}

	}

	@SuppressWarnings("unchecked")
	public List<MiscSocialData> getSocialDataBetween(int url_id, Date startDate, Date endDate){
		
		//log.info("Entering getSocialDataBetween");
		List<MiscSocialData> result = null;

		//OLD
//		emf = Persistence.createEntityManagerFactory("GoFetch");
//		EntityManager mgr = emf.createEntityManager();
		
		// NEW
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager mgr = emf.createEntityManager();

		try {
			
			result = (List<MiscSocialData>) mgr.createQuery("SELECT u FROM MiscSocialData u WHERE u.url_id = :url_id AND u.date BETWEEN :startDate AND :endDate ORDER BY u.date desc")
					.setParameter("startDate", startDate, TemporalType.DATE)
					.setParameter("endDate", endDate, TemporalType.DATE)
					.setParameter("url_id", url_id)
					.getResultList();


		}catch(Exception e){
			String msg = "Exception thrown. MiscSocialDataDBService: getSocialDataBetween";
			
			log.severe(msg + e.getMessage());

		} finally {
			mgr.close();
		}
		if (result == null) {
			log.warning("No Social Data returned");
		}
		//log.info("Exiting getSocialDataBetween");
		return result;
	
		
	}

	@SuppressWarnings("unchecked")
	public List<MiscSocialData> getAllSocialData(int url_id){

		List<MiscSocialData> result = null;
		
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager mgr = emf.createEntityManager();

		try {
			result = (List<MiscSocialData>) mgr.createQuery("SELECT u FROM MiscSocialData u WHERE u.url_id = :url_id ORDER BY u.date desc")
					.setParameter("url_id", url_id)
					.getResultList();

		} finally {
			mgr.close();
		}

		return result;
	}
	
	public void deleteSocialData(int url_id){
		
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager mgr = emf.createEntityManager();
		
			try{
				
				mgr.getTransaction().begin();
				
				mgr.createQuery("DELETE FROM MiscSocialData u WHERE u.url_id = :url_id")
								.setParameter("url_id", url_id)
								.executeUpdate();
				
				mgr.getTransaction().commit();

			}catch(Exception e){
				String msg = "Exception thrown deleting SocialData. MiscSocialDataDBService: deleteSocialData. \n";
				
				log.severe(msg + e.getMessage());
			} finally {
				mgr.close();
			}

	}

	/**
	 * 
	 * @return count of all social data entries
	 */
	public int getNoOfTotalSocialData() {

		int result;

		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager mgr = emf.createEntityManager();

		try {
			result = ((Number) mgr.createQuery("SELECT COUNT(u) FROM MiscSocialData u")
					.getSingleResult()).intValue();

		} finally {
			mgr.close();
		}

		return result;
	}

}
