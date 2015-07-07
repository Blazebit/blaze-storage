package com.blazebit.storage.rest.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.blazebit.storage.rest.model.FileRepresentation;

public interface FilesSubResource {

	@GET
	@Path("{path}")
	@Produces({ MediaType.APPLICATION_JSON })
	public FileRepresentation get(@PathParam("path") String path);
	
	@PUT
	@Path("{path}")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response createFile(@PathParam("path") String path);
}
