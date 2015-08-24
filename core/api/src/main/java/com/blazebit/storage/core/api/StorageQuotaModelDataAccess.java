package com.blazebit.storage.core.api;

import java.util.List;

import com.blazebit.persistence.QueryBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.model.jpa.StorageQuotaModel;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlan;

public interface StorageQuotaModelDataAccess {

	public <T> List<T> findAll(EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting);

	public StorageQuotaModel findById(String id);

	public StorageQuotaPlan findQuotaPlanByModelIdAndLimit(String modelId, Integer gigabyteLimit);
	
	public <T> T findById(String id, EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting);
}
