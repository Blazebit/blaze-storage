package com.blazebit.storage.core.config.wildfly.persistence;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import com.blazebit.storage.core.config.api.persistence.MasterOnly;

public class MasterOnlyEntityManagerProducer {

	@Produces
	@MasterOnly
	@PersistenceUnit(unitName = "StorageMasterOnly")
	private EntityManagerFactory emf;
	
	@Produces
	@MasterOnly
	@PersistenceContext(unitName = "StorageMasterOnly")
	private EntityManager em;
	
}
