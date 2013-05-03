package com.gofetch.entities;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

/* see: http://javanotepad.blogspot.co.uk/2007/05/jpa-entitymanagerfactory-in-web.html
 * 
 */

public class PersistenceManager {
	  
	  private static final PersistenceManager singleton = new PersistenceManager();
	  
	  @PersistenceUnit(unitName="GoFetch")
	  protected EntityManagerFactory emf;
	  
	  public static PersistenceManager getInstance() {
	    
	    return singleton;
	  }
	  
	  private PersistenceManager() {
	  }
	 
	  public EntityManagerFactory getEntityManagerFactory() {
	    
	    if (emf == null)
	      createEntityManagerFactory();
	    return emf;
	  }
	  
	  public void closeEntityManagerFactory() {
	    
	    if (emf != null) {
	      emf.close();
	      emf = null;

	    }
	  }
	  
	  protected void createEntityManagerFactory() {
	    
	    this.emf = Persistence.createEntityManagerFactory("GoFetch");

	  }

}
