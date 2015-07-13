package com.blazebit.storage.core.api;

import java.util.List;

import com.blazebit.storage.core.model.jpa.Bucket;

public interface BucketDataAccess {
	
	public Bucket getBucket(String bucketName);

	public List<Bucket> getBuckets(long userAccountId);

	public List<Bucket> getBuckets(long userAccountId, String storageName);
}
