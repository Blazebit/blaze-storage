package com.blazebit.storage.modules.storage.base;

import java.util.Map;

public class PathBasedStorageProviderFactoryUriHelper extends StorageProviderFactoryUriHelper {
	
	private final String pathKey;

	public PathBasedStorageProviderFactoryUriHelper(String scheme, String pathKey) {
		super(scheme, null);
		this.pathKey = pathKey;
	}

	@Override
	protected String getPath(Map<String, String> configuration) {
		return configuration.get(pathKey);
	}

	@Override
	protected void addPath(Map<String, String> configuration, String path) {
		configuration.put(pathKey, path);
	}

	@Override
	protected boolean shouldAppendParameter(String name) {
		return !pathKey.equals(name) && super.shouldAppendParameter(name);
	}

}
