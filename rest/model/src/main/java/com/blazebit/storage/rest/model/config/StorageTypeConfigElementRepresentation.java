package com.blazebit.storage.rest.model.config;

public class StorageTypeConfigElementRepresentation extends StorageTypeConfigEntryRepresentation {

	private static final long serialVersionUID = 1L;

	private String name;
	private String description;

	public StorageTypeConfigElementRepresentation() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
