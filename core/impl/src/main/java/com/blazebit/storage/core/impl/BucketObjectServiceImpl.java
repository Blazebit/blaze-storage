package com.blazebit.storage.core.impl;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.LockModeType;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.storage.core.api.BucketNotFoundException;
import com.blazebit.storage.core.api.BucketObjectNotFoundException;
import com.blazebit.storage.core.api.BucketObjectService;
import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.StorageNotFoundException;
import com.blazebit.storage.core.api.StorageProviderFactoryDataAccess;
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
import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.service.BucketObjectDeleteReport;
import com.blazebit.storage.core.model.service.BucketObjectDeleteReportItem;

@Stateless
public class BucketObjectServiceImpl extends AbstractService implements BucketObjectService {

	@Inject
	private StorageProviderFactoryDataAccess storageProviderFactoryDataAccess;
	@Inject
	private Event<BucketObjectDeletedEvent> bucketObjectDeleted;
	@Inject
	private CriteriaBuilderFactory cbf;

	@Override
	public String createContent(URI storageUri, InputStream inputStream) {
		StorageProviderFactory factory = storageProviderFactoryDataAccess.findByScheme(storageUri.getScheme());
		StorageProvider storageProvider = factory.createStorageProvider(factory.createConfiguration(storageUri));
		return storageProvider.createObject(inputStream);
	}

	@Override
	public String copyContent(URI sourceStorageUri, String sourceContentKey, URI targetStorageUri) {
		StorageProviderFactory factory = storageProviderFactoryDataAccess.findByScheme(targetStorageUri.getScheme());
		StorageProvider storageProvider = factory.createStorageProvider(factory.createConfiguration(targetStorageUri));
		StorageProviderFactory sourceFactory = storageProviderFactoryDataAccess.findByScheme(sourceStorageUri.getScheme());
		StorageProvider sourceStorageProvider = sourceFactory.createStorageProvider(sourceFactory.createConfiguration(sourceStorageUri));
		
		return storageProvider.copyObject(sourceStorageProvider, sourceContentKey);
	}

	@Override
	public void deleteContent(URI storageUri, String contentKey) {
		StorageProviderFactory factory = storageProviderFactoryDataAccess.findByScheme(storageUri.getScheme());
		StorageProvider storageProvider = factory.createStorageProvider(factory.createConfiguration(storageUri));
		storageProvider.deleteObject(contentKey);
	}

	@Override
	public void put(BucketObject bucketObject) {
		List<BucketObject> results = cbf.create(em, BucketObject.class)
			.fetch("contentVersion.storage")
			.fetch("bucket.storage")
			.where("id.bucketId").eq(bucketObject.getId().getBucketId())
			.where("id.name").eq(bucketObject.getId().getName())
			.getQuery()
			.setLockMode(LockModeType.PESSIMISTIC_WRITE)
			.getResultList();
		
		// Need to retrieve the version here because create might make the reference null
		BucketObjectVersion version = bucketObject.getContentVersion();
		BucketObject currentObject;
		
		// Fallback to create
		if (results.isEmpty()) {
			createObject(bucketObject);
			currentObject = bucketObject;
		} else {
			currentObject = results.get(0);
		}
		
		persistContentVersion(currentObject, version);
	}

	private void createObject(BucketObject bucketObject) {
		List<Bucket> results = cbf.create(em, Bucket.class)
				.fetch("storage")
				.where("id").eq(bucketObject.getId().getBucketId())
				.getQuery()
				.setLockMode(LockModeType.PESSIMISTIC_WRITE)
				.getResultList();
		
		if (results.isEmpty()) {
			throw new BucketNotFoundException("Bucket not found!");
		}
		
		Bucket bucket = results.get(0);
		// 1. Persist empty bucket object
		bucketObject.setBucket(bucket);
		bucketObject.setState(BucketObjectState.CREATING);
		bucketObject.setContentVersion(null);
		bucketObject.setContentVersionUuid(null);
		em.persist(bucketObject);
		em.flush();
	}
	
