package com.blazebit.storage.core.impl;

import javax.ejb.Stateless;
import javax.persistence.LockModeType;

import com.blazebit.storage.core.api.AccountNotFoundException;
import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.StorageQuotaPlanNotFoundException;
import com.blazebit.storage.core.api.StorageService;
import com.blazebit.storage.core.model.jpa.Account;
import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.jpa.StorageId;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlan;

@Stateless
public class StorageServiceImpl extends AbstractService implements StorageService {
	
	private static final long GIGABYTE = 1L << 30;

	private void create(Storage storage) {
		StorageQuotaPlan quotaPlan = em.find(StorageQuotaPlan.class, storage.getQuotaPlan().getId());
		
		if (quotaPlan == null) {
			throw new StorageQuotaPlanNotFoundException("Storage quota plan not found!");
		}
		
		Account owner = em.find(Account.class, storage.getId().getOwnerId());
		
		if (owner == null) {
			throw new AccountNotFoundException("Account not found!");
		}
		
		storage.setOwner(owner);
		storage.setQuotaPlan(quotaPlan);
		em.persist(storage);
		em.flush();
	}

	@Override
	public void put(Storage storage) {
		if (em.contains(storage)) {
			em.detach(storage);
		}
		
		Storage currentStorage = em.find(Storage.class, storage.getId(), LockModeType.PESSIMISTIC_WRITE);
		
		if (currentStorage == null) {
			create(storage);
			return;
		}

		StorageQuotaPlan quotaPlan = em.find(StorageQuotaPlan.class, storage.getQuotaPlan().getId());
		
		if (quotaPlan == null) {
			throw new StorageQuotaPlanNotFoundException("Storage quota plan not found!");
		}
		
		if (quotaPlan.getId().getGigabyteLimit() * GIGABYTE < storage.getStatistics().getObjectBytes()) {
			throw new StorageException("Can not set a quota plan with a gigabyte limit(" + quotaPlan.getId().getGigabyteLimit() + ") lower than the current object bytes(" + storage.getStatistics().getObjectBytes() + ")!");
		}

		currentStorage.setQuotaPlan(quotaPlan);
		currentStorage.setUri(storage.getUri());
		currentStorage.setTags(storage.getTags());
		em.merge(currentStorage);
		em.flush();
	}
	
	@Override
	public void delete(StorageId storageId) {
		throw new UnsupportedOperationException("Deletion of storages not yet supported!");
	}

}
