package com.gofetch.entities;

import java.util.logging.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

//TODO: delete this when we are sure we dont need it....
public class DBService {
	
	@PersistenceUnit(unitName="GoFetch")
	EntityManagerFactory emf;

	private static Logger logger = Logger.getLogger(DBService.class.getName());


}
