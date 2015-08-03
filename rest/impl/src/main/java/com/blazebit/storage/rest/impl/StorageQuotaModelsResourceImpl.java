package com.blazebit.storage.rest.impl;

import java.util.List;

import com.blazebit.storage.rest.api.StorageQuotaModelSubResource;
import com.blazebit.storage.rest.api.StorageQuotaModelsResource;
import com.blazebit.storage.rest.model.StorageQuotaModelListElementRepresentation;

public class StorageQuotaModelsResourceImpl extends AbstractResource implements StorageQuotaModelsResource {

	@Override
	public List<StorageQuotaModelListElementRepresentation> get() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StorageQuotaModelSubResource get(String id) {
		return inject(new StorageQuotaModelSubResourceImpl(id));
	}

}
