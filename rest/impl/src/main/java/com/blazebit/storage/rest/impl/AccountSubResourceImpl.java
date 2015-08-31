package com.blazebit.storage.rest.impl;

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.AccountDataAccess;
import com.blazebit.storage.core.api.AccountService;
import com.blazebit.storage.core.model.jpa.Account;
import com.blazebit.storage.rest.api.AccountSubResource;
import com.blazebit.storage.rest.api.AccountsResource;
import com.blazebit.storage.rest.api.StoragesSubResource;
import com.blazebit.storage.rest.impl.view.AccountRepresentationView;
import com.blazebit.storage.rest.model.AccountRepresentation;
import com.blazebit.storage.rest.model.AccountUpdateRepresentation;

public class AccountSubResourceImpl extends AbstractResource implements AccountSubResource {
	
	private String key;
	
	@Inject
	private AccountService accountService;
	@Inject
	private AccountDataAccess accountDataAccess;

	public AccountSubResourceImpl(String key) {
		this.key = key;
	}

	@Override
	public AccountRepresentation get() {
		AccountRepresentation result = accountDataAccess.findByKey(key, EntityViewSetting.create(AccountRepresentationView.class));
		if (result == null) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("Account not found").build());
		}
		
		return result;
	}

	@Override
	public Response put(AccountUpdateRepresentation accountUpdate) {
		Account account = getAccountByKey(key);
		boolean isNew = account == null;
		
		if (isNew) {
			account = new Account();
			account.setKey(key);
		}
		
		account.setName(accountUpdate.getName());
		account.setTags(accountUpdate.getTags());
		
		if (isNew) {
			accountService.create(account);
		} else {
			accountService.update(account);
		}
		
		URI uri = uriInfo.getRequestUriBuilder()
			.path(AccountsResource.class, "get")
			.build(account.getId());
		return Response.created(uri).build();
	}

	@Override
	public StoragesSubResource getStorages() {
		return inject(new StoragesSubResourceImpl(getAccountByKey(key)));
	}
	
	private Account getAccountByKey(String key) {
		return accountDataAccess.findByKey(key);
	}

}
