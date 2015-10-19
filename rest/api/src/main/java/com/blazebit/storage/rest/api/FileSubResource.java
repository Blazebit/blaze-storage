package com.blazebit.storage.rest.api;

import java.io.InputStream;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.blazebit.storage.rest.api.aop.ResponseObject;
import com.blazebit.storage.rest.model.BucketObjectHeadRepresentation;
import com.blazebit.storage.rest.model.BucketObjectRepresentation;
import com.blazebit.storage.rest.model.BucketObjectUpdateRepresentation;

public interface FileSubResource {

	@GET
	@ResponseObject
	public BucketObjectRepresentation get();

	/**
	 * The HEAD operation retrieves metadata from an object without returning
	 * the object itself. This operation is useful if you are interested only in
	 * an object's metadata. To use HEAD, you must have READ access to the
	 * object.
	 * 
	 * @return
	 */
	@HEAD
	@ResponseObject
	public BucketObjectHeadRepresentation head();

	// This is for CORS, we can do that later
	// @OPTIONS
	// public Response options();

	/**
	 * This implementation of the DELETE operation marks the object named in the
	 * URI (including all object versions) as deleted. If the object doesn't
	 * exist this does nothing.
	 * 
	 * @return
	 */
	@DELETE
	public Response delete();

	@PUT
	@Consumes({ MediaType.WILDCARD })
	public Response put(@BeanParam BucketObjectUpdateRepresentation bucketObjectUpdate, InputStream content);

}
