package com.blazebit.storage.testsuite.common.data;

import com.blazebit.storage.core.model.jpa.StorageQuotaModel;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlan;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlanId;

public class QuotaPlanTestData {

	public static StorageQuotaModel createModel() {
		StorageQuotaModel defaultQuotaModel = new StorageQuotaModel();
		defaultQuotaModel.setId("test-model");
		defaultQuotaModel.setName("test-model");
		defaultQuotaModel.setDescription("test-model");
		return defaultQuotaModel;
	}
	
	public static StorageQuotaPlan createPlan(StorageQuotaModel defaultQuotaModel) {
		StorageQuotaPlan defaultQuotaPlan = new StorageQuotaPlan();
		defaultQuotaPlan.setId(new StorageQuotaPlanId(defaultQuotaModel.getId(), 1));
		defaultQuotaPlan.setQuotaModel(defaultQuotaModel);
		defaultQuotaPlan.setAlertPercent((short) 100);
		return defaultQuotaPlan;
	}
}
