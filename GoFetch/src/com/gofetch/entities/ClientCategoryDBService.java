package com.gofetch.entities;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
//import javax.persistence.Persistence;
//import javax.persistence.PersistenceUnit;

public class ClientCategoryDBService {

//	@PersistenceUnit(unitName="GoFetch")
//	EntityManagerFactory emf;

	private static Logger log = Logger.getLogger(URLDBService.class.getName());
	
	
	@SuppressWarnings("unchecked")
	public ClientCategory getClientsDefaultCategory(Integer clientID){
		
	log.info("Entering getClientsDefaultCategory() for client id: " + clientID);
		
		List <ClientCategory> clientsDefaultCategory = null;
		
		//OLD
//		emf = Persistence.createEntityManagerFactory("GoFetch");
//		EntityManager mgr = emf.createEntityManager();
		
		// NEW
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager mgr = emf.createEntityManager();
		
		try {
			
			clientsDefaultCategory = (List <ClientCategory>) mgr.createQuery("SELECT u FROM ClientCategory u WHERE u.users_id = :clientID AND u.client_default = true")
					.setParameter("clientID", clientID)
					.getResultList();
		}catch(Exception e){
			String msg = "Exception thrown. ClientCategoryDBService: getClientsDefaultCategory. ";
			
			log.severe(msg + e.getMessage());

		} finally {
			mgr.close();
		}
		
		if(null == clientsDefaultCategory)
			return null;
		
		return clientsDefaultCategory.get(0);	
		
	}
	 
	
	@SuppressWarnings("unchecked")
	public List<ClientCategory> getClientsCategories(Integer clientID){
		
		log.info("Entering getClientsCategories() for client id: " + clientID);
		
		List <ClientCategory> clientsCategories = null;
		//OLD
//		emf = Persistence.createEntityManagerFactory("GoFetch");
//		EntityManager mgr = emf.createEntityManager();
		
		// NEW
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager mgr = emf.createEntityManager();
		
		try {
			
			clientsCategories = (List <ClientCategory>) mgr.createQuery("SELECT u FROM ClientCategory u WHERE u.users_id = :clientID ORDER BY u.category ASC")
					.setParameter("clientID", clientID)
					.getResultList();
		}catch(Exception e){
			String msg = "Exception thrown. ClientCategoryDBService: getClientsCategories. ";
			
			log.severe(msg + e.getMessage());

		} finally {
			mgr.close();
		}
		if (clientsCategories == null) {
			log.warning("ClientCategoryDBService: getClientsCategories. No Client Categories returned for id: " + clientID);
		}
		
		log.info("Exiting getClientsCategories() for client id: " + clientID);
		return clientsCategories;
		
	}
	
	public void addNewCategory(Integer clientID, String category){
		
		//TODO: implement all this.....
		
		
		
	}

}
