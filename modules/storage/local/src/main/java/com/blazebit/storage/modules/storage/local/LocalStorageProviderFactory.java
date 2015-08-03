package com.blazebit.storage.modules.storage.local;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.spi.StorageProvider;
import com.blazebit.storage.core.api.spi.StorageProviderFactory;

public class LocalStorageProviderFactory implements StorageProviderFactory {

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

	@Override
	public String getScheme() {
		return "file";
	}

}
