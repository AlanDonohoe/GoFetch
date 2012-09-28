package com.gofetch.entities;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.persistence.TemporalType;

//TODO: delete this when we are sure we dont need it....
public class DBService {
	
	@PersistenceUnit(unitName="GoFetch")
	EntityManagerFactory emf;

	private static Logger logger = Logger.getLogger(DBService.class.getName());


}
