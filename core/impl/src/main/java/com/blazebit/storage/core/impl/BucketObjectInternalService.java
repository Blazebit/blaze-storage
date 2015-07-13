package com.blazebit.storage.core.impl;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.OptimisticLockException;

import com.blazebit.storage.core.api.BucketService;
import com.blazebit.storage.core.model.jpa.BucketObject;

@Stateless
public class BucketObjectInternalService extends AbstractService {

	@Inject
	private BucketService bucketService;

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean createObject(BucketObject bucketObject) {
		try {
			em.persist(bucketObject);
			em.flush();
//			bucketService.updateBucketStatistics(bucketObject.getId().getBucket().getId(), deltaStatistics);
			return true;
		} catch (EntityExistsException ex) {
			return false;
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean updateObject(BucketObject bucketObject) {
		BucketObject currentObject = em.find(BucketObject.class, bucketObject.getId());
		
		// Fallback to create
		if (currentObject == null) {
			return createObject(bucketObject);
		}
		
		currentObject.setVersion(bucketObject.getVersion());
		
		try {
			em.flush();
			return true;
		} catch (OptimisticLockException ex) {
			return false;
		}
	}
}
