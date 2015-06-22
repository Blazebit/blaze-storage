package com.blazebit.storage.rest;

import javax.ws.rs.Path;

import com.blazebit.storage.rest.aop.Authenticated;

@Authenticated
@Path("me")
public interface MeResource {
	
	@Path("account")
	public AccountSubResource getAccount();
}
