package com.blazebit.storage.core.impl;

import javax.ejb.Stateless;

import com.blazebit.storage.core.api.BucketService;
import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.BucketStatistics;

@Stateless
public class BucketServiceImpl extends AbstractService implements BucketService {

	@Override
	public void createBucket(Bucket bucket) {
		em.persist(bucket);
	}

	@Override
	public void updateBucketStatistics(String bucketId, BucketStatistics deltaStatistics) {		
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
	public void deleteBucket(String bucketName) {
		throw new UnsupportedOperationException("Deletion of buckets not yet supported!");
	}

}
