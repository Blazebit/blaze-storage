package com.blazebit.storage.rest.impl;

import com.blazebit.storage.core.model.jpa.Account;
import com.blazebit.storage.rest.api.StorageSubResource;
import com.blazebit.storage.rest.api.StoragesSubResource;
import com.blazebit.storage.rest.model.StorageListRepresentation;

public class StoragesSubResourceImpl extends AbstractResource implements StoragesSubResource {

	private Account owner;
	
	public StoragesSubResourceImpl(Account owner) {
		this.owner = owner;
	}

	@Override
	public StorageListRepresentation get() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StorageSubResource get(String storageName) {
		return inject(new StorageSubResourceImpl(owner, storageName));
	}

}
