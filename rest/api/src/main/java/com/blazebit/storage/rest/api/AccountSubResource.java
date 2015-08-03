package com.blazebit.storage.rest.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.blazebit.storage.rest.model.AccountRepresentation;
import com.blazebit.storage.rest.model.AccountUpdateRepresentation;

public interface AccountSubResource {

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public AccountRepresentation get();
	
	// TODO: not yet planned
//	@DELETE
//	public Response delete();
	
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response put(AccountUpdateRepresentation account);
	
	@Path("storages")
	public StoragesSubResource getStorages();
}
