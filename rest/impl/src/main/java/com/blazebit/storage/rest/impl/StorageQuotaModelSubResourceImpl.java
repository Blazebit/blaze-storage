package com.blazebit.storage.rest.impl;

import javax.ws.rs.core.Response;

import com.blazebit.storage.rest.api.StorageQuotaModelSubResource;
import com.blazebit.storage.rest.model.StorageQuotaModelRepresentation;
import com.blazebit.storage.rest.model.StorageQuotaModelUpdateRepresentation;

public class StorageQuotaModelSubResourceImpl extends AbstractResource implements StorageQuotaModelSubResource {
	
	private String id;

	public StorageQuotaModelSubResourceImpl(String id) {
		this.id = id;
	}

	@Override
	public StorageQuotaModelRepresentation get() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response put(StorageQuotaModelUpdateRepresentation quotaModel) {
		// TODO Auto-generated method stub
		return null;
	}

}
