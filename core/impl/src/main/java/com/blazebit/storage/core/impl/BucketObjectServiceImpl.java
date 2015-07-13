package com.blazebit.storage.core.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;

import com.blazebit.storage.core.api.BucketObjectService;
import com.blazebit.storage.core.api.BucketService;
import com.blazebit.storage.core.model.jpa.BucketObject;

@Stateless
public class BucketObjectServiceImpl extends AbstractService implements BucketObjectService {
	
	@Inject
	private BucketObjectInternalService bucketObjectInternalService;

	@Override
	public void putObject(BucketObject bucketObject) {
//		bucketObjectInternalService.createObject(bucketObject);
		try {
			em.persist(bucketObject);
			em.flush();
			return;
		} catch (EntityExistsException ex) {
		}
		
		em.merge(bucketObject);
		em.flush();
	}

	@Override
	public void deleteObject(String bucketId, String objectName) {
		throw new UnsupportedOperationException("Deletion of bucket objects not yet supported!");
	}

}
