package com.blazebit.storage.core.api;

import com.blazebit.storage.core.model.jpa.Bucket;

public interface BucketService {

	public void put(Bucket bucket);

	public void delete(String bucketId);
}
