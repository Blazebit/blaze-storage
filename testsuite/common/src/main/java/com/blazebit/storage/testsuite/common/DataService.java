package com.blazebit.storage.testsuite.common;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.blazebit.storage.core.config.api.persistence.MasterOnly;

@Stateless
public class DataService {
	
	@Inject
    @MasterOnly
	private EntityManager em;
	
    public <E> E persist(final E entity) {
        em.persist(entity);
        em.flush();
        return entity;
    }

    public void detach(final Object entity) {
        em.detach(entity);
    }

	public <E> E merge(E entity) {
		return em.merge(entity);
	}
	
	public <T> T transactional(TxWork<T> consumer) {
		return consumer.doWork(em);
	}
	
	public void transactional(TxVoidWork consumer) {
		consumer.doWork(em);
	}
}
