package com.blazebit.storage.testsuite.storage.ftp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.spi.StorageProvider;
import com.blazebit.storage.core.api.spi.StorageProviderFactory;
import com.blazebit.storage.core.api.spi.StorageProviderMetamodel;
import com.blazebit.storage.modules.storage.ftp.FtpStorage;
import com.blazebit.storage.modules.storage.ftp.FtpStorageProviderFactory;
import com.blazebit.storage.testsuite.common.Assert;

public class FtpStorageProviderFactoryTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	private FtpServer ftpServer;
	private File directory;
	private StorageProviderFactory factory;
	
	@Before
	public void before() throws Exception {
		directory = folder.newFolder();
		FtpServerFactory serverFactory = new FtpServerFactory();
		ListenerFactory listenerFactory = new ListenerFactory();
		listenerFactory.setServerAddress("127.0.0.1");
		listenerFactory.setPort(21000);
		serverFactory.addListener("default", listenerFactory.createListener());
		serverFactory.setUserManager(new AdminUserManagerFactory(directory.getAbsolutePath()));
		
		try {
			ftpServer = serverFactory.createServer();
			ftpServer.start();
		} catch (FtpException ex) {
			throw new RuntimeException(ex);
		}
		
		factory = new FtpStorageProviderFactory();
	}
	
	@After
	public void after() {
		ftpServer.stop();
	}

	/**************************
	 * getMetamodel()
	 **************************/

	@Test
	public void testMetamodel() {
		StorageProviderMetamodel metamodel = factory.getMetamodel();
		
		assertEquals("ftp", metamodel.getScheme());
		assertEquals(7, metamodel.getConfigurationElements().size());
		
		assertEquals("text", metamodel.getConfigurationElement(FtpStorage.URL_PROPERTY).getType());
		assertEquals("boolean", metamodel.getConfigurationElement(FtpStorage.CREATE_DIRECTORY_PROPERTY).getType());
		assertEquals("text", metamodel.getConfigurationElement(FtpStorage.HOST_PROPERTY).getType());
		assertEquals("integer", metamodel.getConfigurationElement(FtpStorage.PORT_PROPERTY).getType());
		assertEquals("text", metamodel.getConfigurationElement(FtpStorage.USER_PROPERTY).getType());
		assertEquals("password", metamodel.getConfigurationElement(FtpStorage.PASSWORD_PROPERTY).getType());
		assertEquals("text", metamodel.getConfigurationElement(FtpStorage.BASE_PATH_PROPERTY).getType());
	}
	
	/**************************
	 * createStorageProvider(Map)
	 **************************/

	@Test
	public void testCreateStorageProvider_whenSuccessful() throws Exception {
		// Given
		Files.createDirectory(directory.toPath().resolve("test-1"));

		// When
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(FtpStorage.URL_PROPERTY, "ftp://admin:admin@localhost:21000/test-1");
		properties.put(FtpStorage.CREATE_DIRECTORY_PROPERTY, "false");
		StorageProvider provider = factory.createStorageProvider(properties);

		// Then
		assertNotNull(provider);
	}

	@Test
	public void testCreateStorageProvider_whenDirectoryDoesNotExist() throws Exception {
		// When & Then
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(FtpStorage.URL_PROPERTY, "ftp://admin:admin@localhost:21000/test-1");
		properties.put(FtpStorage.CREATE_DIRECTORY_PROPERTY, "false");
		Assert.verifyException(factory, StorageException.class).createStorageProvider(properties);
	}

	@Test
	public void testCreateStorageProvider_whenSuccessfulCreatingDirectory() throws Exception {
		// When
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(FtpStorage.URL_PROPERTY, "ftp://admin:admin@localhost:21000/test-1");
		properties.put(FtpStorage.CREATE_DIRECTORY_PROPERTY, "true");
		StorageProvider provider = factory.createStorageProvider(properties);

		// Then
		assertNotNull(provider);
		assertTrue(Files.exists(directory.toPath().resolve("test-1")));
	}

	@Test
	public void testCreateStorageProvider_whenResolvesToFile() throws Exception {
		// Given
		Files.createFile(directory.toPath().resolve("test-1"));

		// When & Then
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(FtpStorage.URL_PROPERTY, "ftp://admin:admin@localhost:21000/test-1");
		properties.put(FtpStorage.CREATE_DIRECTORY_PROPERTY, "false");
		Assert.verifyException(factory, StorageException.class).createStorageProvider(properties);
	}
}
