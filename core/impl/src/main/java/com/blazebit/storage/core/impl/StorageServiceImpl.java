package com.blazebit.storage.core.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.StorageQuotaModelDataAccess;
import com.blazebit.storage.core.api.StorageService;
import com.blazebit.storage.core.model.jpa.ObjectStatistics;
import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.jpa.StorageId;

@Stateless
public class StorageServiceImpl extends AbstractService implements StorageService {
	
	@Inject
	private StorageQuotaModelDataAccess storageQuotaModelDataAccess;

	@Override
	public void create(Storage storage) {
		if (storage.getQuotaPlan().getId() == null) {
			storage.setQuotaPlan(storageQuotaModelDataAccess.findQuotaPlanByModelIdAndLimit(storage.getQuotaPlan().getQuotaModel().getId(), storage.getQuotaPlan().getGigabyteLimit()));
		}
		em.persist(storage);
	}

	@Override
	public void update(Storage storage) {
		throw new UnsupportedOperationException("Update of storages not yet supported!");
	}

	@Override
	public void updateStatistics(StorageId storageId, ObjectStatistics deltaStatistics) {
		int updated = em.createQuery("UPDATE Storage s "
				+ "SET statistics.objectCount = statistics.objectCount + :objectCountDelta, "
				+ "statistics.objectBytes = statistics.objectBytes + :objectBytesDelta "
				+ "WHERE s.id = :storageId")
			.setParameter("objectCountDelta", deltaStatistics.getObjectCount())
			.setParameter("objectBytesDelta", deltaStatistics.getObjectBytes())
			.setParameter("storageId", storageId)
			.executeUpdate();
		
		if (updated != 1) {
			throw new StorageException("Storage statistics update failed. Expected to update 1 row but was " + updated);
		}
	}

	@Override
	public void delete(StorageId storageId) {
		throw new UnsupportedOperationException("Deletion of storages not yet supported!");
	}

}
