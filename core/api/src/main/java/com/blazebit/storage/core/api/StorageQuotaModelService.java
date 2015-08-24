package com.blazebit.storage.core.api;

import com.blazebit.storage.core.model.jpa.StorageQuotaModel;

public interface StorageQuotaModelService {

	public void create(StorageQuotaModel storageQuotaModel);

	public void update(StorageQuotaModel storageQuotaModel);

	public void delete(String id);
}
