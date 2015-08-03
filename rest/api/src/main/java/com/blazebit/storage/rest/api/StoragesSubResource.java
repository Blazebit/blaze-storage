package com.blazebit.storage.rest.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.blazebit.storage.rest.api.aop.Authenticated;
import com.blazebit.storage.rest.model.StorageListRepresentation;

@Authenticated
public interface StoragesSubResource {

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public StorageListRepresentation get();

	@Path("{storageName}")
	public StorageSubResource get(@PathParam("storageName") String storageName);
}
