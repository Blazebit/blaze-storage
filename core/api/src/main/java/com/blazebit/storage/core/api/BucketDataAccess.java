package com.blazebit.storage.core.api;

import java.util.List;

import com.blazebit.storage.core.model.jpa.Bucket;

public interface BucketDataAccess {
	
	public Bucket findByName(String bucketName);

	public List<Bucket> findByUserAccountId(long userAccountId);

	public List<Bucket> findByUserAccountIdAndStorageName(long userAccountId, String storageName);
}
