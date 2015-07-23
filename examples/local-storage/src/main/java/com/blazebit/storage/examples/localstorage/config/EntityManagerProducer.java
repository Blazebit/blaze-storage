package com.blazebit.storage.examples.localstorage.config;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class EntityManagerProducer {

	@Produces
	@PersistenceContext(unitName = "StoragePU")
	EntityManager em;
}
