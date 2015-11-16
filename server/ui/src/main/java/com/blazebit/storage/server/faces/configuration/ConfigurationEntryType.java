package com.blazebit.storage.server.faces.configuration;

public enum ConfigurationEntryType {

	TEXT("text");
	
	private final String type;
	
	private ConfigurationEntryType(String type) {
		this.type = type;
	}

	public boolean isType(String type) {
		return this.type.equals(type);
	}
	
	public static ConfigurationEntryType forType(String type) {
		if (type == null || type.isEmpty()) {
			return TEXT;
		}
		
		ConfigurationEntryType configurationEntryType = ConfigurationEntryType.valueOf(type.toUpperCase());
		
		if (configurationEntryType == null) {
			throw new IllegalArgumentException("Invalid configuration entry type '" + type + "'!");
		}
		
		return configurationEntryType;
	}
}
