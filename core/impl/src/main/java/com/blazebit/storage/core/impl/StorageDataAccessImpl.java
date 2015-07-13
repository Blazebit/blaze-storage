package com.blazebit.storage.core.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import com.blazebit.storage.core.api.StorageDataAccess;
import com.blazebit.storage.core.model.jpa.Storage;

@Stateless
public class StorageDataAccessImpl extends AbstractDataAccess implements StorageDataAccess {

	@Override
	public List<Storage> getStorages(long userAccountId) {
		return cbf.create(em, Storage.class)
				.where("id.owner.id").eq(userAccountId)
				.getResultList();
	}

	@Override
	public Storage getStorage(long userAccountId, String name) {
		try {
			return cbf.create(em, Storage.class)
					.where("id.owner.id").eq(userAccountId)
					.where("id.name").eq(name)
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

}
