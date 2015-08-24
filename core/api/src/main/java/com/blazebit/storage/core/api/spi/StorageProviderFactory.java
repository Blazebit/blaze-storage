package com.blazebit.storage.core.api.spi;

import java.net.URI;
import java.util.Map;

public interface StorageProviderFactory {
	
	public StorageProviderMetamodel getMetamodel();

	public Map<String, String> createConfiguration(URI uri);

	public URI createUri(Map<String, String> configuration);

	public StorageProvider createStorageProvider(Map<String, Object> properties);
	
}
