package com.blazebit.storage.testsuite.common.data;

import com.blazebit.storage.core.model.jpa.StorageQuotaModel;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlan;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlanId;

public class QuotaPlanTestData {

	public static StorageQuotaModel createModel() {
		return createModel("test-model");
	}
	
	public static StorageQuotaModel createModel(String id) {
		StorageQuotaModel defaultQuotaModel = new StorageQuotaModel();
		defaultQuotaModel.setId(id);
		defaultQuotaModel.setName(id);
		defaultQuotaModel.setDescription(id);
		return defaultQuotaModel;
	}

	public static StorageQuotaPlan createPlan(StorageQuotaModel defaultQuotaModel) {
		return createPlan(defaultQuotaModel, 1);
	}
	
	public static StorageQuotaPlan createPlan(StorageQuotaModel defaultQuotaModel, Integer gigabyteLimit) {
		StorageQuotaPlan defaultQuotaPlan = new StorageQuotaPlan();
		defaultQuotaPlan.setId(new StorageQuotaPlanId(defaultQuotaModel.getId(), gigabyteLimit));
		defaultQuotaPlan.setQuotaModel(defaultQuotaModel);
		defaultQuotaPlan.setAlertPercent((short) 100);
		return defaultQuotaPlan;
	}
}
