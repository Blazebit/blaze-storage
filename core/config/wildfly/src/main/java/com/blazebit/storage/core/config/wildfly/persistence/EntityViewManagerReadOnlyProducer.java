package com.blazebit.storage.core.config.wildfly.persistence;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViews;
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

	private EntityViewManager evm;
	
	@PostConstruct
	public void init() {
		EntityViewConfiguration readOnlyConfig = EntityViews.createDefaultConfiguration();
		for (Class<?> viewClass : config.getEntityViews()) {
			readOnlyConfig.addEntityView(viewClass);
		}

		configEvent.fire(readOnlyConfig);
    	evm = readOnlyConfig.createEntityViewManager(criteriaBuilderFactory);
	}

    @Produces
	@ReadOnly
    @ApplicationScoped
    public EntityViewManager createEntityViewManager() {
    	return evm;
    }
}
