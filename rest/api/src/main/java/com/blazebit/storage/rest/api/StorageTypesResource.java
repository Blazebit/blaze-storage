package com.blazebit.storage.rest.api;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.blazebit.storage.rest.model.StorageTypeListElementRepresentation;
import com.blazebit.storage.rest.model.StorageTypeRepresentation;

@RolesAllowed({ "admin" })
@Path("storage-types")
public interface StorageTypesResource {
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<StorageTypeListElementRepresentation> get();

	@GET
	@Path("{type}")
	@Produces({ MediaType.APPLICATION_JSON })
	public StorageTypeRepresentation get(@PathParam("type") String type);
}
