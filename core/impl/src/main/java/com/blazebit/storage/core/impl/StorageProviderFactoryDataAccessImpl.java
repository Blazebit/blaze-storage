package com.blazebit.storage.core.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.blazebit.storage.core.api.StorageProviderFactoryDataAccess;
import com.blazebit.storage.core.api.spi.StorageProviderFactory;
import com.blazebit.storage.core.api.spi.StorageProviderMetamodel;

@Singleton
public class StorageProviderFactoryDataAccessImpl extends AbstractDataAccess implements StorageProviderFactoryDataAccess {

	private final List<StorageProviderFactory> list;
	private final Map<String, StorageProviderFactory> map;
	
	@Inject
	public StorageProviderFactoryDataAccessImpl(Instance<StorageProviderFactory> storageProviderFactories) {
		List<StorageProviderFactory> list = new ArrayList<>();
		Map<String, StorageProviderFactory> map = new HashMap<>();
		
		for (StorageProviderFactory factory : storageProviderFactories) {
			StorageProviderMetamodel metamodel = factory.getMetamodel();
			list.add(factory);
			map.put(metamodel.getScheme(), factory);
		}
		
		this.list = Collections.unmodifiableList(list);
		this.map = Collections.unmodifiableMap(map);
	}

	@Override
	public String getType(String uriString) {
		URI uri = URI.create(uriString);
		return getType(uri);
	}
	
	@Override
	public String getType(URI uri) {
		StorageProviderFactory storageProviderFactory = map.get(uri.getScheme());
		
		if (storageProviderFactory == null) {
			return null;
		}
		
		return uri.getScheme();
	}

	@Override
	public Map<String, String> getConfiguration(String uriString) {
		URI uri = URI.create(uriString);
		return getConfiguration(uri);
	}

	@Override
	public Map<String, String> getConfiguration(URI uri) {
		StorageProviderFactory storageProviderFactory = map.get(uri.getScheme());
		
		if (storageProviderFactory == null) {
			return null;
		}
		
		return storageProviderFactory.createConfiguration(uri);
	}

	@Override
	public URI getConfigurationUri(String type, Map<String, String> configuration) {
		StorageProviderFactory storageProviderFactory = map.get(type);
		
		if (storageProviderFactory == null) {
			return null;
		}
		
		return storageProviderFactory.createUri(configuration);
	}

	@Override
	public List<StorageProviderFactory> findAll() {
		return list;
	}

	@Override
	public StorageProviderFactory findByScheme(String scheme) {
		return map.get(scheme);
	}

}
