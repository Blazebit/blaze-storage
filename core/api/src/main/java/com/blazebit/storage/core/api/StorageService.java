package com.blazebit.storage.core.api;

import com.blazebit.storage.core.model.jpa.Storage;

public interface StorageService {

	public void createStorage(Storage storage);

	public void deleteStorage(long userAccountId, String name);
}
