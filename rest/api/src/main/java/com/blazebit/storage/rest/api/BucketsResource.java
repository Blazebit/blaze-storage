package com.blazebit.storage.rest.api;

import javax.ws.rs.Path;

@Path("buckets")
public interface BucketsResource {

	@Path("")
	public BucketsSubResource get();
}
