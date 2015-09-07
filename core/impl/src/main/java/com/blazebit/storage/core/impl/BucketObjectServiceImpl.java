package com.blazebit.storage.core.impl;

import java.io.InputStream;
import java.net.URI;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.blazebit.storage.core.api.BucketObjectService;
import com.blazebit.storage.core.api.BucketService;
import com.blazebit.storage.core.api.StorageProviderFactoryDataAccess;
import com.blazebit.storage.core.api.StorageService;
import com.blazebit.storage.core.api.event.BucketObjectDeletedEvent;
import com.blazebit.storage.core.api.spi.StorageProvider;
import com.blazebit.storage.core.api.spi.StorageProviderFactory;
import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.BucketObject;
import com.blazebit.storage.core.model.jpa.BucketObjectId;
import com.blazebit.storage.core.model.jpa.BucketObjectState;
import com.blazebit.storage.core.model.jpa.BucketObjectVersion;
import com.blazebit.storage.core.model.jpa.BucketObjectVersionId;
import com.blazebit.storage.core.model.jpa.ObjectStatistics;

@Stateless
public class BucketObjectServiceImpl extends AbstractService implements BucketObjectService {

	@Inject
	private BucketService bucketService;
	@Inject
	private StorageService storageService;
	@Inject
	private StorageProviderFactoryDataAccess storageProviderFactoryDataAccess;
	@Inject
	private Event<BucketObjectDeletedEvent> bucketObjectDeleted;

	@Override
	public String createContent(URI storageUri, InputStream inputStream) {
		StorageProviderFactory factory = storageProviderFactoryDataAccess.findByScheme(storageUri.getScheme());
		StorageProvider storageProvider = factory.createStorageProvider(factory.createConfiguration(storageUri));
		return storageProvider.createObject(inputStream);
	}

	@Override
	public void put(BucketObject bucketObject) {
		BucketObject currentObject = em.find(BucketObject.class, bucketObject.getId());
		BucketObjectVersion version = bucketObject.getContentVersion();
		
		// Fallback to create
		if (currentObject == null) {
			createObject(bucketObject);
			currentObject = bucketObject;
		}
		
		persistContentVersion(currentObject, version);
	}

	private void createObject(BucketObject bucketObject) {
		// 1. Persist empty bucket object
		bucketObject.setState(BucketObjectState.CREATING);
		bucketObject.setContentVersion(null);
		em.persist(bucketObject);
		em.flush();
	}
	
	private void persistContentVersion(BucketObject bucketObject, BucketObjectVersion version) {
		// 2. Persist bucket object version
		version.setState(BucketObjectState.CREATED);
		version.setId(new BucketObjectVersionId(bucketObject, UUID.randomUUID().toString()));
		em.persist(version);
		em.flush();
		
		// 3. Update storage statistics
		ObjectStatistics storageDeltaStatistics = new ObjectStatistics();
		storageDeltaStatistics.setObjectBytes(version.getContentLength());
		storageDeltaStatistics.setObjectCount(1);
		storageService.updateStatistics(version.getStorage().getId(), storageDeltaStatistics);

		// 4. Update bucket object to latest version
		bucketObject.setState(BucketObjectState.CREATED);
		bucketObject.setContentVersion(version);
		bucketObject.setContentVersionUuid(version.getId().getVersionUuid());
		bucketObject = em.merge(bucketObject);
			
		// 5. Update statistics
		ObjectStatistics bucketDeltaStatistics = new ObjectStatistics();
		bucketDeltaStatistics.setObjectBytes(version.getContentLength());
		bucketDeltaStatistics.setObjectCount(1);
		bucketService.updateStatistics(bucketObject.getId().getBucket().getId(), bucketDeltaStatistics);
		em.flush();
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
