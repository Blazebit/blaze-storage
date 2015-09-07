package com.blazebit.storage.core.api;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import com.blazebit.persistence.QueryBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.model.jpa.BucketObject;
import com.blazebit.storage.core.model.jpa.BucketObjectId;

public interface BucketObjectDataAccess {
	
	public InputStream getContent(URI storageUri, String contentKey);

	public BucketObject findById(BucketObjectId bucketObjectId);

	public <T> T findById(BucketObjectId bucketObjectId, EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting);
	
	public List<BucketObject> findByBucketIdAndPrefix(String bucketId, String prefix);
}
