package com.blazebit.storage.core.api;

import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.jpa.StorageId;

public interface StorageService {

	public void put(Storage storage);

	public void delete(StorageId storageId);
}
