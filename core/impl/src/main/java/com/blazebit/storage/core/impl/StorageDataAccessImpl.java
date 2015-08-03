package com.blazebit.storage.core.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import com.blazebit.storage.core.api.StorageDataAccess;
import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.jpa.StorageId;

@Stateless
public class StorageDataAccessImpl extends AbstractDataAccess implements StorageDataAccess {

	@Override
	public List<Storage> findByUserAccountId(long userAccountId) {
		return cbf.create(em, Storage.class)
				.where("id.owner.id").eq(userAccountId)
				.getResultList();
	}

	@Override
	public Storage findById(StorageId storageId) {
		try {
			return cbf.create(em, Storage.class)
					.where("id").eq(storageId)
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

}