	private void persistContentVersion(BucketObject bucketObject, BucketObjectVersion version) {
		// 2. Persist bucket object version
		version.setState(BucketObjectState.CREATED);
		if (version.getId() == null || version.getId().getVersionUuid() == null || version.getId().getBucketObjectName() == null || version.getId().getBucketId() == null) {
			version.setId(new BucketObjectVersionId(bucketObject, UUID.randomUUID().toString()));
		}
		
		if (!version.getId().getBucketId().equals(bucketObject.getId().getBucketId()) || !version.getId().getBucketObjectName().equals(bucketObject.getId().getName())) {
			throw new IllegalArgumentException("Invalid bucket object version [" + version + "] that is referencing different bucket object than was given [" + bucketObject + "]!");
		}
		
		if (version.getBucketObject() == null) {
			version.setBucketObject(bucketObject);
		}
		if (!version.getBucketObject().getId().getName().equals(version.getId().getBucketObjectName()) || !version.getBucketObject().getId().getBucketId().equals(version.getId().getBucketId())) {
			throw new IllegalArgumentException("Invalid bucket object version [" + version + "] that is referencing different bucket object than was given [" + bucketObject + "]!");
		}
		
		if (version.getStorage() == null) {
			version.setStorage(bucketObject.getBucket().getStorage());
		}
		if (!em.contains(version.getStorage())) {
			Storage storage = em.find(Storage.class, version.getStorage().getId(), LockModeType.PESSIMISTIC_WRITE);
			
			if (storage == null) {
				throw new StorageNotFoundException("Storage not found!");
			}
			
			version.setStorage(storage);
		}
		
		em.persist(version);
		em.flush();
		
		ObjectStatistics defaultDeltaStatistics = new ObjectStatistics();
		defaultDeltaStatistics.setObjectVersionBytes(version.getContentLength());
		defaultDeltaStatistics.setObjectVersionCount(1);
		defaultDeltaStatistics.setPendingObjectVersionBytes(version.getContentLength());
		defaultDeltaStatistics.setPendingObjectVersionCount(1);
		ObjectStatistics storageDeltaStatistics = defaultDeltaStatistics;
		ObjectStatistics bucketDeltaStatistics = defaultDeltaStatistics;
		
		if (bucketObject.getContentVersion() == null) {
			defaultDeltaStatistics.setObjectBytes(version.getContentLength());
			defaultDeltaStatistics.setObjectCount(1L);
			defaultDeltaStatistics.setPendingObjectBytes(version.getContentLength());
			defaultDeltaStatistics.setPendingObjectCount(1L);
		} else {
			defaultDeltaStatistics.setObjectBytes(version.getContentLength() - bucketObject.getContentVersion().getContentLength());
			defaultDeltaStatistics.setObjectCount(0L);
			defaultDeltaStatistics.setPendingObjectBytes(version.getContentLength() - bucketObject.getContentVersion().getContentLength());
			defaultDeltaStatistics.setPendingObjectCount(0L);
			
			if (!bucketObject.getContentVersion().getStorage().equals(version.getStorage())) {
				// We don't add a 
				bucketDeltaStatistics = defaultDeltaStatistics.copy();
				// If the storage changes, we move object statistics from the old storage to the new one
				storageDeltaStatistics.setObjectBytes(version.getContentLength());
				storageDeltaStatistics.setObjectCount(1L);
				storageDeltaStatistics.setPendingObjectBytes(version.getContentLength());
				storageDeltaStatistics.setPendingObjectCount(1L);
				
				ObjectStatistics oldStorageDeltaStatistics = new ObjectStatistics(); 
				oldStorageDeltaStatistics.setObjectBytes(bucketObject.getContentVersion().getContentLength());
				oldStorageDeltaStatistics.setObjectCount(1L);
				oldStorageDeltaStatistics.setPendingObjectBytes(bucketObject.getContentVersion().getContentLength());
				oldStorageDeltaStatistics.setPendingObjectCount(1L);
				
				Storage oldStorage = bucketObject.getContentVersion().getStorage();
				oldStorage.setStatistics(oldStorage.getStatistics().minus(oldStorageDeltaStatistics));
			}
		}
		
		// 3. Update storage statistics
		version.getStorage().setStatistics(version.getStorage().getStatistics().plus(storageDeltaStatistics));

		// 4. Update bucket object to latest version
		bucketObject.setState(BucketObjectState.CREATED);
		bucketObject.setContentVersion(version);
		bucketObject.setContentVersionUuid(version.getId().getVersionUuid());
		bucketObject = em.merge(bucketObject);
			
		// 5. Update statistics
		bucketObject.getBucket().setStatistics(bucketObject.getBucket().getStatistics().plus(bucketDeltaStatistics));
		
		em.flush();
	}

