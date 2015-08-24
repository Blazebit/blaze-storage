package com.blazebit.storage.rest.impl;

import java.util.List;

import javax.inject.Inject;

import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.StorageQuotaModelDataAccess;
import com.blazebit.storage.rest.api.StorageQuotaModelSubResource;
import com.blazebit.storage.rest.api.StorageQuotaModelsResource;
import com.blazebit.storage.rest.impl.view.StorageQuotaModelListElementRepresentationView;
import com.blazebit.storage.rest.model.StorageQuotaModelListElementRepresentation;

public class StorageQuotaModelsResourceImpl extends AbstractResource implements StorageQuotaModelsResource {

	@Inject
	private StorageQuotaModelDataAccess quotaDataAccess;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<StorageQuotaModelListElementRepresentation> get() {
		return (List<StorageQuotaModelListElementRepresentation>) (List<?>) quotaDataAccess.findAll(EntityViewSetting.create(StorageQuotaModelListElementRepresentationView.class));
	}

	@Override
	public StorageQuotaModelSubResource get(String id) {
		return inject(new StorageQuotaModelSubResourceImpl(id));
	}

}
