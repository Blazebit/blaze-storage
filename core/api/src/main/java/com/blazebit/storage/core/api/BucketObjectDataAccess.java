package com.blazebit.storage.core.api;

import java.util.List;

import com.blazebit.storage.core.model.jpa.BucketObject;

public interface BucketObjectDataAccess {

	public BucketObject getObject(String bucketId, String name);
	
	public List<BucketObject> getObjects(String bucketId, String prefix);
}
