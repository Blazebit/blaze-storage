package com.blazebit.storage.core.api;

import com.blazebit.storage.core.model.jpa.BucketObject;

public interface BucketObjectService {

	public void putObject(BucketObject bucketObject);
	
	public void deleteObject(String bucketId, String objectName);
}
