package com.blazebit.storage.modules.storage.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.logging.Logger;

import com.blazebit.storage.core.api.spi.StorageResult;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.spi.StorageProvider;
import com.blazebit.storage.modules.storage.base.AbstractStorageProvider;

public class FtpStorageProvider extends AbstractStorageProvider implements StorageProvider {

	private static final Logger LOG = Logger.getLogger(FtpStorageProvider.class.getName());
	private static final int CREATE_RETRIES = 3;

	private final FileObject root;
	private final boolean supportsWriting;

	public FtpStorageProvider(FileObject root, boolean supportsWriting) {
		this.root = root;
		this.supportsWriting = supportsWriting;
	}

	@Override
	public Object getStorageIdentifier() {
		return root;
	}

	@Override
	public InputStream getObject(String externalKey) {
		try {
			return root.resolveFile(externalKey).getContent().getInputStream();
		} catch (FileSystemException ex) {
			throw new StorageException(ex);
		}
	}

	@Override
	public void deleteObject(String externalKey) {
		try {
			root.resolveFile(externalKey).delete();
		} catch (FileSystemException ex) {
			throw new StorageException(ex);
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
		FileObject fileObject = createTempFile(null);
		StorageResult storageResult = putObject(md, fileObject, content);
		return storageResult.withExternalKey(fileObject.getName().getBaseName());
	}

	@Override
	public StorageResult putObject(String externalKey, InputStream content) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		try {
			FileObject targetFileObject = root.resolveFile(externalKey);
			FileObject fileObject = createTempFile("temp");

			StorageResult storageResult = putObject(md, fileObject, content);
			fileObject.moveTo(targetFileObject);
			return storageResult.withExternalKey(externalKey);
		} catch (FileSystemException ex) {
			throw new StorageException(ex);
		}
	}
	
	@Override
	public StorageResult copyObject(StorageProvider sourceStorageProvider, String contentKey) {
		// NOTE: Since FTP does not support efficient copying within the same server, we do it the old fashioned way 
		return super.copyObject(sourceStorageProvider, contentKey);
	}

	private StorageResult putObject(MessageDigest md, FileObject targetFileObject, InputStream content) {
		OutputStream os = null;
		
		try {
			os = targetFileObject.getContent().getOutputStream();
			return copyWithChecksum(md, content, os);
		} catch (IOException ex) {
			throw new StorageException(ex);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException ex) {
					// Ignore
				}
			}
		}
	}
	
	private FileObject createTempFile(String tempFolder) {
		int retries = CREATE_RETRIES;
		Exception retryException = null;
		while (retries-- != 0) {
			try {
				String externalKey = UUID.randomUUID().toString();
				FileObject tempFolderObject = tempFolder == null ? root : root.resolveFile(tempFolder);
				FileObject fileObject = tempFolderObject.resolveFile(externalKey);
				
				if (fileObject.exists()) {
					continue;
				}
				
				fileObject.createFile();
				return fileObject;
			} catch (FileSystemException ex) {
				retryException = ex;
			}
		}
		
		throw new StorageException("Could not create object. Tried " + CREATE_RETRIES + " times!", retryException);
	}

	/* FTP does not support these operations */

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
		return supportsWriting;
	}

	@Override
	public boolean supportsPut() {
		return supportsWriting;
	}

}
