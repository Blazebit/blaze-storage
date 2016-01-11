package com.blazebit.storage.testsuite.storage.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.junit.Test;

import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.spi.StorageProvider;
import com.blazebit.storage.testsuite.common.Assert;

public abstract class AbstractStorageProviderTest {
	
	protected abstract Path getDirectory();
	
	protected abstract StorageProvider getStorageProvider();

	/**************************
	 * getStorageIdentifier()
	 **************************/

	@Test
	public void testGetStorageIdentifier_whenSame() throws Exception {
		// When & Then
		assertEquals(getStorageProvider().getStorageIdentifier(), getStorageProvider().getStorageIdentifier());
	}
	
	/**************************
	 * getObject(String)
	 **************************/

	@Test
	public void testGetObject_whenExisting() throws Exception {
		// Given
		String fileName = createFile(getDirectory(), "xyz");

		// When
		String content = readFull(getStorageProvider().getObject(fileName));

		// Then
		assertEquals("xyz", content);
	}

	@Test
	public void testGetObject_whenNotExisting() throws Exception {
		// When & Then
		Assert.verifyException(getStorageProvider(), StorageException.class).getObject("not-existing");
	}
	
	/**************************
	 * deleteObject(String)
	 **************************/

	@Test
	public void testDeleteObject_whenExisting() throws Exception {
		// Given
		String fileName = createFile(getDirectory(), "xyz");

		// When
		getStorageProvider().deleteObject(fileName);

		// Then
		Path file = getDirectory().resolve(fileName);
		assertFalse(Files.exists(file));
	}

	@Test
	public void testDeleteObject_whenNotExisting() throws Exception {
		// When & Then
		getStorageProvider().deleteObject("not-existing");
	}

	@Test
	public void testDeleteObject_whenOpened() throws Exception {
		// Given
		String fileName = createFile(getDirectory(), "xyz");
		Path newFile = getDirectory().resolve(fileName);
		
		try (FileOutputStream fos = new FileOutputStream(newFile.toFile())) {
			FileChannel channel = fos.getChannel();
			// When & Then
			FileLock lock = null;
			
			try {
				channel.lock();
				Assert.verifyException(getStorageProvider(), StorageException.class).deleteObject(fileName);
			} finally {
				if (lock != null) {
					lock.release();
				}
			}
		}
	}
	
	/**************************
	 * createObject(String)
	 **************************/

	@Test
	public void testCreateObject_whenSuccessful() throws Exception {
		// When
		String fileName = getStorageProvider().createObject(new ByteArrayInputStream("abc".getBytes()));
		Path newFile = getDirectory().resolve(fileName);

		// Then
		assertTrue(Files.exists(newFile));
		assertEquals("abc", readFull(Files.newInputStream(newFile)));
	}
	
	/**************************
	 * putObject(String, InputStream)
	 **************************/

	@Test
	public void testPutObject_whenNew() throws Exception {
		// When
		getStorageProvider().putObject("test", new ByteArrayInputStream("abc".getBytes()));
		Path newFile = getDirectory().resolve("test");

		// Then
		assertTrue(Files.exists(newFile));
		assertEquals("abc", readFull(Files.newInputStream(newFile)));
	}

	@Test
	public void testPutObject_whenExisting() throws Exception {
		// Given
		String fileName = createFile(getDirectory(), "xyz");

		// When
		getStorageProvider().putObject(fileName, new ByteArrayInputStream("abc".getBytes()));
		Path newFile = getDirectory().resolve(fileName);

		// Then
		assertTrue(Files.exists(newFile));
		assertEquals("abc", readFull(Files.newInputStream(newFile)));
	}
	
	/**************************
	 * copyObject(String, InputStream)
	 **************************/

	@Test
	public void testCopyObject_whenSourceNotExisting() throws Exception {
		// When & Then
		Assert.verifyException(getStorageProvider(), StorageException.class).copyObject(getStorageProvider(), "not-existing");
	}

	@Test
	public void testCopyObject_whenTargetNotExisting() throws Exception {
		// Given
		String fileName = createFile(getDirectory(), "abc");
		
		// When
		String externalKey = getStorageProvider().copyObject(getStorageProvider(), fileName);
		Path newFile = getDirectory().resolve(externalKey);

		// Then
		assertTrue(Files.exists(newFile));
		assertEquals("abc", readFull(Files.newInputStream(newFile)));
	}
	
	/*************************************
	 * Private methods
	 ************************************/
	
	protected static String readFull(InputStream is) {
		if (is == null) {
			return null;
		}
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			return br.readLine();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// Ignore
			}
		}
	}
	
	protected static String createFile(Path basePath, String content) {
		try {
			Path tempFile = Files.createTempFile(basePath, null, null);
			Files.copy(new ByteArrayInputStream(content.getBytes()), tempFile, StandardCopyOption.REPLACE_EXISTING);
			return tempFile.getFileName().toString();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
}
