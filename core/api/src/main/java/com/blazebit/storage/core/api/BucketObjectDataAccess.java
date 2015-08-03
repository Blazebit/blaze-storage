package com.blazebit.storage.core.api;

import java.util.List;

import com.blazebit.storage.core.model.jpa.BucketObject;
import com.blazebit.storage.core.model.jpa.BucketObjectId;

public interface BucketObjectDataAccess {

	public BucketObject findById(BucketObjectId buckeObjectId);
	
	public List<BucketObject> findByBucketIdAndPrefix(String bucketId, String prefix);
}
