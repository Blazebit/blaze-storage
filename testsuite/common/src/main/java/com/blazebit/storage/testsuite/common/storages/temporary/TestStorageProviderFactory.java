package com.blazebit.storage.testsuite.common.storages.temporary;

import java.net.URI;
import java.nio.file.Path;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.spi.StorageProvider;
import com.blazebit.storage.core.api.spi.StorageProviderFactory;
import com.blazebit.storage.core.api.spi.StorageProviderMetamodel;
import com.blazebit.storage.modules.storage.base.DefaultStorageProviderMetamodel;
import com.blazebit.storage.modules.storage.base.StorageProviderFactoryUriHelper;
import com.blazebit.storage.modules.storage.local.LocalStorageProvider;

@ApplicationScoped
public class TestStorageProviderFactory implements StorageProviderFactory {

	private static final String SCHEME = "test";
	private static final StorageProviderFactoryUriHelper URI_HELPER = new StorageProviderFactoryUriHelper(SCHEME);
	
	private final StorageProviderMetamodel metamodel;
	private volatile Path basePath;
	
	public TestStorageProviderFactory() {
		this.metamodel = new DefaultStorageProviderMetamodel(SCHEME, "Test storage provider", "For tests only, uses a temporary directory");
	}

	@Override
	public StorageProviderMetamodel getMetamodel() {
		return metamodel;
	}

	@Override
	public Map<String, String> createConfiguration(URI uri) {
		return URI_HELPER.createConfiguration(uri);
	}

	@Override
	public URI createUri(Map<String, String> configuration) {
		return URI_HELPER.createUri(configuration);
	}

	@Override
	public StorageProvider createStorageProvider(Map<String, ? extends Object> properties) {
		Path path = basePath;
		
		if (path == null) {
			throw new StorageException("No base path has been set!");
		}
		
		return new LocalStorageProvider(path);
	}

	public Path getBasePath() {
		return basePath;
	}

	public void setBasePath(Path basePath) {
		this.basePath = basePath;
	}

}
