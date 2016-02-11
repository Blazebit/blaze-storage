package com.blazebit.storage.testsuite.common.persistence;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import com.blazebit.storage.core.config.api.persistence.MasterOnly;

public class MasterOnlyEntityManagerProducer {

	@Produces
	@MasterOnly
	@PersistenceUnit(unitName = PersistenceUnits.STORAGE_TEST_READ_ONLY)
	private EntityManagerFactory emf;
	
	@Produces
	@MasterOnly
	@PersistenceContext(unitName = PersistenceUnits.STORAGE_TEST_MASTER_ONLY)
	private EntityManager em;

}