package com.blazebit.storage.core.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.storage.core.api.BucketObjectDataAccess;
import com.blazebit.storage.core.model.jpa.BucketObject;
import com.blazebit.storage.core.model.jpa.BucketObjectId;
import com.blazebit.storage.core.model.jpa.BucketObjectState;

@Stateless
public class BucketObjectDataAccessImpl extends AbstractDataAccess implements BucketObjectDataAccess {

	@Override
	public BucketObject findById(BucketObjectId bucketObjectId) {
		try {
			return cbf.create(em, BucketObject.class)
					.where("id").eq(bucketObjectId)
					.where("state").eq(BucketObjectState.CREATED)
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	@Override
	public List<BucketObject> findByBucketIdAndPrefix(String bucketId, String prefix) {
		CriteriaBuilder<BucketObject> cb = cbf.create(em, BucketObject.class)
				.where("id.bucket.id").eq(bucketId)
				.where("state").eq(BucketObjectState.CREATED)
				.orderByAsc("id.name");
		
		if (prefix != null && !(prefix = prefix.trim()).isEmpty()) {
			prefix = prefix.replaceAll("%", "\\%") + "%";
			cb.where("id.name").like().value(prefix).escape('\\');
		}
		
		return cb.getResultList();
	}

}