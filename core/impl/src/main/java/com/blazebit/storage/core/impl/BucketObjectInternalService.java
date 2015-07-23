package com.blazebit.storage.core.impl;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;

import com.blazebit.storage.core.api.BucketService;
import com.blazebit.storage.core.model.jpa.BucketObject;
import com.blazebit.storage.core.model.jpa.BucketObjectState;
import com.blazebit.storage.core.model.jpa.BucketObjectVersion;
import com.blazebit.storage.core.model.jpa.BucketObjectVersionId;
import com.blazebit.storage.core.model.jpa.ObjectStatistics;

@Stateless
public class BucketObjectInternalService extends AbstractService {
	
	private static final Logger LOG = Logger.getLogger(BucketObjectInternalService.class.getName());

	@Inject
	private BucketService bucketService;

	public boolean createObject(BucketObject bucketObject) {
		BucketObjectVersion version;
		
		try {
			// 1. Persist empty bucket object
			version = bucketObject.getContentVersion();
			bucketObject.setState(BucketObjectState.CREATING);
			bucketObject.setContentVersion(null);
			em.persist(bucketObject);
			em.flush();
		} catch (EntityExistsException ex) {
			LOG.log(Level.SEVERE, "Error on persisting BucketObject", ex);
			return false;
		}
		
		return persistContentVersion(bucketObject, version);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public boolean updateObject(BucketObject bucketObject) {
		BucketObject currentObject = em.find(BucketObject.class, bucketObject.getId());
		
		// Fallback to create
		if (currentObject == null) {
			return createObject(bucketObject);
		}
		
		return persistContentVersion(currentObject, bucketObject.getContentVersion());
	}
	
	private boolean persistContentVersion(BucketObject bucketObject, BucketObjectVersion version) {
		try {
			// 2. Persist bucket object version
			version.setState(BucketObjectState.CREATED);
			version.setId(new BucketObjectVersionId(bucketObject, UUID.randomUUID().toString()));
			em.persist(version);
			em.flush();
		} catch (EntityExistsException ex) {
			LOG.log(Level.SEVERE, "Error on persisting BucketObjectVersion", ex);
			return false;
		}

		// 3. Update bucket object to latest version
		bucketObject.setState(BucketObjectState.CREATED);
		bucketObject.setContentVersion(version);
		bucketObject = em.merge(bucketObject);
			
		// 4. Update statistics
		ObjectStatistics deltaStatistics = new ObjectStatistics();
		deltaStatistics.setObjectBytes(bucketObject.getContentVersion().getContentLength());
		deltaStatistics.setObjectCount(1);
		bucketService.updateBucketStatistics(bucketObject.getId().getBucket().getId(), deltaStatistics);
		em.flush();
		return true;
	}
}
