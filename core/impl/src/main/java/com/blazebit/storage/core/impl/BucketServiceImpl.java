package com.blazebit.storage.core.impl;

import java.util.HashSet;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.LockModeType;

import com.blazebit.storage.core.api.AccountNotFoundException;
import com.blazebit.storage.core.api.BucketNotEmptyException;
import com.blazebit.storage.core.api.BucketNotFoundException;
import com.blazebit.storage.core.api.BucketService;
import com.blazebit.storage.core.api.StorageNotFoundException;
import com.blazebit.storage.core.api.event.BucketDeletedEvent;
import com.blazebit.storage.core.model.jpa.Account;
import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.BucketObject;
import com.blazebit.storage.core.model.jpa.ObjectStatistics;
import com.blazebit.storage.core.model.jpa.Storage;

@Stateless
public class BucketServiceImpl extends AbstractService implements BucketService {

	@Inject
	private Event<BucketDeletedEvent> bucketDeleted;
	
	@Override
	public void put(Bucket bucket) {
		if (em.contains(bucket)) {
			em.detach(bucket);
		}
		
		Bucket currentBucket = em.find(Bucket.class, bucket.getId(), LockModeType.PESSIMISTIC_WRITE);
		
		// Fallback to create
		if (currentBucket == null) {
			createObject(bucket);
			return;
		}

		// TODO: check access rights?
		
		Storage storage = em.find(Storage.class, bucket.getStorage().getId());
		
		if (storage == null) {
			throw new StorageNotFoundException("Storage not found!");
		}
		
		currentBucket.setStorage(storage);
		em.merge(currentBucket);
		em.flush();
	}

	private void createObject(Bucket bucket) {
		bucket.setDeleted(false);
		bucket.setStatistics(new ObjectStatistics());
		bucket.setObjects(new HashSet<BucketObject>(0));
		
		Account owner = em.find(Account.class, bucket.getOwner().getId());
		
		if (owner == null) {
			throw new AccountNotFoundException("Account not found!");
		}
		
		bucket.setOwner(owner);
		
		Storage storage = em.find(Storage.class, bucket.getStorage().getId());
		
		if (storage == null) {
			throw new StorageNotFoundException("Storage not found!");
		}
		
		bucket.setStorage(storage);
		
		em.persist(bucket);
		em.flush();
	}

	@Override
	public void delete(String bucketId) {
		Bucket bucket = em.find(Bucket.class, bucketId, LockModeType.PESSIMISTIC_WRITE);
		
		// TODO: check access rights?
		
		if (bucket == null) {
			throw new BucketNotFoundException("Bucket '" + bucketId + "' does not exist!");
		}
		
		if (Boolean.TRUE.equals(bucket.getDeleted())) {
			// If it is already deleted, everything is ok
			throw new BucketNotFoundException("Bucket '" + bucketId + "' does not exist!");
		}
		
		if (bucket.getStatistics().getObjectCount() != 0L) {
			throw new BucketNotEmptyException("Can not delete not empty bucket!");
		}
		
		if (bucket.getStatistics().getPendingObjectCount() == 0L) {
			em.remove(bucket);
		} else {
			bucket.setDeleted(true);
			em.merge(bucket);
		}

		em.flush();
		
		bucketDeleted.fire(new BucketDeletedEvent(bucketId));
	}

}
