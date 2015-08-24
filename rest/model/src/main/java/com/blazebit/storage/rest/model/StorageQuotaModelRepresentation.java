package com.blazebit.storage.rest.model;

import java.util.Set;

public class StorageQuotaModelRepresentation extends StorageQuotaModelUpdateRepresentation {

	private static final long serialVersionUID = 1L;

	private String id;

	public StorageQuotaModelRepresentation() {
	}

	public StorageQuotaModelRepresentation(String name, String description, Set<Integer> limits, String id) {
		super(name, description, limits);
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
