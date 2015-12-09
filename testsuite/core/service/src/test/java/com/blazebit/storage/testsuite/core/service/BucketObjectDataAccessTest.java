package com.blazebit.storage.testsuite.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.BucketObjectDataAccess;
import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.model.jpa.Account;
import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.BucketObject;
import com.blazebit.storage.core.model.jpa.BucketObjectId;
import com.blazebit.storage.core.model.jpa.BucketObjectState;
import com.blazebit.storage.core.model.jpa.BucketObjectVersion;
import com.blazebit.storage.core.model.jpa.BucketObjectVersionId;
import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.jpa.StorageQuotaModel;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlan;
import com.blazebit.storage.testsuite.common.AbstractContainerTest;
import com.blazebit.storage.testsuite.common.Assert;
import com.blazebit.storage.testsuite.common.DatabaseAware;
import com.blazebit.storage.testsuite.common.data.AccountTestData;
import com.blazebit.storage.testsuite.common.data.QuotaPlanTestData;
import com.blazebit.storage.testsuite.common.data.StorageTestData;
import com.blazebit.storage.testsuite.common.persistence.PersistenceUnits;
import com.blazebit.storage.testsuite.core.common.view.BucketObjectView;

@DatabaseAware(unitName = PersistenceUnits.STORAGE_TEST_MASTER_ONLY)
public class BucketObjectDataAccessTest extends AbstractContainerTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	
	@Inject
	private BucketObjectDataAccess bucketObjectDataAccess;

	private StorageQuotaModel defaultQuotaModel;
	private StorageQuotaPlan defaultQuotaPlan;
	
	private Account defaultOwner;
	private Storage defaultStorage;
	
	@Deployment
	public static Archive<?> createDeployment() {
		WebArchive archive = createBaseDeployment();
		return archive;
	}
	
	@Before
	public void prepareTestData() {
		defaultQuotaModel = QuotaPlanTestData.createModel();
		dataService.persist(defaultQuotaModel);
		
		defaultQuotaPlan = QuotaPlanTestData.createPlan(defaultQuotaModel);
		dataService.persist(defaultQuotaPlan);
		
		defaultOwner = AccountTestData.createAccount();
		dataService.persist(defaultOwner);
		
		defaultStorage = StorageTestData.createStorage(defaultOwner, defaultQuotaPlan);
		dataService.persist(defaultStorage);
	}
	
	/**************************
	 * findById(BucketObjectId)
	 **************************/

	@Test
	public void testFindById_whenUnkown() throws Exception {
		// Given
		Bucket bucket = dataService.persist(createBucket());
		BucketObjectId id = new BucketObjectId(bucket, "");

		// When
		BucketObject actual = bucketObjectDataAccess.findById(id);
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testFindById_whenHasOne() throws Exception {
		// Given
		Bucket bucket = dataService.persist(createBucket());
		BucketObject object1 = persist(createBucketObject(bucket, "test/obj1"));

		// When
		BucketObject actual = bucketObjectDataAccess.findById(object1.getId());
		
		// Then
		assertNotNull(actual);
		assertEquals(object1.getId(), actual.getId());
	}

	@Test
	public void testFindById_whenStillCreating() throws Exception {
		// Given
		Bucket bucket = dataService.persist(createBucket());
		BucketObject object1 = createBucketObject(bucket, "test/obj1");
		object1.setState(BucketObjectState.CREATING);
		persist(object1);

		// When
		BucketObject actual = bucketObjectDataAccess.findById(object1.getId());
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testFindById_whenStillRemoving() throws Exception {
		// Given
		Bucket bucket = dataService.persist(createBucket());
		BucketObject object1 = createBucketObject(bucket, "test/obj1");
		object1.setState(BucketObjectState.REMOVING);
		persist(object1);

		// When
		BucketObject actual = bucketObjectDataAccess.findById(object1.getId());
		
		// Then
		assertNull(actual);
	}
	
	/**************************
	 * findById(BucketObjectId, EntityViewSetting)
	 **************************/

	@Test
	public void testFindByIdWithSetting_whenUnkown() throws Exception {
		// Given
		EntityViewSetting<BucketObjectView, CriteriaBuilder<BucketObjectView>> setting = EntityViewSetting.create(BucketObjectView.class);
		Bucket bucket = dataService.persist(createBucket());
		BucketObjectId id = new BucketObjectId(bucket, "");

		// When
		BucketObjectView actual = bucketObjectDataAccess.findById(id, setting);
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testFindByIdWithSetting_whenHasOne() throws Exception {
		// Given
		EntityViewSetting<BucketObjectView, CriteriaBuilder<BucketObjectView>> setting = EntityViewSetting.create(BucketObjectView.class);
		Bucket bucket = dataService.persist(createBucket());
		BucketObject object1 = persist(createBucketObject(bucket, "test/obj1"));

		// When
		BucketObjectView actual = bucketObjectDataAccess.findById(object1.getId(), setting);
		
		// Then
		assertNotNull(actual);
		assertEquals(object1.getId(), actual.getId());
	}

	@Test
	public void testFindByIdWithSetting_whenStillCreating() throws Exception {
		// Given
		EntityViewSetting<BucketObjectView, CriteriaBuilder<BucketObjectView>> setting = EntityViewSetting.create(BucketObjectView.class);
		Bucket bucket = dataService.persist(createBucket());
		BucketObject object1 = createBucketObject(bucket, "test/obj1");
		object1.setState(BucketObjectState.CREATING);
		persist(object1);

		// When
		BucketObjectView actual = bucketObjectDataAccess.findById(object1.getId(), setting);
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testFindByIdWithSetting_whenStillRemoving() throws Exception {
		// Given
		EntityViewSetting<BucketObjectView, CriteriaBuilder<BucketObjectView>> setting = EntityViewSetting.create(BucketObjectView.class);
		Bucket bucket = dataService.persist(createBucket());
		BucketObject object1 = createBucketObject(bucket, "test/obj1");
		object1.setState(BucketObjectState.REMOVING);
		persist(object1);

		// When
		BucketObjectView actual = bucketObjectDataAccess.findById(object1.getId(), setting);
		
		// Then
		assertNull(actual);
	}
	
	/**************************
	 * getContent(URI, String)
	 **************************/

	@Test
	public void testGetContent_whenUnkown() throws Exception {
		// Given
		Path basePath = createBasePath();
		Map<String, String> configuration = createConfiguration();
		testStorage.setBasePath(basePath);

		// When
		InputStream actual = Assert.verifyException(bucketObjectDataAccess, StorageException.class).getContent(testStorage.createUri(configuration), "not-existing");
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testGetContent_whenHasOne() throws Exception {
		// Given
		Path basePath = createBasePath();
		Map<String, String> configuration = createConfiguration();
		testStorage.setBasePath(basePath);
		String externalKey1 = createFile(basePath, "1");

		// When
		InputStream actual = bucketObjectDataAccess.getContent(testStorage.createUri(configuration), externalKey1);
		
		// Then
		assertNotNull(actual);
		assertEquals("1", readFull(actual));
	}
	
	/*************************************
	 * Private methods
	 ************************************/
	
	private String readFull(InputStream is) {
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
	
	private Map<String, String> createConfiguration() {
		Map<String, String> configuration = new HashMap<>();
		
		return configuration;
	}
	
	private Path createBasePath() {
		try {
			return tempFolder.newFolder().toPath();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private String createFile(Path basePath, String content) {
		try {
			Path tempFile = Files.createTempFile(basePath, null, null);
			Files.copy(new ByteArrayInputStream(content.getBytes()), tempFile, StandardCopyOption.REPLACE_EXISTING);
			return tempFile.getFileName().toString();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private BucketObject createBucketObject(Bucket bucket, String name) {
		BucketObject bucketObject = new BucketObject();
		bucketObject.setId(new BucketObjectId(bucket, name));
		bucketObject.setBucket(bucket);
		bucketObject.setState(BucketObjectState.CREATED);
		
		BucketObjectVersion contentVersion = new BucketObjectVersion();
		contentVersion.setId(new BucketObjectVersionId(bucketObject, UUID.randomUUID().toString()));
		contentVersion.setBucketObject(bucketObject);
		contentVersion.setContentKey(UUID.randomUUID().toString());
		contentVersion.setContentLength(0);
		contentVersion.setContentType(BucketObjectVersion.DEFAULT_CONTENT_TYPE);
		contentVersion.setEntityTag("");
		contentVersion.setLastModified(System.currentTimeMillis());
		contentVersion.setState(BucketObjectState.CREATED);
		contentVersion.setStorage(bucket.getStorage());
		
		bucketObject.setContentVersion(contentVersion);
		bucketObject.setContentVersionUuid(contentVersion.getId().getVersionUuid());
		return bucketObject;
	}
	
	private BucketObject persist(BucketObject bucketObject) {
		BucketObjectVersion version = bucketObject.getContentVersion();
		bucketObject.setContentVersion(null);
		bucketObject.setContentVersionUuid(null);
		
		dataService.persist(bucketObject);
		dataService.persist(version);
		
		bucketObject.setContentVersion(version);
		bucketObject.setContentVersionUuid(version.getId().getVersionUuid());
		dataService.merge(bucketObject);
		
//		bucketObject.setContentVersion(version);
		
		return bucketObject;
	}
	
	private Bucket createBucket() {
		return createBucket(UUID.randomUUID().toString());
	}

	private Bucket createBucket(Account owner, Storage storage) {
		return createBucket(UUID.randomUUID().toString(), owner, storage);
	}
	
	private Bucket createBucket(String id) {
		return createBucket(id, defaultOwner, defaultStorage);
	}
	
	private Bucket createBucket(String id, Account owner, Storage storage) {
		Bucket bucket = new Bucket();
		bucket.setId(id);
		bucket.setOwner(owner);
		bucket.setStorage(storage);
		return bucket;
	}
}
