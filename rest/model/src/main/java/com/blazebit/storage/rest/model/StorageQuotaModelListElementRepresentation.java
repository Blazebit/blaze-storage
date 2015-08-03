package com.blazebit.storage.rest.model;

public class StorageQuotaModelListElementRepresentation extends StorageQuotaModelUpdateRepresentation {

	private static final long serialVersionUID = 1L;

	private String name;

	public StorageQuotaModelListElementRepresentation() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
