package com.blazebit.storage.core.impl;

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
	public void putObject(BucketObject bucketObject) {
		if (bucketObjectInternalService.createObject(bucketObject)) {
			return;
		}
		
		if (bucketObjectInternalService.updateObject(bucketObject)) {
			return;
		}
		
		throw new StorageException("Could neither create or update the bucket object: " + bucketObject);
	}

	@Override
	public void deleteObject(String bucketId, String objectName) {
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
