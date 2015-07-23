package com.blazebit.storage.core.api.actor;

import com.blazebit.storage.core.model.jpa.BucketObjectId;

public interface BucketObjectDeleter {

	public void addBucketObject(BucketObjectId bucketObjectId);
}
