package com.blazebit.storage.core.impl;

import java.io.InputStream;
import java.net.URI;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.QueryBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.BucketObjectDataAccess;
import com.blazebit.storage.core.api.StorageProviderFactoryDataAccess;
import com.blazebit.storage.core.api.spi.StorageProvider;
import com.blazebit.storage.core.api.spi.StorageProviderFactory;
import com.blazebit.storage.core.model.jpa.BucketObject;
import com.blazebit.storage.core.model.jpa.BucketObjectId;
import com.blazebit.storage.core.model.jpa.BucketObjectState;

@Stateless
public class BucketObjectDataAccessImpl extends AbstractDataAccess implements BucketObjectDataAccess {
	
	@Inject
	private StorageProviderFactoryDataAccess storageProviderFactoryDataAccess;

	@Override
	public InputStream getContent(URI storageUri, String contentKey) {
		StorageProviderFactory factory = storageProviderFactoryDataAccess.findByScheme(storageUri.getScheme());
		StorageProvider storageProvider = factory.createStorageProvider(factory.createConfiguration(storageUri));
		return storageProvider.getObject(contentKey);
	}
	
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
	public <T> T findById(BucketObjectId bucketObjectId, EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting) {
		try {
			CriteriaBuilder<BucketObject> cb = cbf.create(em, BucketObject.class)
					.where("id").eq(bucketObjectId)
					.where("state").eq(BucketObjectState.CREATED);
			return evm.applySetting(setting, cb).getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

}
