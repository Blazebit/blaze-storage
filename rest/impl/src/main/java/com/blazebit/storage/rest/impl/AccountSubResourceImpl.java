package com.blazebit.storage.rest.impl;

import com.blazebit.storage.rest.api.AccountSubResource;
import com.blazebit.storage.rest.api.StoragesSubResource;
import com.blazebit.storage.rest.model.AccountRepresentation;

public class AccountSubResourceImpl extends AbstractResource implements AccountSubResource {
	

	@Override
	public AccountRepresentation get() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StoragesSubResource getStorages() {
		return inject(new StoragesSubResourceImpl());
	}

}
