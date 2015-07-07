package com.blazebit.storage.rest.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.blazebit.storage.rest.model.AccountRepresentation;

public interface AccountSubResource {

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public AccountRepresentation get();
	
	@Path("storages")
	public StoragesSubResource getStorages();
}
