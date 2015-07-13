package com.blazebit.storage.core.impl;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.blazebit.persistence.CriteriaBuilderFactory;

@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class AbstractDataAccess {

	@Inject
	protected EntityManager em;
	@Inject
	protected CriteriaBuilderFactory cbf;
}
