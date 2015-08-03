package com.blazebit.storage.rest.api;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.blazebit.storage.rest.model.AccountListElementRepresentation;

@RolesAllowed({ "admin" })
@Path("accounts")
public interface AccountsResource {
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public List<AccountListElementRepresentation> get();

	@Path("{key}")
	public AccountSubResource get(@PathParam("key") String key);
}
