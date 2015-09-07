package com.blazebit.storage.rest.impl.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlan;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlanId;
import com.fasterxml.jackson.annotation.JsonIgnore;

@EntityView(StorageQuotaPlan.class)
public abstract class StorageQuotaPlanView {

	@JsonIgnore
	@IdMapping("id")
	public abstract StorageQuotaPlanId getId();
	
	@Mapping("gigabyteLimit")
	public Integer getGigabyteLimit() {
		return getId().getGigabyteLimit();
	}

	@Mapping("alertPercent")
	public abstract Short getAlertPercent();
	
}
