package com.blazebit.storage.modules.storage.local;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.spi.StorageProvider;
import com.blazebit.storage.core.api.spi.StorageProviderFactory;
import com.blazebit.storage.core.api.spi.StorageProviderMetamodel;
import com.blazebit.storage.modules.storage.base.DefaultStorageProviderConfigurationElement;
import com.blazebit.storage.modules.storage.base.DefaultStorageProviderMetamodel;
import com.blazebit.storage.modules.storage.base.PathBasedStorageProviderFactoryUriHelper;
import com.blazebit.storage.modules.storage.base.StorageProviderFactoryUriHelper;

@ApplicationScoped
public class LocalStorageProviderFactory implements StorageProviderFactory {

	private static final String SCHEME = "file";
	private static final StorageProviderFactoryUriHelper URI_HELPER = new PathBasedStorageProviderFactoryUriHelper(SCHEME, LocalStorage.BASE_PATH_PROPERTY);
	
	private final StorageProviderMetamodel metamodel;
	
	public LocalStorageProviderFactory() {
		this.metamodel = new DefaultStorageProviderMetamodel(SCHEME, "Local storage provider", "Loads from the local filesystem", 
			new DefaultStorageProviderConfigurationElement(LocalStorage.BASE_PATH_PROPERTY, "text", null, "Base path", "The path to the base directory which is used as storage. In the future EL expressions will be allowed.")
		);
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
	public StorageProvider createStorageProvider(Map<String, Object> properties) {
		Object basePathValue = properties.get(LocalStorage.BASE_PATH_PROPERTY);
		
		if (basePathValue == null) {
			throw new StorageException("Base path is not set!");
		} else if (!(basePathValue instanceof String)) {
			throw new StorageException("Invalid base path is set! String expected but got: " + basePathValue);
		}
		
		String basePath = ((String) basePathValue).trim();
		if (basePath.isEmpty()) {
			throw new StorageException("The given base path is empty!");
		}
		
		Path path = Paths.get(basePath);
		
		if (!Files.isDirectory(path)) {
			throw new StorageException("The given base path does not resolve to an existing directory: " + path.toAbsolutePath().toString());
		}
		
		return new LocalStorageProvider(path);
	}

}
