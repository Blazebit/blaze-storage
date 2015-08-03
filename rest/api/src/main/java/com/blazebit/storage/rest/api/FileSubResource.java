package com.blazebit.storage.rest.api;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface FileSubResource {

	@GET
	public Response get();

	/**
	 * The HEAD operation retrieves metadata from an object without returning
	 * the object itself. This operation is useful if you are interested only in
	 * an object's metadata. To use HEAD, you must have READ access to the
	 * object.
	 * 
	 * @return
	 */
	@HEAD
	public Response head();

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
	public Response put(InputStream inputStream);

	// We might also want to add POST to allow direct uploads
	// @POST
	// @Path("{path}")
	// public Response createFile(@PathParam("path") String path);
}
