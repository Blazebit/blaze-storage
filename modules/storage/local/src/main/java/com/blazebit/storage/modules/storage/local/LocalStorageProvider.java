package com.blazebit.storage.modules.storage.local;

import java.nio.file.Path;

import com.blazebit.storage.modules.storage.nio2.Nio2StorageProvider;

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
