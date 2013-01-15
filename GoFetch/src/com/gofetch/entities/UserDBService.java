package com.gofetch.entities;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

public class UserDBService {
	
	@PersistenceUnit(unitName="GoFetch")
	EntityManagerFactory emf;

	private static Logger log = Logger.getLogger(UserDBService.class.getName());
	
	public void createNewUser(User user){
		
		log.info("Entering UserDBService::createNewUser.  \n");

		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		
		try {

			mgr.getTransaction().begin();
			mgr.persist(user);
			mgr.getTransaction().commit();

		}catch(Exception e){
			String msg = "Exception thrown. UserDBService::createNewUser. \n";

			log.severe(msg + e.getMessage());
		}
		finally {
			mgr.close();
		}

		log.info("Exiting createNewUser"); 
		
	}
	
	public List<User> getAllClients(){
		
		log.info("Entering getAllClients");
		
		List<User> clients = null;
		
		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		
		try { 
			clients= (List<User>) mgr.createQuery("SELECT u FROM User u WHERE u.client = true ORDER BY u.displayed_name ASC").getResultList();
		}catch(Exception e){
			String msg = "Exception thrown. UserDBService::getAllClients";

			log.severe(msg + e.getMessage());

		} finally {
			mgr.close();
		}
		if (clients == null) {
			log.warning("No URLs returned");
		}
		log.info("Exiting getAllClients");
		
		return clients;
		
	}
	
	public List<User> getUserByDisplayedName(String name){
		
		log.info("Entering getUserByDisplayedName");
		
		List<User> clients = null;
		
		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		
		try { 														
			clients= (List<User>) mgr.createQuery("SELECT u FROM User u WHERE u.client = true AND u.displayed_name = :name")
					.setParameter("name", name)
					.getResultList();
		}catch(Exception e){
			String msg = "Exception thrown. UserDBService::getUserByDisplayedName";

			log.severe(msg + e.getMessage());


		} finally {
			mgr.close();
		}
		if (clients == null) {
			log.warning("No Users returned");
		}
		log.info("Exiting getUserByDisplayedName");
		
		return clients;
		
	}

}
