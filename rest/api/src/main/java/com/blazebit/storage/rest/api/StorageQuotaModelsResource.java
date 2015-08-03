package com.blazebit.storage.rest.api;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.blazebit.storage.rest.model.StorageQuotaModelListElementRepresentation;

@RolesAllowed({ "admin" })
@Path("storage-quota-models")
public interface StorageQuotaModelsResource {
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<StorageQuotaModelListElementRepresentation> get();

	@Path("{id}")
	public StorageQuotaModelSubResource get(@PathParam("id") String id);
}
