package com.blazebit.storage.core.api;

import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.ObjectStatistics;

public interface BucketService {

	public void put(Bucket bucket);
	
	public void updateStatistics(String bucketId, ObjectStatistics deltaStatistics);

	public void delete(String bucketId);
}
