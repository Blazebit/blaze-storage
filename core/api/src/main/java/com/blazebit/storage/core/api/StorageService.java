package com.blazebit.storage.core.api;

import com.blazebit.storage.core.model.jpa.ObjectStatistics;
import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.jpa.StorageId;

public interface StorageService {

	public void create(Storage storage);

	public void update(Storage storage);
	
	public void updateStatistics(StorageId storageId, ObjectStatistics deltaStatistics);

	public void delete(StorageId storageId);
}
