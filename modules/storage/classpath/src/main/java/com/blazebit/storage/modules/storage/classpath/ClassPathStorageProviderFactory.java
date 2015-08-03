package com.blazebit.storage.modules.storage.classpath;

import java.util.Map;

import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.spi.StorageProvider;
import com.blazebit.storage.core.api.spi.StorageProviderFactory;

public class ClassPathStorageProviderFactory implements StorageProviderFactory {

	@Override
	public StorageProvider createStorageProvider(Map<String, Object> properties) {
		Object classLoader = properties.get(ClassPathStorage.CLASS_LOADER_PROPERTY);
		
		if (classLoader == null) {
			throw new StorageException("No class loader has been set!");
		} else if (!(classLoader instanceof ClassLoader)) {
			throw new StorageException("The value for the class loader property is not an instance of a class loader: " + classLoader);
		}
		
		return new ClassPathStorageProvider((ClassLoader) classLoader);
	}

	@Override
	public String getScheme() {
		return "classpath";
	}

}
