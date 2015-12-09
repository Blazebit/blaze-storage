package com.blazebit.storage.core.api;

import java.io.InputStream;
import java.net.URI;

import com.blazebit.persistence.QueryBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.model.jpa.BucketObject;
import com.blazebit.storage.core.model.jpa.BucketObjectId;

public interface BucketObjectDataAccess {
	
	/**
	 * 
	 * @param storageUri
	 * @param contentKey
	 * @return the input stream to the file if it exists, never null
	 * @throws StorageException when the file does not exist or cannot be accessed
	 */
	public InputStream getContent(URI storageUri, String contentKey);

	public BucketObject findById(BucketObjectId bucketObjectId);

	public <T> T findById(BucketObjectId bucketObjectId, EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting);
}
