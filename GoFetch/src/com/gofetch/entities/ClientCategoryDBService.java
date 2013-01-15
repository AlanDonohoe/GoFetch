package com.gofetch.entities;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

public class ClientCategoryDBService {

	@PersistenceUnit(unitName="GoFetch")
	EntityManagerFactory emf;

	private static Logger log = Logger.getLogger(URLDBService.class.getName());
	
	
	public List<ClientCategory> getClientsCategories(Integer clientID){
		
		log.info("Entering getClientsCategories() for client id: " + clientID);
		
		List <ClientCategory> clientsCategories = null;
		emf = Persistence.createEntityManagerFactory("GoFetch");
		EntityManager mgr = emf.createEntityManager();
		
		try {
			
			clientsCategories = mgr.createQuery("SELECT u FROM ClientCategory u WHERE u.users_id = :clientID ORDER BY u.category ASC")
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
