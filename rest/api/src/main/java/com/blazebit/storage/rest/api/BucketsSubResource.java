package com.blazebit.storage.rest.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.blazebit.storage.rest.model.BucketListElementRepresentation;

public interface BucketsSubResource {
	
	/**
	 * Returns the buckets of the current user.
	 * 
	 * @return
	 */
	@GET
	@Path("")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<BucketListElementRepresentation> get();

	/**
	 * Returns the buckets with the given id.
	 * 
	 * @return
	 */
	@Path("{id}")
	public BucketSubResource get(@PathParam("id") String id);
	
}
