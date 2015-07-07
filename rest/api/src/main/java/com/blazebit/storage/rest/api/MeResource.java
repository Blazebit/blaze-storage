package com.blazebit.storage.rest.api;

import javax.ws.rs.Path;

import com.blazebit.storage.rest.api.aop.Authenticated;

@Authenticated
@Path("me")
public interface MeResource {
	
	@Path("account")
	public AccountSubResource getAccount();
}
