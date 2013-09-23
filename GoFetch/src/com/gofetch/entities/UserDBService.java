package com.gofetch.entities;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * Handles all CRUD operations for User entities
 * @author alandonohoe
 *
 */
public class UserDBService {

	private static Logger log = Logger.getLogger(UserDBService.class.getName());

	public void createNewUser(User user){

		log.info("Entering UserDBService::createNewUser.  \n");

		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
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

	@SuppressWarnings("unchecked")
	public List<User> getAllClients(){

		log.info("Entering getAllClients");

		List<User> clients = null;

		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
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

	@SuppressWarnings("unchecked")
	public List<User> getUserByDisplayedName(String name){

		log.info("Entering getUserByDisplayedName");

		List<User> clients = null;

		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
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

	/**
	 * Returns Users from db who has "administrator" flag set to true.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<User> getAdministrator() {
		log.info("Entering getAdministrator");

		List<User> administrators = null;

		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager mgr = emf.createEntityManager();

		try { 														
			administrators= (List<User>) mgr.createQuery("SELECT u FROM User u WHERE u.administrator")
					.getResultList();
		}catch(Exception e){
			String msg = "Exception thrown. UserDBService::getAdministrator";
			log.severe(msg + e.getMessage());
		} finally {
			mgr.close();
		}
		if (administrators == null) {
			log.warning("No administrators found returned");
		}

		return administrators;
	}

}
