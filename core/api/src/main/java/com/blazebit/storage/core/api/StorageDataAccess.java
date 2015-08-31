package com.blazebit.storage.core.api;

import java.util.List;

import com.blazebit.persistence.QueryBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.jpa.StorageId;

public interface StorageDataAccess {

	public <T> List<T> findAllByAccountId(long accountId, EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting);

	public Storage findById(StorageId storageId);

	public Storage findByBucketId(String bucketId);

	public <T> T findById(StorageId storageId, EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting);
}
