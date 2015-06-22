package com.blazebit.storage.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.blazebit.storage.rest.model.DirectoryRepresentation;

public interface DirectoriesSubResource {

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public DirectoryRepresentation getRoot();
	
	@GET
	@Path("{path}")
	@Produces({ MediaType.APPLICATION_JSON })
	public DirectoryRepresentation get(@PathParam("path") String path);
	
	@PUT
	@Path("{path}")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response createDirectory(@PathParam("path") String path);
}
