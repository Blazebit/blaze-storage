package com.blazebit.storage.core.impl;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public abstract class AbstractService {

	@Inject
	protected EntityManager em;
}
