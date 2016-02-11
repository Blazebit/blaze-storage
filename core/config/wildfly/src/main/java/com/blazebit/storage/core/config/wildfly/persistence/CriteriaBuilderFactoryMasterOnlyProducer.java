package com.blazebit.storage.core.config.wildfly.persistence;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

import com.blazebit.persistence.Criteria;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import com.blazebit.storage.core.config.api.persistence.MasterOnly;

@Singleton
@Startup
public class CriteriaBuilderFactoryMasterOnlyProducer {

	@Inject
	@MasterOnly
	private EntityManagerFactory entityManagerFactory;
	@Inject
	private Event<CriteriaBuilderConfiguration> configEvent;

	private CriteriaBuilderFactory criteriaBuilderFactory;

	@PostConstruct
	public void init() {
    	CriteriaBuilderConfiguration config = Criteria.getDefault();
    	configEvent.fire(config);
    	this.criteriaBuilderFactory = config.createCriteriaBuilderFactory(entityManagerFactory);
	}

    @Produces
	@MasterOnly
    @ApplicationScoped
    public CriteriaBuilderFactory createCriteriaBuilderFactory() {
    	return criteriaBuilderFactory;
    }
}