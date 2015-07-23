package com.blazebit.storage.rest.impl;

import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.apache.deltaspike.core.api.provider.BeanProvider;

public abstract class AbstractResource {

	@Context
	protected UriInfo uriInfo;
	@Context
	private ResourceContext rc;

	public <T> T inject(T instance) {
		rc.initResource(instance);
		return BeanProvider.injectFields(instance);
	}
}
