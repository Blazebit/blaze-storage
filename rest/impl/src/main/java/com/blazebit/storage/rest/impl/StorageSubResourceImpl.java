package com.blazebit.storage.rest.impl;

import javax.ws.rs.core.Response;

import com.blazebit.storage.core.model.jpa.StorageId;
import com.blazebit.storage.core.model.jpa.UserAccount;
import com.blazebit.storage.rest.api.StorageSubResource;
import com.blazebit.storage.rest.model.StorageRepresentation;
import com.blazebit.storage.rest.model.StorageUpdateRepresentation;

public class StorageSubResourceImpl extends AbstractResource implements StorageSubResource {
	
	private StorageId id;

	public StorageSubResourceImpl(UserAccount owner, String storageName) {
		this.id = new StorageId(owner, storageName);
	}

	@Override
	public StorageRepresentation get() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response put(StorageUpdateRepresentation storageUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

}
