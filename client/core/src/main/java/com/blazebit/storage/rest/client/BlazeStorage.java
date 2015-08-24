package com.blazebit.storage.rest.client;

import com.blazebit.storage.rest.api.AccountsResource;
import com.blazebit.storage.rest.api.BucketsSubResource;
import com.blazebit.storage.rest.api.StorageQuotaModelsResource;
import com.blazebit.storage.rest.api.StorageTypesResource;

public interface BlazeStorage {

	public AccountsResource accounts();

	public BucketsSubResource buckets();

	public StorageQuotaModelsResource storageQuotaModels();

	public StorageTypesResource storageTypes();

	public void close();

}