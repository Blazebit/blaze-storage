package com.blazebit.storage.rest.impl;

import java.util.List;

import javax.inject.Inject;

import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.UserAccountDataAccess;
import com.blazebit.storage.rest.api.AccountSubResource;
import com.blazebit.storage.rest.api.AccountsResource;
import com.blazebit.storage.rest.impl.view.AccountRepresentationView;
import com.blazebit.storage.rest.model.AccountListElementRepresentation;

public class AccountsResourceImpl extends AbstractResource implements AccountsResource {

	@Inject
	private UserAccountDataAccess userAccountDataAccess;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<AccountListElementRepresentation> get() {
		return (List<AccountListElementRepresentation>) (List<?>) userAccountDataAccess.findAll(EntityViewSetting.create(AccountRepresentationView.class));
	}

	@Override
	public AccountSubResource get(String id) {
		return inject(new AccountSubResourceImpl(id));
	}

}
