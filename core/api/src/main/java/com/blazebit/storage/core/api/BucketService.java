package com.blazebit.storage.core.api;

import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.BucketStatistics;

public interface BucketService {

	public void createBucket(Bucket bucket);
	
	public void updateBucketStatistics(String bucketId, BucketStatistics deltaStatistics);

	public void deleteBucket(String bucketId);
}
