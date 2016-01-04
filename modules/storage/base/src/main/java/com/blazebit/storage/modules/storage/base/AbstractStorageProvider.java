package com.blazebit.storage.modules.storage.base;

import java.io.IOException;
import java.io.InputStream;

import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.spi.StorageProvider;

public abstract class AbstractStorageProvider implements StorageProvider {

	@Override
	public String copyObject(StorageProvider sourceStorageProvider, String contentKey) {
		try (InputStream is = sourceStorageProvider.getObject(contentKey)) {
			return createObject(is);
		} catch (IOException ex) {
			throw new StorageException("Could not copy object", ex);
		}
	}

}
