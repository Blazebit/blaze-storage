package com.blazebit.storage.core.api;

import java.io.InputStream;
import java.net.URI;

import com.blazebit.storage.core.model.jpa.BucketObject;
import com.blazebit.storage.core.model.jpa.BucketObjectId;

public interface BucketObjectService {

	public String createContent(URI storageUri, InputStream inputStream);

	public void put(BucketObject bucketObject);
	
	public void delete(BucketObjectId bucketObjectId);
}
