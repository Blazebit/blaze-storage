package com.blazebit.storage.core.impl;

import javax.ejb.Stateless;

import com.blazebit.storage.core.api.StorageService;
import com.blazebit.storage.core.model.jpa.Storage;

@Stateless
public class StorageServiceImpl extends AbstractService implements StorageService {

	@Override
	public void createStorage(Storage storage) {
		em.persist(storage);
	}

	@Override
	public void deleteStorage(long userAccountId, String name) {
		throw new UnsupportedOperationException("Deletion of storages not yet supported!");
	}

}
