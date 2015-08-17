package com.blazebit.storage.core.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import com.blazebit.storage.core.api.BucketDataAccess;
import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.BucketObject;

@Stateless
public class BucketDataAccessImpl extends AbstractDataAccess implements BucketDataAccess {

	@Override
	public Bucket findByName(String bucketName) {
		try {
			return cbf.create(em, Bucket.class)
					.where("id").eq(bucketName)
					.where("deleted").eqExpression("false")
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	@Override
	public List<Bucket> findByAccountId(long accountId) {
		return cbf.create(em, Bucket.class)
				.where("owner.id").eq(accountId)
				.where("deleted").eqExpression("false")
				.getResultList();
	}

	@Override
	public List<Bucket> findByAccountIdAndStorageName(long accountId, String storageName) {
		return cbf.create(em, Bucket.class)
				.from(BucketObject.class)
				.where("id.bucket.owner.id").eq(accountId)
				.where("storage.id.name").eq(storageName)
				.where("deleted").eqExpression("false")
				.distinct()
				.select("id.bucket")
				.getResultList();
	}

}
