package com.blazebit.storage.rest.api;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.blazebit.storage.rest.model.AccountRepresentation;
import com.blazebit.storage.rest.model.AccountUpdate;

@RolesAllowed({ "admin" })
@Path("accounts")
public interface AccountsResource {
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<AccountRepresentation> get();

	@Path("{id}")
	public AccountSubResource get(@PathParam("id") String id);
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response create(AccountUpdate account);
}
