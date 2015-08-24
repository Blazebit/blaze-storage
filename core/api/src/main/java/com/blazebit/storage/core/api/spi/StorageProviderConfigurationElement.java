package com.blazebit.storage.core.api.spi;

public interface StorageProviderConfigurationElement {

	public String getKey();
	
	public String getType();

	public String getDefaultValue();
	
	public String getName();
	
	public String getDescription();
	
}