	@Override
	public void delete(BucketObjectId bucketObjectId) {
		Map<String, List<String>> deletedBucketObjectVersions = delete(bucketObjectId.getBucketId(), Arrays.asList(bucketObjectId.getName()));
		
		if (deletedBucketObjectVersions.isEmpty()) {
			throw new BucketObjectNotFoundException("Could not mark bucket object [" + bucketObjectId + "] as deleted because it doesn't exist!");
		}
		
		// TODO: don't know what to do with that
//		bucketObjectDeleted.fire(new BucketObjectDeletedEvent(new BucketObjectId(new Bucket(bucketId), objectName)));
	}

	@Override
	public BucketObjectDeleteReport delete(List<BucketObjectId> bucketObjectIds) {
		Map<String, List<String>> groupedBucketObjectIds = new HashMap<>();
		
		for (BucketObjectId id : bucketObjectIds) {
			String key = id.getBucketId();
			List<String> objectNames = groupedBucketObjectIds.get(key);
			
			if (objectNames == null) {
				objectNames = new ArrayList<>();
				groupedBucketObjectIds.put(key, objectNames);
			}
			
			// Skip duplicates
			if (!objectNames.contains(id.getName())) {
				objectNames.add(id.getName());
			}
		}
		
		List<BucketObjectDeleteReportItem> items = new ArrayList<>(bucketObjectIds.size());
		
		for (Map.Entry<String, List<String>> entry : groupedBucketObjectIds.entrySet()) {
			Bucket bucket = new Bucket(entry.getKey());
			List<String> bucketObjectNames = entry.getValue();
			Map<String, List<String>> deletedBucketObjectVersions = delete(entry.getKey(), bucketObjectNames);
			
			for (String bucketObjectName : bucketObjectNames) {
				List<String> deletedVersionUuids = deletedBucketObjectVersions.get(bucketObjectName);
				if (deletedVersionUuids != null) {
					for (String versionUuid : deletedVersionUuids) {
						items.add(BucketObjectDeleteReportItem.deleted(new BucketObjectId(bucket, bucketObjectName), versionUuid));
					}
				} else {
					items.add(BucketObjectDeleteReportItem.error(new BucketObjectId(bucket, bucketObjectName), "BucketObjectNotFound", "not found"));
				}
			}
		}

		BucketObjectDeleteReport report = new BucketObjectDeleteReport(items);
		return report;
	}
	
	private Map<String, List<String>> delete(String bucketId, List<String> bucketObjectNames) {
		if (bucketObjectNames == null || bucketObjectNames.isEmpty()) {
			return Collections.emptyMap();
		}
		
		Bucket bucket = em.find(Bucket.class, bucketId, LockModeType.PESSIMISTIC_WRITE);
		
		// TODO: check access rights?
		
		if (bucket == null) {
			throw new BucketNotFoundException("Bucket '" + bucketId + "' does not exist!");
		}
		
		List<BucketObjectVersion> bucketObjectVersions = cbf.create(em, BucketObjectVersion.class)
				.fetch("storage")
				.fetch("bucketObject")
				.where("id.bucketId").eq(bucketId)
				.where("state").eq(BucketObjectState.CREATED)
				.where("id.bucketObjectName").in(bucketObjectNames)
				.getQuery()
				.setLockMode(LockModeType.PESSIMISTIC_WRITE)
				.getResultList();
			
		if (bucketObjectVersions.size() == 0) {
			return Collections.emptyMap();
		}
		
		return deleteBucketObjectVersions(bucket, bucketObjectVersions, true);
	}
	
