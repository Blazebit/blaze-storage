package com.blazebit.storage.testsuite.storage.ftp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Path;
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

import com.blazebit.storage.core.api.spi.StorageProvider;
import com.blazebit.storage.modules.storage.ftp.FtpStorage;
import com.blazebit.storage.modules.storage.ftp.FtpStorageProviderFactory;
import com.blazebit.storage.testsuite.storage.common.AbstractStorageProviderTest;

public class FtpStorageProviderTest extends AbstractStorageProviderTest {

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	private FtpServer ftpServer;
	private File directory;
	private StorageProvider storageProvider;
	
	@Before
	public void before() throws Exception {
		directory = new File(folder.newFolder(), "test-1");
		directory.mkdir();
		
		FtpServerFactory serverFactory = new FtpServerFactory();
		ListenerFactory listenerFactory = new ListenerFactory();
		listenerFactory.setPort(21);
		serverFactory.addListener("default", listenerFactory.createListener());
		serverFactory.setUserManager(new AdminUserManagerFactory(directory.getParentFile().getAbsolutePath()));
		
		try {
			ftpServer = serverFactory.createServer();
			ftpServer.start();
		} catch (FtpException ex) {
			throw new RuntimeException(ex);
		}
		
		FtpStorageProviderFactory factory = new FtpStorageProviderFactory();
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(FtpStorage.URL_PROPERTY, "ftp://admin:admin@localhost/" + directory.getName());
		properties.put(FtpStorage.CREATE_DIRECTORY_PROPERTY, "false");
		storageProvider = factory.createStorageProvider(properties);
	}
	
	@After
	public void after() {
		ftpServer.stop();
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
		// FTP does not support these space statistics
		assertEquals(-1, storageProvider.getTotalSpace());
		assertEquals(-1, storageProvider.getUsableSpace());
		assertEquals(-1, storageProvider.getUnallocatedSpace());
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
