package com.blazebit.storage.rest.impl;

import java.util.List;

import javax.inject.Inject;

import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.AccountDataAccess;
import com.blazebit.storage.rest.api.AccountSubResource;
import com.blazebit.storage.rest.api.AccountsResource;
import com.blazebit.storage.rest.impl.view.AccountListElementRepresentationView;
import com.blazebit.storage.rest.model.AccountListElementRepresentation;

public class AccountsResourceImpl extends AbstractResource implements AccountsResource {

	@Inject
	private AccountDataAccess accountDataAccess;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<AccountListElementRepresentation> get() {
		return (List<AccountListElementRepresentation>) (List<?>) accountDataAccess.findAll(EntityViewSetting.create(AccountListElementRepresentationView.class));
	}

	@Override
	public AccountSubResource get(String id) {
		return inject(new AccountSubResourceImpl(id));
	}

}
