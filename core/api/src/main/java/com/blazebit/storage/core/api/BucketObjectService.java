package com.blazebit.storage.core.api;

import com.blazebit.storage.core.model.jpa.BucketObject;
import com.blazebit.storage.core.model.jpa.BucketObjectId;

public interface BucketObjectService {

	public void put(BucketObject bucketObject);
	
	public void delete(BucketObjectId bucketObjectId);
}
