package com.blazebit.storage.core.impl;

import java.io.InputStream;
import java.net.URI;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.blazebit.storage.core.api.BucketObjectService;
import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.event.BucketObjectDeletedEvent;
import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.BucketObject;
import com.blazebit.storage.core.model.jpa.BucketObjectId;
import com.blazebit.storage.core.model.jpa.BucketObjectState;

@Stateless
public class BucketObjectServiceImpl extends AbstractService implements BucketObjectService {
	
	@Inject
	private BucketObjectInternalService bucketObjectInternalService;
	@Inject
	private Event<BucketObjectDeletedEvent> bucketObjectDeleted;

	@Override
	public URI createContent(URI storageUri, String key, InputStream inputStream) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void put(BucketObject bucketObject) {
		if (bucketObjectInternalService.createObject(bucketObject)) {
			return;
		}
		
		if (bucketObjectInternalService.updateObject(bucketObject)) {
			return;
		}
		
		throw new StorageException("Could neither create or update the bucket object: " + bucketObject);
	}

	@Override
	public void delete(BucketObjectId bucketObjectId) {
		String bucketId = bucketObjectId.getBucket().getId();
		String objectName = bucketObjectId.getName();
		int updatedObjects = em.createQuery("UPDATE BucketObject SET state = :state WHERE id.bucket.id = :bucketId AND id.name = :objectName")
				.setParameter("state", BucketObjectState.REMOVING)
				.setParameter("bucketId", bucketId)
				.setParameter("objectName", objectName)
				.executeUpdate();
		int updatedVersions = em.createQuery("UPDATE BucketObjectVersion SET state = :state WHERE object.id.bucket.id = :bucketId AND object.id.name = :objectName")
				.setParameter("state", BucketObjectState.REMOVING)
				.setParameter("bucketId", bucketId)
				.setParameter("objectName", objectName)
				.executeUpdate();
		bucketObjectDeleted.fire(new BucketObjectDeletedEvent(new BucketObjectId(new Bucket(bucketId), objectName)));
	}

}
