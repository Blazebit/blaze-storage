package com.blazebit.storage.core.api;

import java.util.List;

import com.blazebit.storage.core.model.jpa.Bucket;

public interface BucketDataAccess {
	
	public Bucket findByName(String bucketName);

	public List<Bucket> findByAccountId(long accountId);

	public List<Bucket> findByAccountIdAndStorageName(long accountId, String storageName);
}
