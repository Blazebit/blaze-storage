package com.blazebit.storage.rest.impl;

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import com.blazebit.storage.core.api.UserAccountService;
import com.blazebit.storage.core.model.jpa.UserAccount;
import com.blazebit.storage.rest.api.AccountSubResource;
import com.blazebit.storage.rest.api.AccountsResource;
import com.blazebit.storage.rest.api.StoragesSubResource;
import com.blazebit.storage.rest.model.AccountRepresentation;
import com.blazebit.storage.rest.model.AccountUpdateRepresentation;

public class AccountSubResourceImpl extends AbstractResource implements AccountSubResource {
	
	private String id;
	
	@Inject
	private UserAccountService userAccountService;

	public AccountSubResourceImpl(String id) {
		this.id = id;
	}

	@Override
	public AccountRepresentation get() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response put(AccountUpdateRepresentation account) {
		UserAccount userAccount = new UserAccount();
		userAccount.setKey(null);
		userAccount.setName(account.getName());
		userAccountService.create(userAccount);
		URI uri = uriInfo.getRequestUriBuilder()
			.path(AccountsResource.class, "get")
			.build(userAccount.getId());
		return Response.created(uri).build();
	}

	@Override
	public StoragesSubResource getStorages() {
		return inject(new StoragesSubResourceImpl(getUserByKey(id)));
	}
	
	private UserAccount getUserByKey(String key) {
		return null;
	}

}
