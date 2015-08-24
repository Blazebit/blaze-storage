package com.blazebit.storage.server.faces.configuration;

import java.io.Serializable;

public class ConfigurationEntry implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String key;
	private String value;

	private final String type;
	private final String name;
	private final String description;
	private final ConfigurationEntryType configurationEntryType;

	public ConfigurationEntry() {
		this(null, null, null, null, null);
	}

	public ConfigurationEntry(String key, String value) {
		this(key, value, null, null, null);
	}

	public ConfigurationEntry(String key, String value, String type, String name, String description) {
		this.key = key;
		this.value = value;
		this.type = type;
		this.name = name;
		this.description = description;
		this.configurationEntryType = ConfigurationEntryType.forType(type);
	}
	
	public boolean isType(String type) {
		return configurationEntryType.isType(type);
	}
	
	public String getLabel() {
		if (name != null) {
			return name;
		}
		
		return key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
}
