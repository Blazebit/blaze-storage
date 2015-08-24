package com.blazebit.storage.modules.storage.classpath;

import java.io.InputStream;
import java.net.URL;

import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.spi.StorageProvider;

public class ClassPathStorageProvider implements StorageProvider {
	
	private final ClassLoader classLoader;

	public ClassPathStorageProvider(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}
	
	protected URL getResource(String path) {
		return classLoader.getResource(path);
	}

	@Override
	public InputStream getObject(String path) {
		InputStream inputStream = classLoader.getResourceAsStream(path);
		
		if (inputStream == null) {
			throw new StorageException("Object could not be found: " + path);
		}
		
		return inputStream;
	}

	@Override
	public void deleteObject(String path) {
		throw new UnsupportedOperationException("Delete is not supported on the classpath!");
	}

	@Override
	public long putObject(String path, InputStream content) {
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