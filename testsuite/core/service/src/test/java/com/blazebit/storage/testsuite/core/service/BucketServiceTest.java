package com.blazebit.storage.testsuite.core.service;

import static org.junit.Assert.*;

import java.util.UUID;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;

import com.blazebit.storage.core.api.AccountNotFoundException;
import com.blazebit.storage.core.api.BucketNotEmptyException;
import com.blazebit.storage.core.api.BucketNotFoundException;
import com.blazebit.storage.core.api.BucketObjectService;
import com.blazebit.storage.core.api.BucketService;
import com.blazebit.storage.core.api.StorageNotFoundException;
import com.blazebit.storage.core.model.jpa.Account;
import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.BucketObject;
import com.blazebit.storage.core.model.jpa.BucketObjectId;
import com.blazebit.storage.core.model.jpa.BucketObjectState;
import com.blazebit.storage.core.model.jpa.BucketObjectVersion;
import com.blazebit.storage.core.model.jpa.BucketObjectVersionId;
import com.blazebit.storage.core.model.jpa.ObjectStatistics;
import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.jpa.StorageId;
import com.blazebit.storage.core.model.jpa.StorageQuotaModel;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlan;
import com.blazebit.storage.testsuite.common.AbstractContainerTest;
import com.blazebit.storage.testsuite.common.Assert;
import com.blazebit.storage.testsuite.common.DatabaseAware;
import com.blazebit.storage.testsuite.common.data.AccountTestData;
import com.blazebit.storage.testsuite.common.data.QuotaPlanTestData;
import com.blazebit.storage.testsuite.common.data.StorageTestData;
import com.blazebit.storage.testsuite.common.persistence.PersistenceUnits;

@DatabaseAware(unitName = PersistenceUnits.STORAGE_TEST_MASTER_ONLY)
public class BucketServiceTest extends AbstractContainerTest {

	@Inject
	private BucketService bucketService;
	@Inject
	private BucketObjectService bucketObjectService;

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
	 * put(Bucket)
	 **************************/
	
	@Test
	public void testPut_whenCreating() throws Exception {
		// Given
		Bucket bucket = createBucket("test");

		// When
		bucketService.put(bucket);
		
		// Then
		Bucket actualBucket = em.find(Bucket.class, bucket.getId());
		Storage actualStorage = em.find(Storage.class, defaultStorage.getId());
		
		assertFalse(actualBucket.getDeleted());
		assertEquals(defaultStorage, actualBucket.getStorage());
		assertEquals(defaultOwner, actualBucket.getOwner());
		
		ObjectStatistics expectedStatistics = new ObjectStatistics();
		expectedStatistics.setObjectCount(0L);
		expectedStatistics.setObjectBytes(0L);
		expectedStatistics.setObjectVersionCount(0L);
		expectedStatistics.setObjectVersionBytes(0L);
		expectedStatistics.setPendingObjectCount(0L);
		expectedStatistics.setPendingObjectBytes(0L);
		expectedStatistics.setPendingObjectVersionCount(0L);
		expectedStatistics.setPendingObjectVersionBytes(0L);
		assertEquals(expectedStatistics, actualBucket.getStatistics());
		assertEquals(expectedStatistics, actualStorage.getStatistics());
	}
	
	@Test
	public void testPut_whenUpdating() throws Exception {
		// Given
		Bucket bucket = dataService.persist(createBucket("test"));
		
		Storage newStorage = StorageTestData.createStorage(defaultOwner, defaultQuotaPlan);
		newStorage.getId().setName("newStorage");
		newStorage = dataService.persist(newStorage);

		Bucket newBucket = createBucket("test");
		newBucket.setStorage(newStorage);

		// When
		bucketService.put(newBucket);
		
		// Then
		Bucket actualBucket = em.find(Bucket.class, bucket.getId());
		Storage actualStorage = em.find(Storage.class, newStorage.getId());
		
		assertFalse(actualBucket.getDeleted());
		assertEquals(newStorage, actualBucket.getStorage());
		assertEquals(defaultOwner, actualBucket.getOwner());
		
		ObjectStatistics expectedStatistics = new ObjectStatistics();
		expectedStatistics.setObjectCount(0L);
		expectedStatistics.setObjectBytes(0L);
		expectedStatistics.setObjectVersionCount(0L);
		expectedStatistics.setObjectVersionBytes(0L);
		expectedStatistics.setPendingObjectCount(0L);
		expectedStatistics.setPendingObjectBytes(0L);
		expectedStatistics.setPendingObjectVersionCount(0L);
		expectedStatistics.setPendingObjectVersionBytes(0L);
		assertEquals(expectedStatistics, actualBucket.getStatistics());
		assertEquals(expectedStatistics, actualStorage.getStatistics());
	}
	
