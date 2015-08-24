package com.blazebit.storage.core.api.spi;

import java.util.Set;

public interface StorageProviderMetamodel {
	
	public String getScheme();
	
	public String getName();
	
	public String getDescription();
	
	public StorageProviderConfigurationElement getConfigurationElement(String key);
	
	public Set<StorageProviderConfigurationElement> getConfigurationElements();

}
