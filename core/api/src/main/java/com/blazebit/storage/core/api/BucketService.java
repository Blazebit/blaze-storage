package com.blazebit.storage.core.api;

import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.ObjectStatistics;

public interface BucketService {

	public void createBucket(Bucket bucket);
	
	public void updateBucketStatistics(String bucketId, ObjectStatistics deltaStatistics);

	public void deleteBucket(String bucketId);
}
