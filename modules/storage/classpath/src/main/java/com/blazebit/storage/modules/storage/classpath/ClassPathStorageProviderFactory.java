package com.blazebit.storage.modules.storage.classpath;

import java.net.URI;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.spi.StorageProvider;
import com.blazebit.storage.core.api.spi.StorageProviderFactory;
import com.blazebit.storage.core.api.spi.StorageProviderMetamodel;
import com.blazebit.storage.modules.storage.base.DefaultStorageProviderConfigurationElement;
import com.blazebit.storage.modules.storage.base.DefaultStorageProviderMetamodel;
import com.blazebit.storage.modules.storage.base.StorageProviderFactoryUriHelper;

@ApplicationScoped
public class ClassPathStorageProviderFactory implements StorageProviderFactory {
	
	private static final String SCHEME = "classpath";
	private static final StorageProviderFactoryUriHelper URI_HELPER = new StorageProviderFactoryUriHelper(SCHEME);
	
	private final StorageProviderMetamodel metamodel;
	
	public ClassPathStorageProviderFactory() {
		this.metamodel = new DefaultStorageProviderMetamodel(SCHEME, "ClassPath storage provider", "Loads from the classpath", 
			new DefaultStorageProviderConfigurationElement(ClassPathStorage.CLASS_LOADER_PROPERTY, "java.lang.ClassLoader", null, "Classloader", "The class loader to use for loading. In the future EL expressions will be allowed. The string 'context' will mean the context classloader should be used.")
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
		Object classLoader = properties.get(ClassPathStorage.CLASS_LOADER_PROPERTY);
		
		if (classLoader == null) {
			throw new StorageException("No class loader has been set!");
		} else if (!(classLoader instanceof ClassLoader)) {
			throw new StorageException("The value for the class loader property is not an instance of a class loader: " + classLoader);
		}
		
		return new ClassPathStorageProvider((ClassLoader) classLoader);
	}

}
