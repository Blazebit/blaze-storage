package com.blazebit.storage.modules.storage.base;

import com.blazebit.storage.core.api.spi.StorageProviderConfigurationElement;

public class DefaultStorageProviderConfigurationElement implements StorageProviderConfigurationElement {

	private final String key;
	private final String type;
	private final String defaultValue;
	private final String name;
	private final String description;
	
	public DefaultStorageProviderConfigurationElement(String key, String type, String defaultValue, String name, String description) {
		this.key = key;
		this.type = type;
		this.defaultValue = defaultValue;
		this.name = name;
		this.description = description;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getDefaultValue() {
		return defaultValue;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}
}
