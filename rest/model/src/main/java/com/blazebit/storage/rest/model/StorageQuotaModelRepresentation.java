package com.blazebit.storage.rest.model;

public class StorageQuotaModelRepresentation extends StorageQuotaModelUpdateRepresentation {

	private static final long serialVersionUID = 1L;

	private String key;

	public StorageQuotaModelRepresentation() {
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
