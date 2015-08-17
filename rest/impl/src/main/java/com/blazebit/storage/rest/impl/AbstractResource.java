package com.blazebit.storage.rest.impl;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.apache.deltaspike.core.api.provider.BeanProvider;

import com.blazebit.storage.core.api.context.UserContext;

@RequestScoped
public abstract class AbstractResource {

	@Context
	protected UriInfo uriInfo;
	@Inject
	protected UserContext userContext;
	@Context
	private ResourceContext rc;

	public <T> T inject(T instance) {
		rc.initResource(instance);
		return BeanProvider.injectFields(instance);
	}
}
