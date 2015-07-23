package com.blazebit.storage.core.impl.actor;

import java.util.concurrent.TimeUnit;

import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.blazebit.persistence.CriteriaBuilderFactory;

public abstract class AbstractActor {

	@Inject
	protected EntityManager em;
	@Inject
	protected CriteriaBuilderFactory cbf;

	public void init(@Observes @Initialized(ApplicationScoped.class) Object o) {
		ManagedScheduledExecutorService executorService = getExecutorService();
		executorService.scheduleAtFixedRate(new ActorTask(this), 0L, 10L, TimeUnit.SECONDS);
	}
	
	protected abstract void work();
	
	protected abstract ManagedScheduledExecutorService getExecutorService();

	// TODO: transform to annotations
	
	protected abstract long getInitialDelay();
	
	protected abstract long getPeriod();
	
	protected abstract TimeUnit getTimeUnit();
}
