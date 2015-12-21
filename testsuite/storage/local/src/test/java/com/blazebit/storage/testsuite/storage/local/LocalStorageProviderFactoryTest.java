package com.blazebit.storage.testsuite.storage.local;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.spi.StorageProvider;
import com.blazebit.storage.core.api.spi.StorageProviderMetamodel;
import com.blazebit.storage.modules.storage.local.LocalStorage;
import com.blazebit.storage.modules.storage.local.LocalStorageProviderFactory;
import com.blazebit.storage.testsuite.common.Assert;

public class LocalStorageProviderFactoryTest {
	
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	private LocalStorageProviderFactory factory;
	
	@Before
	public void before() {
		factory = new LocalStorageProviderFactory();
	}

	/**************************
	 * getMetamodel()
	 **************************/

	@Test
	public void testMetamodel() {
		StorageProviderMetamodel metamodel = factory.getMetamodel();
		
		assertEquals("file", metamodel.getScheme());
		assertEquals(1, metamodel.getConfigurationElements().size());
		
		assertEquals("text", metamodel.getConfigurationElement(LocalStorage.BASE_PATH_PROPERTY).getType());
	}
	
	/**************************
	 * createStorageProvider(Map)
	 **************************/

	@Test
	public void testCreateStorageProvider_whenSuccessful() throws Exception {
		File directory = folder.newFolder();
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(LocalStorage.BASE_PATH_PROPERTY, directory.getAbsolutePath());
		StorageProvider provider = factory.createStorageProvider(properties);
		assertNotNull(provider);
	}

	@Test
	public void testCreateStorageProvider_whenNormalizing1() throws Exception {
		File directory = folder.newFolder();
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(LocalStorage.BASE_PATH_PROPERTY, "/" + directory.getAbsolutePath());
		StorageProvider provider = factory.createStorageProvider(properties);
		assertNotNull(provider);
	}

	@Test
	public void testCreateStorageProvider_whenNormalizing2() throws Exception {
		File directory = folder.newFolder();
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(LocalStorage.BASE_PATH_PROPERTY, "/" + directory.getAbsolutePath().replace('\\', '/'));
		StorageProvider provider = factory.createStorageProvider(properties);
		assertNotNull(provider);
	}

	@Test
	public void testCreateStorageProvider_whenNotSet() throws Exception {
		Map<String, Object> properties = new HashMap<String, Object>();
		Assert.verifyException(factory, StorageException.class).createStorageProvider(properties);
	}

	@Test
	public void testCreateStorageProvider_whenNotEmpty() throws Exception {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(LocalStorage.BASE_PATH_PROPERTY, "");
		Assert.verifyException(factory, StorageException.class).createStorageProvider(properties);
	}

	@Test
	public void testCreateStorageProvider_whenNotExists() throws Exception {
		File directory = folder.newFolder();
		String path = directory.getAbsolutePath();
		directory.delete();
		
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(LocalStorage.BASE_PATH_PROPERTY, path);
		Assert.verifyException(factory, StorageException.class).createStorageProvider(properties);
	}

	@Test
	public void testCreateStorageProvider_whenNotDirectory() throws Exception {
		File file = folder.newFile();
		
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(LocalStorage.BASE_PATH_PROPERTY, file.getAbsolutePath());
		Assert.verifyException(factory, StorageException.class).createStorageProvider(properties);
	}
}
