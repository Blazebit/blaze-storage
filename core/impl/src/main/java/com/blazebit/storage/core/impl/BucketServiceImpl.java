package com.blazebit.storage.core.impl;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.blazebit.storage.core.api.BucketService;
import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.event.BucketDeletedEvent;
import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.ObjectStatistics;

@Stateless
public class BucketServiceImpl extends AbstractService implements BucketService {

	@Inject
	private Event<BucketDeletedEvent> bucketDeleted;
	
	@Override
	public void create(Bucket bucket) {
		em.persist(bucket);
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
			throw new StorageException("Bucket statistics update failed. Expected to update 1 row but was " + updated);
		}
	}

	@Override
	public void delete(String bucketId) {
		int updated = em.createQuery("UPDATE Bucket b "
				+ "SET deleted = true "
				+ "WHERE b.id = :bucketId")
			.setParameter("bucketId", bucketId)
			.executeUpdate();
		
		bucketDeleted.fire(new BucketDeletedEvent(bucketId));
	}

}
