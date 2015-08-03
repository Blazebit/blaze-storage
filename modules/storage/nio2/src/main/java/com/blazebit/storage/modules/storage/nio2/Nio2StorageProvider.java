package com.blazebit.storage.modules.storage.nio2;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.spi.StorageProvider;

public abstract class Nio2StorageProvider implements StorageProvider {
	
	private static final Logger LOG = Logger.getLogger(Nio2StorageProvider.class.getName());
	
	protected abstract Path getBasePath();
	
	protected FileStore getFileStore() throws IOException {
		Path basePath = getBasePath();
		return basePath.getFileSystem().provider().getFileStore(basePath);
	}
	
	protected Path getObjectPath(String path) {
		return getBasePath().resolve(path);
	}
	
	/**
	 * Create a temporary file that resides on the same device as the actual files so we can do atomic moves.
	 * 
	 * @return
	 * @throws IOException
	 */
	protected Path createTempFile() throws IOException {
		return Files.createTempFile(getBasePath(), null, null);
	}
	
	@Override
	public InputStream getObject(String path) {
		Path objectPath = getObjectPath(path);
		
		try {
			return Files.newInputStream(objectPath);
		} catch (IOException e) {
			throw new StorageException(e);
		}
	}
	
	@Override
	public void deleteObject(String path) {
		Path objectPath = getObjectPath(path);
		
		try {
			Files.deleteIfExists(objectPath);
		} catch (IOException e) {
			throw new StorageException(e);
		}
	}

	@Override
	public long putObject(String path, InputStream content) {
		Path objectPath = getObjectPath(path);
		Path tempPath = null;
		
		try {
			tempPath = createTempFile();
			long bytes = Files.copy(content, tempPath, StandardCopyOption.REPLACE_EXISTING);
			Files.move(tempPath, objectPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
			return bytes;
		} catch (IOException e) {
			throw new StorageException(e);
		} finally {
			if (tempPath != null) {
				try {
					Files.deleteIfExists(tempPath);
				} catch (IOException e) {
					LOG.log(Level.SEVERE, "Could not delete temporary file!", e);
				}
			}
		}
	}

	@Override
	public long getTotalSpace() {
		try {
			return getFileStore().getTotalSpace();
		} catch (IOException e) {
			throw new StorageException(e);
		}
	}

	@Override
	public long getUsableSpace() {
		try {
			return getFileStore().getUsableSpace();
		} catch (IOException e) {
			throw new StorageException(e);
		}
	}

	@Override
	public long getUnallocatedSpace() {
		try {
			return getFileStore().getUnallocatedSpace();
		} catch (IOException e) {
			throw new StorageException(e);
		}
	}

	@Override
	public boolean supportsDelete() {
		return true;
	}

	@Override
	public boolean supportsPut() {
		return true;
	}

}
