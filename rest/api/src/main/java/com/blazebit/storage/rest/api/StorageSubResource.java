package com.blazebit.storage.rest.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.blazebit.storage.rest.model.StorageRepresentation;

public interface StorageSubResource {

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public StorageRepresentation get();
	
	@Path("directories")
	public DirectoriesSubResource getDirectories();
	
	@Path("files")
	public FilesSubResource getFiles();
}
