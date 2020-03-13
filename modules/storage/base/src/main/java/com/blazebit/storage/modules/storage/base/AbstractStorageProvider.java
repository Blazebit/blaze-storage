package com.blazebit.storage.modules.storage.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;

import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.spi.StorageProvider;
import com.blazebit.storage.core.api.spi.StorageResult;

public abstract class AbstractStorageProvider implements StorageProvider {

	private static final int BUFFER_SIZE = 8192;

	@Override
	public StorageResult copyObject(StorageProvider sourceStorageProvider, String contentKey) {
		try (InputStream is = sourceStorageProvider.getObject(contentKey)) {
			return createObject(is);
		} catch (IOException ex) {
			throw new StorageException("Could not copy object", ex);
		}
	}

	protected static long copy(InputStream source, OutputStream sink) throws IOException {
		long nread = 0L;
		byte[] buf = new byte[BUFFER_SIZE];
		int n;
		while ((n = source.read(buf)) > 0) {
			sink.write(buf, 0, n);
			nread += n;
		}
		return nread;
	}

	protected static StorageResult copyWithChecksum(MessageDigest md, InputStream source, OutputStream sink) throws IOException {
		md.reset();
		long nread = 0L;
		byte[] buf = new byte[BUFFER_SIZE];
		int n;
		while ((n = source.read(buf)) > 0) {
			sink.write(buf, 0, n);
			md.update(buf, 0, n);
			nread += n;
		}
		return new StorageResult(null, md.digest(), nread);
	}

	private final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

	private static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars);
	}

}
