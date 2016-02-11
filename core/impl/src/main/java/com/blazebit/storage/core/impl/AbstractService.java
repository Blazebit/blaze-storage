package com.blazebit.storage.core.impl;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.storage.core.config.api.persistence.MasterOnly;

public abstract class AbstractService {

	@Inject
	@MasterOnly
	protected EntityManager em;
	@Inject
	@MasterOnly
	protected CriteriaBuilderFactory cbf;
}
