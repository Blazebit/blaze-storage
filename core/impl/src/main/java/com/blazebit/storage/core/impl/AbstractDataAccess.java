package com.blazebit.storage.core.impl;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.storage.core.config.api.persistence.ReadOnly;

@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public abstract class AbstractDataAccess {

	@Inject
	@ReadOnly
	protected EntityManager em;
	@Inject
	@ReadOnly
	protected CriteriaBuilderFactory cbf;
	@Inject
	@ReadOnly
	protected EntityViewManager evm;
}