	private Map<String, List<String>> deleteBucketObjectVersions(Bucket bucket, List<BucketObjectVersion> bucketObjectVersions, boolean deleteBucketObjects) {
		// ObjectName -> List<VersionUuid>
		Map<String, List<String>> deletedBucketObjectVersions = new HashMap<>(bucketObjectVersions.size());
		// ObjectName -> Set<Storage>
		Map<String, Set<Storage>> bucketObjectStorages = new HashMap<>(bucketObjectVersions.size());
		long deletedBucketObjectBytes = 0;
		
		for (BucketObjectVersion bucketObjectVersion : bucketObjectVersions) {
			String key = bucketObjectVersion.getId().getBucketObjectName();
			List<String> versionIds = deletedBucketObjectVersions.get(key);
			
			if (versionIds == null) {
				versionIds = new ArrayList<>();
				deletedBucketObjectVersions.put(key, versionIds);
			}
			
			versionIds.add(bucketObjectVersion.getId().getVersionUuid());
			bucketObjectVersion.setState(BucketObjectState.REMOVING);
			
			if (deleteBucketObjects) {
				bucketObjectVersion.getBucketObject().setState(BucketObjectState.REMOVING);
			}
			
			// Update bucket statistics
			ObjectStatistics versionDeltaStatistics = new ObjectStatistics();
			versionDeltaStatistics.setObjectVersionBytes(bucketObjectVersion.getContentLength());
			versionDeltaStatistics.setObjectVersionCount(1L);
			bucket.setStatistics(bucket.getStatistics().minus(versionDeltaStatistics));

			// Update storage statistics
			Storage storage = bucketObjectVersion.getStorage();
			Set<Storage> storages = bucketObjectStorages.get(key);
			
			if (storages == null) {
				storages = new HashSet<>();
				bucketObjectStorages.put(key, storages);

				if (deleteBucketObjects) {
					// We must use the content length of the current version
					long currentContentLength = bucketObjectVersion.getBucketObject().getContentVersion().getContentLength();
					deletedBucketObjectBytes += currentContentLength;
					
					// The object statistics are only relevant for the storage of the current version
					Storage currentVersionStorage = bucketObjectVersion.getBucketObject().getContentVersion().getStorage();
					ObjectStatistics currentVersionStorageStatistics = new ObjectStatistics();
					currentVersionStorageStatistics.setObjectCount(1L);
					currentVersionStorageStatistics.setObjectBytes(currentContentLength);
					currentVersionStorage.setStatistics(currentVersionStorage.getStatistics().minus(currentVersionStorageStatistics));
				}
			}
			
			storage.setStatistics(storage.getStatistics().minus(versionDeltaStatistics));
		}
		
		// Update bucket statistics
		if (deleteBucketObjects) {
			bucket.getStatistics().setObjectCount(bucket.getStatistics().getObjectCount() - bucketObjectStorages.size());
			bucket.getStatistics().setObjectBytes(bucket.getStatistics().getObjectBytes() - deletedBucketObjectBytes);
		}
		
		return deletedBucketObjectVersions;
	}

	@Override
	public void deleteVersion(BucketObjectVersionId bucketObjectVersionId) {
		Map<String, List<String>> deletedBucketObjectVersions = deleteVersions(bucketObjectVersionId.getBucketId(), Collections.singletonMap(bucketObjectVersionId.getBucketObjectName(), Collections.singleton(bucketObjectVersionId.getVersionUuid())), Arrays.asList(bucketObjectVersionId.getVersionUuid()));

		if (deletedBucketObjectVersions.isEmpty()) {
			throw new BucketObjectNotFoundException("Could not mark bucket object version [" + bucketObjectVersionId + "] as deleted because it does not exist!");
		}
		
		// TODO: don't know what to do with that
//		bucketObjectDeleted.fire(new BucketObjectDeletedEvent(new BucketObjectId(new Bucket(bucketId), objectName)));
	}

