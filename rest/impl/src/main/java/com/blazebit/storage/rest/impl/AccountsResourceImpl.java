package com.blazebit.storage.rest.impl;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.UserAccountDataAccess;
import com.blazebit.storage.core.api.UserAccountService;
import com.blazebit.storage.core.model.jpa.UserAccount;
import com.blazebit.storage.rest.api.AccountSubResource;
import com.blazebit.storage.rest.api.AccountsResource;
import com.blazebit.storage.rest.impl.view.AccountRepresentationView;
import com.blazebit.storage.rest.model.AccountRepresentation;
import com.blazebit.storage.rest.model.AccountUpdate;

public class AccountsResourceImpl extends AbstractResource implements AccountsResource {

	@Inject
	private UserAccountDataAccess userAccountDataAccess;
	@Inject
	private UserAccountService userAccountService;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<AccountRepresentation> get() {
		return (List<AccountRepresentation>) (List<?>) userAccountDataAccess.getUserAccounts(EntityViewSetting.create(AccountRepresentationView.class));
	}

	@Override
	public AccountSubResource get(String id) {
		throw new UnsupportedOperationException("Not yet implemented");
//		return inject(new AccountSubResourceImpl(id));
	}

	@Override
	public Response create(AccountUpdate account) {
		UserAccount userAccount = new UserAccount();
		userAccount.setKey(null);
		userAccount.setName(account.getName());
		userAccountService.createUserAccount(userAccount);
		URI uri = uriInfo.getRequestUriBuilder()
			.path(AccountsResource.class, "get")
			.build(userAccount.getId());
		return Response.created(uri).build();
	}

}
