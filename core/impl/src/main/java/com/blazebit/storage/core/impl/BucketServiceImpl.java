package com.blazebit.storage.core.impl;

import java.util.HashSet;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.blazebit.storage.core.api.BucketNotFoundException;
import com.blazebit.storage.core.api.BucketService;
import com.blazebit.storage.core.api.event.BucketDeletedEvent;
import com.blazebit.storage.core.model.jpa.Account;
import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.BucketObject;
import com.blazebit.storage.core.model.jpa.ObjectStatistics;
import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.jpa.StorageId;

@Stateless
public class BucketServiceImpl extends AbstractService implements BucketService {

	@Inject
	private Event<BucketDeletedEvent> bucketDeleted;
	
	@Override
	public void create(Bucket bucket) {
		bucket.setDeleted(false);
		bucket.setStatistics(new ObjectStatistics());
		bucket.setObjects(new HashSet<BucketObject>(0));
		bucket.setOwner(em.getReference(Account.class, bucket.getOwner().getId()));
		bucket.setStorage(em.getReference(Storage.class, new StorageId(bucket.getOwner(), bucket.getStorage().getId().getName())));
		
		em.persist(bucket);
		em.flush();
		// TODO: translate JPA/SQL exceptions to not found exceptions 
	}

	@Override
	public void updateStatistics(String bucketId, ObjectStatistics deltaStatistics) {
		int updated = em.createQuery("UPDATE Bucket b "
				+ "SET statistics.objectCount = statistics.objectCount + :objectCountDelta, "
				+ "statistics.objectBytes = statistics.objectBytes + :objectBytesDelta "
				+ "WHERE b.id = :bucketId")
			.setParameter("objectCountDelta", deltaStatistics.getObjectCount())
			.setParameter("objectBytesDelta", deltaStatistics.getObjectBytes())
			.setParameter("bucketId", bucketId)
			.executeUpdate();
		
		if (updated != 1) {
			throw new BucketNotFoundException("Bucket statistics update for '" + bucketId + "' failed. Expected to update 1 row but was " + updated);
		}
	}

	@Override
	public void delete(String bucketId) {
		int updated = em.createQuery("UPDATE Bucket b "
				+ "SET deleted = true "
				+ "WHERE b.id = :bucketId")
			.setParameter("bucketId", bucketId)
			.executeUpdate();
		
		if (updated != 1) {
			throw new BucketNotFoundException("Bucket deletion for '" + bucketId + "' failed. Expected to update 1 row but was " + updated);
		}
		
		bucketDeleted.fire(new BucketDeletedEvent(bucketId));
	}

}
