package com.blazebit.storage.core.api.spi;

import java.util.Map;

public interface StorageProviderFactory {

	public StorageProvider createStorageProvider(Map<String, Object> properties);
	
	public String getScheme();
	
}
