package com.blazebit.storage.testsuite.storage.local;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.blazebit.storage.core.api.spi.StorageProvider;
import com.blazebit.storage.modules.storage.local.LocalStorage;
import com.blazebit.storage.modules.storage.local.LocalStorageProviderFactory;
import com.blazebit.storage.testsuite.storage.common.AbstractStorageProviderTest;

public class LocalStorageProviderTest extends AbstractStorageProviderTest {
	
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	private File directory;
	private StorageProvider storageProvider;
	
	@Before
	public void before() throws Exception {
		LocalStorageProviderFactory factory = new LocalStorageProviderFactory();
		directory = folder.newFolder();
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(LocalStorage.BASE_PATH_PROPERTY, directory.getAbsolutePath());
		storageProvider = factory.createStorageProvider(properties);
	}
	
	@Override
	protected Path getDirectory() {
		return directory.toPath();
	}

	@Override
	protected StorageProvider getStorageProvider() {
		return storageProvider;
	}
	
	/**************************
	 * getTotalSpace() & getUsableSpace() & getUnallocatedSpace()
	 **************************/

	@Test
	public void testSpaceStatistics() throws Exception {
		// There might be some file operations on this system so let's use a big enough delta
		assertEquals(directory.getTotalSpace(), storageProvider.getTotalSpace(), 100_000);
		assertEquals(directory.getUsableSpace(), storageProvider.getUsableSpace(), 100_000);
		assertEquals(directory.getFreeSpace(), storageProvider.getUnallocatedSpace(), 100_000);
	}
	
	/**************************
	 * supportsDelete() & supportsPut()
	 **************************/

	@Test
	public void testFeatures() throws Exception {
		assertTrue(storageProvider.supportsDelete());
		assertTrue(storageProvider.supportsPut());
	}
}
