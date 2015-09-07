package com.blazebit.storage.core.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.StorageQuotaModelDataAccess;
import com.blazebit.storage.core.api.StorageService;
import com.blazebit.storage.core.model.jpa.ObjectStatistics;
import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.jpa.StorageId;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlan;

@Stateless
public class StorageServiceImpl extends AbstractService implements StorageService {
	
	private static final long GIGABYTE = 1L << 30;
	
	@Inject
	private StorageQuotaModelDataAccess storageQuotaModelDataAccess;

	private void create(Storage storage) {
		if (storage.getQuotaPlan().getId() == null) {
			storage.setQuotaPlan(storageQuotaModelDataAccess.findQuotaPlanById(storage.getQuotaPlan().getId()));
		}
		
		em.persist(storage);
		em.flush();
	}

	@Override
	public void put(Storage storage) {
		Storage currentStorage = em.find(Storage.class, storage.getId());
		
		if (currentStorage == null) {
			create(storage);
			return;
		}

		StorageQuotaPlan quotaPlan = storageQuotaModelDataAccess.findQuotaPlanById(storage.getQuotaPlan().getId());
		
		if (quotaPlan.getId().getGigabyteLimit() * GIGABYTE < storage.getStatistics().getObjectBytes()) {
			throw new StorageException("Can not set a quota plan with a gigabyte limit(" + quotaPlan.getId().getGigabyteLimit() + ") lower than the current object bytes(" + storage.getStatistics().getObjectBytes() + ")!");
		}

		currentStorage.setUri(storage.getUri());
		currentStorage.setQuotaPlan(quotaPlan);
		currentStorage.setTags(storage.getTags());
		em.merge(currentStorage);
		em.flush();
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