	@Override
	public BucketObjectDeleteReport deleteVersions(List<BucketObjectVersionId> bucketObjectVersionIds) {
		// Map<BucketId, Map<BucketObjectName, List<VersionUuid>>>
		Map<String, Map<String, Set<String>>> groupedBucketObjectVersionIds = new HashMap<>();
		Map<String, List<String>> groupedVersionUuids = new HashMap<>();
		
		for (BucketObjectVersionId id : bucketObjectVersionIds) {
			String key = id.getBucketId();
			Map<String, Set<String>> bucketObjectVersions = groupedBucketObjectVersionIds.get(key);
			
			if (bucketObjectVersions == null) {
				bucketObjectVersions = new HashMap<String, Set<String>>();
				groupedBucketObjectVersionIds.put(key, bucketObjectVersions);
			}

			String objectKey = id.getBucketObjectName();
			Set<String> versionIds = bucketObjectVersions.get(objectKey);
			
			if (versionIds == null) {
				versionIds = new HashSet<>();
				bucketObjectVersions.put(objectKey, versionIds);
			}
			
			// Skip duplicates
			String uuid = id.getVersionUuid();
			if (versionIds.add(uuid)) {
				
				List<String> uuids = groupedVersionUuids.get(key);
				
				if (uuids == null) {
					uuids = new ArrayList<>();
					groupedVersionUuids.put(key, uuids);
				}
				
				uuids.add(uuid);
			}
		}
		
		List<BucketObjectDeleteReportItem> items = new ArrayList<>(bucketObjectVersionIds.size());
		
		for (Map.Entry<String, Map<String, Set<String>>> entry : groupedBucketObjectVersionIds.entrySet()) {
			List<String> versionUuids = groupedVersionUuids.get(entry.getKey());
			Map<String, List<String>> deletedBucketObjectVersions = deleteVersions(entry.getKey(), entry.getValue(), versionUuids);
			
			for (BucketObjectVersionId versionId : bucketObjectVersionIds) {
				// Skip versions that don't belong to our bucket
				if (entry.getKey().equals(versionId.getBucketId())) {
					List<String> deletedVersionUuids = deletedBucketObjectVersions.get(versionId.getBucketObjectName());
					if (deletedVersionUuids != null) {
						if (deletedVersionUuids.contains(versionId.getVersionUuid())) {
							items.add(BucketObjectDeleteReportItem.deleted(versionId.toBucketObjectId(), versionId.getVersionUuid()));
						}
					} else {
						items.add(BucketObjectDeleteReportItem.error(versionId.toBucketObjectId(), versionId.getVersionUuid(), "BucketObjectVersionNotFound", "not found"));
					}
				}
			}
		}

		BucketObjectDeleteReport report = new BucketObjectDeleteReport(items);
		return report;
	}
	
	private Map<String, List<String>> deleteVersions(String bucketId, Map<String, Set<String>> bucketObjectVersionIds, List<String> versionUuids) {
		if (bucketObjectVersionIds == null || bucketObjectVersionIds.isEmpty()) {
			return Collections.emptyMap();
		}
		
		Bucket bucket = em.find(Bucket.class, bucketId, LockModeType.PESSIMISTIC_WRITE);
		
		// TODO: check access rights?
		
		if (bucket == null) {
			throw new BucketNotFoundException("Bucket '" + bucketId + "' does not exist!");
		}
		
		List<BucketObjectVersion> bucketObjectVersions = cbf.create(em, BucketObjectVersion.class)
				.fetch("storage")
				.fetch("bucketObject.bucket")
				.where("id.bucketId").eq(bucketId)
				// We fetch by version uuid and filter out manually
//				.where("id.bucketObjectName").eq(objectName)
				.where("state").eq(BucketObjectState.CREATED)
				.where("id.versionUuid").in(versionUuids)
				.getQuery()
				.setLockMode(LockModeType.PESSIMISTIC_WRITE)
				.getResultList();
		
		if (bucketObjectVersionIds.isEmpty()) {
			return Collections.emptyMap();
		}
		
		for (ListIterator<BucketObjectVersion> iter = bucketObjectVersions.listIterator(); iter.hasNext();) {
			BucketObjectVersion bucketObjectVersion = iter.next();
			Set<String> allowedVersionUuids = bucketObjectVersionIds.get(bucketObjectVersion.getId().getBucketObjectName());
			
			if (allowedVersionUuids == null || !allowedVersionUuids.contains(bucketObjectVersion.getId().getVersionUuid())) {
				iter.remove();
				continue;
			}
			
			if (bucketObjectVersion.getBucketObject().getContentVersionUuid().equals(bucketObjectVersion.getId().getVersionUuid())) {
				throw new StorageException("Can not delete the current version of the bucket object [" + bucketObjectVersion.getId().toBucketObjectId() + "]!");
			}
		}
		
		return deleteBucketObjectVersions(bucket, bucketObjectVersions, false);
	}

}
