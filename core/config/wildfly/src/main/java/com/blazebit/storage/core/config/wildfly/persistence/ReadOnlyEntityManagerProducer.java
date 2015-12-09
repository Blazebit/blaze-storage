package com.blazebit.storage.core.config.wildfly.persistence;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;

import com.blazebit.storage.core.config.api.persistence.ReadOnly;

public class ReadOnlyEntityManagerProducer {

	@PersistenceContext(unitName = "StorageReadOnly")
	private EntityManager em;

	@Produces
	@ReadOnly
	@RequestScoped
	public EntityManager create() {
		// Make the underlying session read-only
		em.unwrap(Session.class).setDefaultReadOnly(true);
		return em;
	}
}