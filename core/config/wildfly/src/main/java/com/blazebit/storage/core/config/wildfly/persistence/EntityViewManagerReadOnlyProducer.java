package com.blazebit.storage.core.config.wildfly.persistence;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.spi.EntityViewConfiguration;
import com.blazebit.storage.core.config.api.persistence.ReadOnly;

@Singleton
@Startup
public class EntityViewManagerReadOnlyProducer {

	// inject the configuration provided by the cdi integration
	@Inject
	private EntityViewConfiguration config;
	@Inject
	private Event<EntityViewConfiguration> configEvent;

	// inject the criteria builder factory which will be used along with the entity view manager
	@Inject
	@ReadOnly
	private CriteriaBuilderFactory criteriaBuilderFactory;

	// inject your entity manager factory
	@Inject
	@ReadOnly
	private EntityManagerFactory entityManagerFactory;
	
	private EntityViewManager evm;
	
	@PostConstruct
	public void init() {
    	configEvent.fire(config);
    	evm = config.createEntityViewManager(criteriaBuilderFactory, entityManagerFactory);
	}

    @Produces
	@ReadOnly
    @ApplicationScoped
    public EntityViewManager createEntityViewManager() {
    	return evm;
    }
}
