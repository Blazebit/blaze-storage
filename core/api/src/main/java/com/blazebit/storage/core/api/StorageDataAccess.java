package com.blazebit.storage.core.api;

import java.util.List;

import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.jpa.StorageId;

public interface StorageDataAccess {

	public List<Storage> findByUserAccountId(long userAccountId);

	public Storage findById(StorageId storageId);
}
