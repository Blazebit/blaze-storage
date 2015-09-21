package com.blazebit.storage.rest.api;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.blazebit.storage.rest.model.BucketListElementRepresentation;

@RolesAllowed({ "admin" })
@Path("admin")
public interface AdminResource {

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("buckets")
	public List<BucketListElementRepresentation> getBuckets();
}
