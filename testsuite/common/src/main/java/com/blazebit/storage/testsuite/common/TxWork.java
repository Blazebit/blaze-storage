package com.blazebit.storage.testsuite.common;

import javax.persistence.EntityManager;

public interface TxWork<T> {

	public T doWork(EntityManager em);
	
}
