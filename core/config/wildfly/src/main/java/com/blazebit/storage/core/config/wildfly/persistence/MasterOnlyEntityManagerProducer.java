package com.blazebit.storage.core.config.wildfly.persistence;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.blazebit.storage.core.config.api.persistence.MasterOnly;

public class MasterOnlyEntityManagerProducer {

	@Produces
	@MasterOnly
	@PersistenceContext(unitName = "StorageMasterOnly")
	private EntityManager em;
	
}
