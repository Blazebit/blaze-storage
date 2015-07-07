package com.blazebit.storage.rest.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.blazebit.storage.rest.api.aop.Authenticated;
import com.blazebit.storage.rest.model.StorageRepresentation;

@Authenticated
@Path("storages")
public interface StoragesSubResource {

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<StorageRepresentation> getList();

	@Path("{id}")
	public StorageSubResource get(@PathParam("id") String id);
	
	@PUT
	@Path("{path}")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response createStorage(@PathParam("path") String path);
}
