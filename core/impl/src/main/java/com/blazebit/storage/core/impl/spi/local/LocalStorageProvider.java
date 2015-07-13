package com.blazebit.storage.core.impl.spi.local;

import java.nio.file.Path;

import com.blazebit.storage.core.impl.spi.nio2.Nio2StorageProvider;

public class LocalStorageProvider extends Nio2StorageProvider {
	
	private final Path basePath;

	public LocalStorageProvider(Path basePath) {
		this.basePath = basePath;
	}

	@Override
	protected Path getBasePath() {
		return basePath;
	}

}
