package com.blazebit.storage.core.impl;

import javax.ejb.Stateless;

import com.blazebit.storage.core.api.StorageQuotaModelService;
import com.blazebit.storage.core.model.jpa.StorageQuotaModel;

@Stateless
public class StorageQuotaModelServiceImpl extends AbstractService implements StorageQuotaModelService {

	@Override
	public void create(StorageQuotaModel storageQuotaModel) {
		em.persist(storageQuotaModel);
		em.flush();
	}

	@Override
	public void update(StorageQuotaModel storageQuotaModel) {
		em.merge(storageQuotaModel);
		em.flush();
	}

	@Override
	public void delete(String id) {
		throw new UnsupportedOperationException("Deletion of storage quota models not yet supported!");
	}

}
