package com.blazebit.storage.testsuite.common.data;

import java.net.URI;

import com.blazebit.storage.core.model.jpa.Account;
import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.jpa.StorageId;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlan;

public class StorageTestData {

	public static Storage createStorage(Account owner, StorageQuotaPlan quotaPlan) {
		Storage defaultStorage = new Storage();
		defaultStorage.setId(new StorageId(owner.getId(), "test"));
		defaultStorage.setOwner(owner);
		defaultStorage.setQuotaPlan(quotaPlan);
		defaultStorage.setUri(URI.create("file:/tmp"));
		return defaultStorage;
	}
}
