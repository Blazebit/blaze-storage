package com.blazebit.storage.rest.impl.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlan;
import com.fasterxml.jackson.annotation.JsonIgnore;

@EntityView(StorageQuotaPlan.class)
public interface StorageQuotaPlanView {

	@JsonIgnore
	@IdMapping("id")
	public Long getId();
	
	@Mapping("gigabyteLimit")
	public Integer getGigabyteLimit();

	@Mapping("alertPercent")
	public Short getAlertPercent();
	
}
