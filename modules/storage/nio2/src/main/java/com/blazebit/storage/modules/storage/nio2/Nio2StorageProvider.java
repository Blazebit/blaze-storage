package com.blazebit.storage.modules.storage.nio2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.spi.StorageProvider;
import com.blazebit.storage.core.api.spi.StorageResult;
import com.blazebit.storage.modules.storage.base.AbstractStorageProvider;

public abstract class Nio2StorageProvider extends AbstractStorageProvider implements StorageProvider {
	
	private static final Logger LOG = Logger.getLogger(Nio2StorageProvider.class.getName());
	private static final int CREATE_RETRIES = 3;
	
	protected abstract Path getBasePath();
	
	protected FileStore getFileStore() throws IOException {
		Path basePath = getBasePath();
		return basePath.getFileSystem().provider().getFileStore(basePath);
	}
	
	protected Path getObjectPath(String externalKey) {
		return getBasePath().resolve(externalKey);
	}
	
	@Override
	public Object getStorageIdentifier() {
		return getBasePath();
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
	public InputStream getObject(String externalKey) {
		Path objectPath = getObjectPath(externalKey);
		
		try {
			return Files.newInputStream(objectPath);
		} catch (IOException e) {
			throw new StorageException(e);
		}
	}
	
	@Override
	public void deleteObject(String externalKey) {
		Path objectPath = getObjectPath(externalKey);
		
		try {
			Files.deleteIfExists(objectPath);
		} catch (IOException e) {
			throw new StorageException(e);
		}
	}

	@Override
	public StorageResult createObject(InputStream content) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		Path tempPath = null;
		
		try {
			tempPath = createTempFile();
			StorageResult storageResult;
			try (OutputStream os = Files.newOutputStream(tempPath, StandardOpenOption.WRITE)) {
				storageResult = copyWithChecksum(md, content, os);
			}

			int retries = CREATE_RETRIES;
			Exception retryException = null;
			Path objectPath = null;
			while (retries-- != 0) {
				try {
					String externalKey = UUID.randomUUID().toString();
					objectPath = getObjectPath(externalKey);
					Files.move(tempPath, objectPath, StandardCopyOption.ATOMIC_MOVE);
					return storageResult.withExternalKey(externalKey);
				} catch (FileAlreadyExistsException ex) {
					retryException = ex;
				} catch (IOException e) {
					if (objectPath != null) {
						Files.deleteIfExists(objectPath);
					}
					throw e;
				}
			}
			
			throw new StorageException("Could not create object. Tried " + CREATE_RETRIES + " times!", retryException);
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
	public StorageResult putObject(String externalKey, InputStream content) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		Path objectPath = getObjectPath(externalKey);
		Path tempPath = null;
		
		try {
			tempPath = createTempFile();
			StorageResult storageResult;
			try (OutputStream os = Files.newOutputStream(tempPath, StandardOpenOption.WRITE)) {
				storageResult = copyWithChecksum(md, content, os);
			}

			Files.move(tempPath, objectPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
			return storageResult.withExternalKey(externalKey);
		} catch (IOException e) {
			if (objectPath != null) {
				try {
					Files.deleteIfExists(objectPath);
				} catch (IOException ex) {
					ex.addSuppressed(e);
					LOG.log(Level.SEVERE, "Could not delete file after IO error!", ex);
				}
			}
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
	public StorageResult copyObject(StorageProvider sourceStorageProvider, String contentKey) {
		if (getStorageIdentifier().equals(sourceStorageProvider.getStorageIdentifier())) {
			Path sourceObjectPath = getObjectPath(contentKey);

			try {
				int retries = CREATE_RETRIES;
				Exception retryException = null;
				while (retries-- != 0) {
					Path objectPath = null;
					try {
						String externalKey = UUID.randomUUID().toString();
						objectPath = getObjectPath(externalKey);
						Files.copy(sourceObjectPath, objectPath);
						return new StorageResult(externalKey, null, -1);
					} catch (FileAlreadyExistsException ex) {
						retryException = ex;
					} catch (IOException ex) {
						if (objectPath != null) {
							Files.deleteIfExists(objectPath);
						}
						throw ex;
					}
				}
				
				throw new StorageException("Could not copy object. Tried " + CREATE_RETRIES + " times!", retryException);
			} catch (IOException e) {
				throw new StorageException(e);
			}
		} else {
			return super.copyObject(sourceStorageProvider, contentKey);
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
