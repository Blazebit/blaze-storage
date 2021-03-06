package com.blazebit.storage.core.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.QueryBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.StorageDataAccess;
import com.blazebit.storage.core.api.StorageProviderFactoryDataAccess;
import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.jpa.StorageId;

@Stateless
public class StorageDataAccessImpl extends AbstractDataAccess implements StorageDataAccess {
	
	@Inject
	private StorageProviderFactoryDataAccess storageProviderFactoryDataAccess;

	@Override
	public <T> List<T> findAllByAccountId(long accountId, EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting) {
		CriteriaBuilder<Storage> cb = cbf.create(em, Storage.class)
				.where("owner.id").eq(accountId);
		setting.addOptionalParameter("storageProviderFactoryDataAccess", storageProviderFactoryDataAccess);
		return evm.applySetting(setting, cb).getResultList();
	}

	@Override
	public Storage findById(StorageId storageId) {
		if (storageId == null) {
			return null;
		}
		
		try {
			return cbf.create(em, Storage.class)
					.where("id").eq(storageId)
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	@Override
	public Storage findByBucketId(String bucketId) {
		if (bucketId == null) {
			return null;
		}
		
		try {
			return cbf.create(em, Storage.class)
					.from(Bucket.class)
					.where("id").eq(bucketId)
					.select("storage")
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	@Override
	public <T> T findById(StorageId storageId, EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting) {
		if (storageId == null) {
			return null;
		}
		
		try {
			CriteriaBuilder<Storage> cb = cbf.create(em, Storage.class)
					.where("id").eq(storageId);
			setting.addOptionalParameter("storageProviderFactoryDataAccess", storageProviderFactoryDataAccess);
			return evm.applySetting(setting, cb).getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

}
