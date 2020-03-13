package com.blazebit.storage.modules.storage.classpath;

import java.io.InputStream;
import java.net.URL;

import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.spi.StorageProvider;
import com.blazebit.storage.core.api.spi.StorageResult;
import com.blazebit.storage.modules.storage.base.AbstractStorageProvider;

public class ClassPathStorageProvider extends AbstractStorageProvider implements StorageProvider {
	
	private final ClassLoader classLoader;

	public ClassPathStorageProvider(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	protected URL getResource(String externalKey) {
		return classLoader.getResource(externalKey);
	}

	@Override
	public Object getStorageIdentifier() {
		return classLoader;
	}

	@Override
	public InputStream getObject(String externalKey) {
		InputStream inputStream = classLoader.getResourceAsStream(externalKey);
		
		if (inputStream == null) {
			throw new StorageException("Object could not be found: " + externalKey);
		}
		
		return inputStream;
	}

	@Override
	public void deleteObject(String externalKey) {
		throw new UnsupportedOperationException("Delete is not supported on the classpath!");
	}

	@Override
	public StorageResult createObject(InputStream content) {
		throw new UnsupportedOperationException("Put is not supported on the classpath!");
	}

	@Override
	public StorageResult putObject(String externalKey, InputStream content) {
		throw new UnsupportedOperationException("Put is not supported on the classpath!");
	}

	@Override
	public StorageResult copyObject(StorageProvider sourceStorageProvider, String contentKey) {
		throw new UnsupportedOperationException("Put is not supported on the classpath!");
	}

	@Override
	public long getTotalSpace() {
		return -1;
	}

	@Override
	public long getUsableSpace() {
		return -1;
	}

	@Override
	public long getUnallocatedSpace() {
		return -1;
	}

	@Override
	public boolean supportsDelete() {
		return false;
	}

	@Override
	public boolean supportsPut() {
		return false;
	}

}
