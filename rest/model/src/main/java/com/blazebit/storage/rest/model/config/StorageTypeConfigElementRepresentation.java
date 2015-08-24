package com.blazebit.storage.rest.model.config;

public class StorageTypeConfigElementRepresentation extends StorageTypeConfigEntryRepresentation {

	private static final long serialVersionUID = 1L;

	private String type;
	private String name;
	private String description;

	public StorageTypeConfigElementRepresentation() {
	}

	public StorageTypeConfigElementRepresentation(String key, String value, String type, String name, String description) {
		super(key, value);
		this.type = type;
		this.name = name;
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
