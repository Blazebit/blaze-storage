package com.blazebit.storage.rest.model;

import java.io.Serializable;

public class StorageQuotaPlanRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

	private StorageQuotaModelRepresentation model;
	private Integer gigabyteLimit;

	public StorageQuotaPlanRepresentation() {
	}

	public StorageQuotaModelRepresentation getModel() {
		return model;
	}

	public void setModel(StorageQuotaModelRepresentation model) {
		this.model = model;
	}

	public Integer getGigabyteLimit() {
		return gigabyteLimit;
	}

	public void setGigabyteLimit(Integer gigabyteLimit) {
		this.gigabyteLimit = gigabyteLimit;
	}
}
