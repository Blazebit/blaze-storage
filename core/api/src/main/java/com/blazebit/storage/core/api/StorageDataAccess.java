package com.blazebit.storage.core.api;

import java.util.List;

import com.blazebit.storage.core.model.jpa.Storage;

public interface StorageDataAccess {

	public List<Storage> getStorages(long userAccountId);

	public Storage getStorage(long userAccountId, String name);
}
