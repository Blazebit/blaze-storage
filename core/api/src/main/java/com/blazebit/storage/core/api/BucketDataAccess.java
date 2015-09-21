package com.blazebit.storage.core.api;

import java.util.List;

import com.blazebit.persistence.QueryBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.model.jpa.Bucket;

public interface BucketDataAccess {
	
	public Bucket findByName(String bucketName);
	
	public <T> T findByName(String bucketName, EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting);
	
	public <T> T findByName(String bucketName, String prefix, Integer limit, String marker, EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting);

	public <T> List<T> findByAccountId(long accountId, EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting);

	public <T> List<T> findByAccountIdAndStorageName(long accountId, String storageName, EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting);

	public <T> List<T> findAll(EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting);
}