	@Test
	public void testPut_whenUpdatingIgnoredProperties() throws Exception {
		// Given
		Bucket bucket = dataService.persist(createBucket("test"));

		Bucket newBucket = createBucket("test");
		newBucket.setStatistics(new ObjectStatistics(1L, 1L, 1L, 1L, 1L, 1L, 1L, 1L));
		newBucket.setDeleted(true);
		newBucket.setOwner(new Account(-1L));

		// When
		bucketService.put(newBucket);
		
		// Then
		Bucket actualBucket = em.find(Bucket.class, bucket.getId());
		Storage actualStorage = em.find(Storage.class, defaultStorage.getId());

		assertFalse(actualBucket.getDeleted());
		assertEquals(defaultStorage, actualBucket.getStorage());
		assertEquals(defaultOwner, actualBucket.getOwner());
		
		ObjectStatistics expectedStatistics = new ObjectStatistics();
		expectedStatistics.setObjectCount(0L);
		expectedStatistics.setObjectBytes(0L);
		expectedStatistics.setObjectVersionCount(0L);
		expectedStatistics.setObjectVersionBytes(0L);
		expectedStatistics.setPendingObjectCount(0L);
		expectedStatistics.setPendingObjectBytes(0L);
		expectedStatistics.setPendingObjectVersionCount(0L);
		expectedStatistics.setPendingObjectVersionBytes(0L);
		assertEquals(expectedStatistics, actualBucket.getStatistics());
		assertEquals(expectedStatistics, actualStorage.getStatistics());
	}
	
	@Test
	public void testPut_whenCreatingForNonExistingAccount() throws Exception {
		// Given
		Bucket bucket = createBucket("test");
		bucket.setOwner(new Account(-1L));

		// When & Then
		Assert.verifyException(bucketService, AccountNotFoundException.class).put(bucket);
	}
	
	@Test
	public void testPut_whenCreatingForNonExistingStorage() throws Exception {
		// Given
		Bucket bucket = createBucket("test");
		bucket.setStorage(new Storage(new StorageId(defaultOwner.getId(), "not-existing")));

		// When & Then
		Assert.verifyException(bucketService, StorageNotFoundException.class).put(bucket);
	}
	
	@Test
	public void testPut_whenUpdatingWithNonExistingStorage() throws Exception {
		// Given
		Bucket bucket = dataService.persist(createBucket("test"));
		
		Bucket newBucket = createBucket("test");
		newBucket.setStorage(new Storage(new StorageId(defaultOwner.getId(), "not-existing")));

		// When
		Assert.verifyException(bucketService, StorageNotFoundException.class).put(newBucket);
		
		// Then
		Bucket actual = em.find(Bucket.class, bucket.getId());
		assertEquals(defaultStorage, actual.getStorage());
	}
	
	/**************************
	 * delete(String)
	 **************************/
	
	@Test
	public void testDelete_whenSuccessful() throws Exception {
		// Given
		Bucket bucket = dataService.persist(createBucket("test"));

		// When
		bucketService.delete(bucket.getId());
		
		// Then
		Bucket actual = em.find(Bucket.class, bucket.getId());
		assertNull(actual);
	}
	
	@Test
	public void testDelete_whenNotExisting() throws Exception {
		// Given
		Bucket bucket = dataService.persist(createBucket("test"));
		
		// When
		Assert.verifyException(bucketService, BucketNotFoundException.class).delete("not-existing");
		
		// Then
		Bucket actual = em.find(Bucket.class, bucket.getId());
		assertNotNull(actual);
	}
	
	@Test
	public void testDelete_whenNotEmpty() throws Exception {
		// Given
		Bucket bucket = dataService.persist(createBucket("test"));
		BucketObject bucketObject = createBucketObject(bucket, "test");
		bucketObjectService.put(bucketObject);
		
		// When
		Assert.verifyException(bucketService, BucketNotEmptyException.class).delete(bucket.getId());
		
		// Then
		Bucket actual = em.find(Bucket.class, bucket.getId());
		assertNotNull(actual);
	}
	
	@Test
	public void testDelete_whenPendingObjects() throws Exception {
		// Given
		Bucket bucket = dataService.persist(createBucket("test"));
		BucketObject bucketObject = createBucketObject(bucket, "test");
		bucketObjectService.put(bucketObject);
		bucketObjectService.delete(bucketObject.getId());
		
		// When
		bucketService.delete(bucket.getId());
		
		// Then
		Bucket actual = em.find(Bucket.class, bucket.getId());
		assertNotNull(actual);
		assertTrue(actual.getDeleted());
	}
	
	@Test
	public void testDelete_whenDeletedObjects() throws Exception {
		// Given
		Bucket bucket = dataService.persist(createBucket("test"));
		BucketObject bucketObject = createBucketObject(bucket, "test");
		bucketObjectService.put(bucketObject);
		bucketObjectService.delete(bucketObject.getId());
		bucketService.delete(bucket.getId());
		
		// When
		Assert.verifyException(bucketService, BucketNotFoundException.class).delete(bucket.getId());
		
		// Then
		Bucket actual = em.find(Bucket.class, bucket.getId());
		assertNotNull(actual);
		assertTrue(actual.getDeleted());
	}
	
	/*************************************
	 * Private methods
	 ************************************/
	
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
}
