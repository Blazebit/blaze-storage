package com.blazebit.storage.core.api;

import java.net.URI;
import java.util.List;
import java.util.Map;

import com.blazebit.storage.core.api.spi.StorageProviderFactory;

public interface StorageProviderFactoryDataAccess {
	
	public String getType(String uri);
	
	public String getType(URI uri);
	
	public Map<String, String> getConfiguration(String uri);
	
	public Map<String, String> getConfiguration(URI uri);
	
	public URI getConfigurationUri(String type, Map<String, String> configuration);

	public List<StorageProviderFactory> findAll();

	public StorageProviderFactory findByScheme(String scheme);
}
