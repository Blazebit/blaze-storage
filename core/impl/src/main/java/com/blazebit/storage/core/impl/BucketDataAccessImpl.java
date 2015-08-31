package com.blazebit.storage.core.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.QueryBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
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
	public <T> T findByName(String bucketName, EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting) {
		try {
			CriteriaBuilder<Bucket> cb =  cbf.create(em, Bucket.class)
					.where("id").eq(bucketName)
					.where("deleted").eqExpression("false");
			return evm.applySetting(setting, cb).getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}
	
	@Override
	public <T> T findByName(String bucketName, String prefix, Integer limit, String marker, EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting) {
		if (limit == null) {
			limit = 1000;
		} else if (limit > 1000) {
			throw new IllegalArgumentException("Limit may not exceed 1000!");
		}
		
		try {
			CriteriaBuilder<Bucket> cb =  cbf.create(em, Bucket.class)
					.where("id").eq(bucketName)
					.where("deleted").eqExpression("false");
			
			if (prefix != null && !prefix.isEmpty()) {
				cb.where("objects.id.name").like().value(prefix.replaceAll("%", "\\%") + "%").escape('\\');
			}
			
			// TODO: implement limit and marker in query and also for passing into entity views
			setting.addOptionalParameter("prefix", prefix);
			setting.addOptionalParameter("limit", limit);
			setting.addOptionalParameter("marker", marker);
			return evm.applySetting(setting, cb).getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	@Override
	public <T> List<T> findByAccountId(long accountId, EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting) {
		CriteriaBuilder<Bucket> cb = cbf.create(em, Bucket.class)
				.where("owner.id").eq(accountId)
				.where("deleted").eqExpression("false");
		return evm.applySetting(setting, cb).getResultList();
	}

	@Override
	public <T> List<T> findByAccountIdAndStorageName(long accountId, String storageName, EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting) {
		CriteriaBuilder<Bucket> cb = cbf.create(em, Bucket.class)
				.from(BucketObject.class)
				.where("id.bucket.owner.id").eq(accountId)
				.where("storage.id.name").eq(storageName)
				.where("deleted").eqExpression("false")
				.distinct()
				.select("id.bucket");
		return evm.applySetting(setting, cb).getResultList();
	}

}
