package com.gofetch.entities;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ImageDBService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger
			.getLogger(ImageDBService.class.getName());

	public void createImage(Image image) {

		EntityManagerFactory emf = PersistenceManager.getInstance()
				.getEntityManagerFactory();
		EntityManager mgr = emf.createEntityManager();

		try {

			mgr.getTransaction().begin();
			mgr.persist(image);
			mgr.getTransaction().commit();

		} catch (Exception e) {
			String msg = "Exception thrown. " + "ImageDBService::createImage";
			log.severe(msg + e.getMessage());
		} finally {
			mgr.close();
		}

	}
	
	public Image getImage(Integer imageId){
		
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager mgr = emf.createEntityManager();
		
		Image result = null;

		try {
			result =  (Image) mgr.createQuery("SELECT i FROM Image i WHERE i.id = :imageId")
					.setParameter("imageId",  imageId)
					.getSingleResult();

		}catch(Exception e){
			String msg = "Exception thrown getting data for: " + Integer.toString(imageId) + ". ImageDBService: getImage";
			log.severe(msg + e.getMessage());
		} finally {
			mgr.close();
		}

		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Image> getImagesPointingToObject(Integer objectId){
		
		EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		EntityManager mgr = emf.createEntityManager();
		
		List<Image> result = null;

		try {
			result =  (List<Image>) mgr.createQuery("SELECT i FROM Image i WHERE i.object_id= :objectId")
					.setParameter("objectId",  objectId)
					.getResultList();

		}catch(Exception e){
			String msg = "Exception thrown getting data for: " + Integer.toString(objectId) + ". ImageDBService: getImagesPointingToObject";
			log.severe(msg + e.getMessage());
		} finally {
			mgr.close();
		}

		return result;

	}

}
