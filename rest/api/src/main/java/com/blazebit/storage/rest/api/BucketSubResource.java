package com.blazebit.storage.rest.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.blazebit.storage.rest.api.aop.ResponseObject;
import com.blazebit.storage.rest.model.BucketHeadRepresentation;
import com.blazebit.storage.rest.model.BucketRepresentation;
import com.blazebit.storage.rest.model.BucketUpdateRepresentation;

public interface BucketSubResource {

	@GET
	@ResponseObject
	public BucketRepresentation get(
			@QueryParam("prefix") String prefix, 
			@QueryParam("limit") Integer limit, 
			@QueryParam("marker") String marker
	);

	/**
	 * This operation is useful to determine if a bucket exists and you have
	 * permission to access it. The operation returns a 200 OK if the bucket
	 * exists and you have permission to access it. Otherwise, the operation
	 * might return responses such as 404 Not Found and 403 Forbidden.
	 * 
	 * @return
	 */
	@HEAD
	@ResponseObject
	public BucketHeadRepresentation head();

	/**
	 * This implementation of the DELETE operation deletes the bucket named in
	 * the URI. All objects (including all object versions)
	 * in the bucket must be deleted before the bucket itself can be deleted.
	 * 
	 * @return
	 */
	@DELETE
	public Response delete();
	
	/**
	 * Creates or updates the bucket.
	 * 
	 * @param bucketUpdate
	 * @return
	 */
//	@PUT
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response put(BucketUpdateRepresentation bucketUpdate);
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response put(BucketUpdateRepresentation bucketUpdate, @HeaderParam("x-blz-owner-key") String ownerKey);
	
	@Path("{key: .+}")
	public FileSubResource getFile(@PathParam("key") String key);
}
