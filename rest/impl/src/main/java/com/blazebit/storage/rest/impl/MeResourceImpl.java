package com.blazebit.storage.rest.impl;

import com.blazebit.storage.rest.api.AccountSubResource;
import com.blazebit.storage.rest.api.MeResource;

public class MeResourceImpl extends AbstractResource implements MeResource {

	@Override
	public AccountSubResource getAccount() {
		return inject(new AccountSubResourceImpl(null));
	}

}
