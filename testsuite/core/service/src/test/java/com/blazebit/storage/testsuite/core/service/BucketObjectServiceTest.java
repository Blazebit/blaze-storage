package com.blazebit.storage.testsuite.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
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

import com.blazebit.storage.core.api.BucketObjectNotFoundException;
import com.blazebit.storage.core.api.BucketObjectService;
import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.model.jpa.Account;
import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.BucketObject;
import com.blazebit.storage.core.model.jpa.BucketObjectId;
import com.blazebit.storage.core.model.jpa.BucketObjectState;
import com.blazebit.storage.core.model.jpa.BucketObjectVersion;
import com.blazebit.storage.core.model.jpa.BucketObjectVersionId;
import com.blazebit.storage.core.model.jpa.ObjectStatistics;
import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.jpa.StorageQuotaModel;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlan;
import com.blazebit.storage.core.model.service.BucketObjectDeleteReport;
import com.blazebit.storage.core.model.service.BucketObjectDeleteReportItem;
import com.blazebit.storage.testsuite.common.AbstractContainerTest;
import com.blazebit.storage.testsuite.common.Assert;
import com.blazebit.storage.testsuite.common.DatabaseAware;
import com.blazebit.storage.testsuite.common.data.AccountTestData;
import com.blazebit.storage.testsuite.common.data.QuotaPlanTestData;
import com.blazebit.storage.testsuite.common.data.StorageTestData;
import com.blazebit.storage.testsuite.common.persistence.PersistenceUnits;

@DatabaseAware(unitName = PersistenceUnits.STORAGE_TEST_MASTER_ONLY)
public class BucketObjectServiceTest extends AbstractContainerTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	
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
	
	/*********************************
	 * createContent(URI, InputStream)
	 ********************************/

	@Test
	public void testCreateContent_whenIOError() throws Exception {
		// Given
		Path basePath = createBasePath();
		Map<String, String> configuration = createConfiguration();
		testStorage.setBasePath(basePath);

		// When
		InputStream is = null;
		String actual = null;
		
		try {
			is = createTempFile("1");
			is.close();
			actual = Assert.verifyException(bucketObjectService, StorageException.class).createContent(testStorage.createUri(configuration), is);
		} finally {
			is.close();
		}
		
		// Then
		assertNull(actual);
	}
	
	@Test
	public void testCreateContent_whenSuccessful() throws Exception {
		// Given
		Path basePath = createBasePath();
		Map<String, String> configuration = createConfiguration();
		testStorage.setBasePath(basePath);

		// When
		InputStream is = null;
		String actual = null;
		
		try {
			is = createTempFile("1");
			actual = bucketObjectService.createContent(testStorage.createUri(configuration), is);
		} finally {
			is.close();
		}
		
		// Then
		assertTrue(Files.exists(basePath.resolve(actual)));
		assertEquals("1", readFull(Files.newInputStream(basePath.resolve(actual))));
	}
	
	/**************************
	 * put(BucketObject)
	 **************************/
	
	@Test
	public void testPut_whenCreating() throws Exception {
		// Given
		ObjectStatistics initialStatistics = new ObjectStatistics();
		initialStatistics.setObjectCount(10L);
		initialStatistics.setObjectBytes(1234L);
		initialStatistics.setObjectVersionCount(14L);
		initialStatistics.setObjectVersionBytes(1234L);
		initialStatistics.setPendingObjectCount(10L);
		initialStatistics.setPendingObjectBytes(1234L);
		initialStatistics.setPendingObjectVersionCount(14L);
		initialStatistics.setPendingObjectVersionBytes(1234L);
		defaultStorage.setStatistics(initialStatistics);
		dataService.merge(defaultStorage);
		
		Bucket bucket = createBucket();
		bucket.setStatistics(initialStatistics);
		bucket = dataService.persist(bucket);
		
		BucketObject bucketObject = createBucketObject(bucket, "test");
		bucketObject.getContentVersion().setContentLength(10L);

		// When
		bucketObjectService.put(bucketObject);
		
		// Then
		BucketObject actual = em.find(BucketObject.class, bucketObject.getId());
		Bucket actualBucket = em.find(Bucket.class, bucket.getId());
		Storage actualStorage = em.find(Storage.class, bucket.getStorage().getId());
		
		assertEquals(bucketObject.getContentVersionUuid(), actual.getContentVersionUuid());
		assertEquals(BucketObjectState.CREATED, actual.getState());
		
		ObjectStatistics expectedStatistics = new ObjectStatistics();
		expectedStatistics.setObjectCount(1L);
		expectedStatistics.setObjectBytes(10L);
		expectedStatistics.setObjectVersionCount(1L);
		expectedStatistics.setObjectVersionBytes(10L);
		expectedStatistics.setPendingObjectCount(1L);
		expectedStatistics.setPendingObjectBytes(10L);
		expectedStatistics.setPendingObjectVersionCount(1L);
		expectedStatistics.setPendingObjectVersionBytes(10L);
		expectedStatistics = initialStatistics.plus(expectedStatistics);
		assertEquals(expectedStatistics, actualBucket.getStatistics());
		assertEquals(expectedStatistics, actualStorage.getStatistics());
	}
	
	@Test
	public void testPut_whenUpdating() throws Exception {
		// Given
		Bucket bucket = dataService.persist(createBucket());
		BucketObject bucketObject = createBucketObject(bucket, "test");
		bucketObject.getContentVersion().setContentLength(10L);
		bucketObjectService.put(bucketObject);

		bucketObject = createBucketObject(bucket, "test");
		bucketObject.getContentVersion().setContentLength(200L);
		
		// When
		bucketObjectService.put(bucketObject);
		
		// Then
		BucketObject actual = em.find(BucketObject.class, bucketObject.getId());
		Bucket actualBucket = em.find(Bucket.class, bucket.getId());
		Storage actualStorage = em.find(Storage.class, bucket.getStorage().getId());
		
		assertEquals(bucketObject.getContentVersionUuid(), actual.getContentVersionUuid());
		assertEquals(BucketObjectState.CREATED, actual.getState());
		
		ObjectStatistics expectedStatistics = new ObjectStatistics();
		expectedStatistics.setObjectCount(1L);
		expectedStatistics.setObjectBytes(200L);
		expectedStatistics.setObjectVersionCount(2L);
		expectedStatistics.setObjectVersionBytes(210L);
		expectedStatistics.setPendingObjectCount(1L);
		expectedStatistics.setPendingObjectBytes(200L);
		expectedStatistics.setPendingObjectVersionCount(2L);
		expectedStatistics.setPendingObjectVersionBytes(210L);
		assertEquals(expectedStatistics, actualBucket.getStatistics());
		assertEquals(expectedStatistics, actualStorage.getStatistics());
	}
	
	@Test
	public void testPut_whenMovingStorage() throws Exception {
		// Given
		Bucket bucket = dataService.persist(createBucket());
		BucketObject bucketObject = createBucketObject(bucket, "test");
		bucketObject.getContentVersion().setContentLength(10L);
		bucketObjectService.put(bucketObject);
		
		Storage newStorage = StorageTestData.createStorage(defaultOwner, defaultQuotaPlan);
		newStorage.getId().setName("newStorage");
		newStorage = dataService.persist(newStorage);

		bucketObject = createBucketObject(bucket, "test");
		bucketObject.getContentVersion().setContentLength(200L);
		bucketObject.getContentVersion().setStorage(newStorage);
		
		// When
		bucketObjectService.put(bucketObject);
		
		// Then
		BucketObject actual = em.find(BucketObject.class, bucketObject.getId());
		Bucket actualBucket = em.find(Bucket.class, bucket.getId());
		Storage actualStorage = em.find(Storage.class, defaultStorage.getId());
		Storage actualNewStorage = em.find(Storage.class, newStorage.getId());
		
		assertEquals(bucketObject.getContentVersionUuid(), actual.getContentVersionUuid());
		assertEquals(BucketObjectState.CREATED, actual.getState());
		
		ObjectStatistics expectedBucketStatistics = new ObjectStatistics();
		expectedBucketStatistics.setObjectCount(1L);
		expectedBucketStatistics.setObjectBytes(200L);
		expectedBucketStatistics.setObjectVersionCount(2L);
		expectedBucketStatistics.setObjectVersionBytes(210L);
		expectedBucketStatistics.setPendingObjectCount(1L);
		expectedBucketStatistics.setPendingObjectBytes(200L);
		expectedBucketStatistics.setPendingObjectVersionCount(2L);
		expectedBucketStatistics.setPendingObjectVersionBytes(210L);
		assertEquals(expectedBucketStatistics, actualBucket.getStatistics());

		ObjectStatistics expectedDefaultStorageStatistics = new ObjectStatistics();
		expectedDefaultStorageStatistics.setObjectCount(0L);
		expectedDefaultStorageStatistics.setObjectBytes(0L);
		expectedDefaultStorageStatistics.setObjectVersionCount(1L);
		expectedDefaultStorageStatistics.setObjectVersionBytes(10L);
		expectedDefaultStorageStatistics.setPendingObjectCount(0L);
		expectedDefaultStorageStatistics.setPendingObjectBytes(0L);
		expectedDefaultStorageStatistics.setPendingObjectVersionCount(1L);
		expectedDefaultStorageStatistics.setPendingObjectVersionBytes(10L);
		assertEquals(expectedDefaultStorageStatistics, actualStorage.getStatistics());

		ObjectStatistics expectedNewStorageStatistics = new ObjectStatistics();
		expectedNewStorageStatistics.setObjectCount(1L);
		expectedNewStorageStatistics.setObjectBytes(200L);
		expectedNewStorageStatistics.setObjectVersionCount(1L);
		expectedNewStorageStatistics.setObjectVersionBytes(200L);
		expectedNewStorageStatistics.setPendingObjectCount(1L);
		expectedNewStorageStatistics.setPendingObjectBytes(200L);
		expectedNewStorageStatistics.setPendingObjectVersionCount(1L);
		expectedNewStorageStatistics.setPendingObjectVersionBytes(200L);
		assertEquals(expectedNewStorageStatistics, actualNewStorage.getStatistics());
	}
	
	/**************************
	 * delete(BucketObjectId)
	 **************************/
	
	@Test
	public void testDelete_whenSuccessful() throws Exception {
		// Given
		Bucket bucket = dataService.persist(createBucket());
		BucketObject bucketObject = createBucketObject(bucket, "test");
		bucketObject.getContentVersion().setContentLength(10L);
		bucketObjectService.put(bucketObject);

		// When
		bucketObjectService.delete(bucketObject.getId());
		em.clear();
		
		// Then
		BucketObject actual = em.find(BucketObject.class, bucketObject.getId());
		Bucket actualBucket = em.find(Bucket.class, bucket.getId());
		Storage actualStorage = em.find(Storage.class, bucket.getStorage().getId());
		
		assertEquals(BucketObjectState.REMOVING, actual.getState());
		
		ObjectStatistics expectedStatistics = new ObjectStatistics();
		expectedStatistics.setObjectCount(0L);
		expectedStatistics.setObjectBytes(0L);
		expectedStatistics.setObjectVersionCount(0L);
		expectedStatistics.setObjectVersionBytes(0L);
		expectedStatistics.setPendingObjectCount(1L);
		expectedStatistics.setPendingObjectBytes(10L);
		expectedStatistics.setPendingObjectVersionCount(1L);
		expectedStatistics.setPendingObjectVersionBytes(10L);
		assertEquals(expectedStatistics, actualBucket.getStatistics());
		assertEquals(expectedStatistics, actualStorage.getStatistics());
	}
	
	@Test
	public void testDelete_whenNotExists() throws Exception {
		// Given
		Bucket bucket = dataService.persist(createBucket());
		BucketObject bucketObject = createBucketObject(bucket, "test");
		bucketObject.getContentVersion().setContentLength(10L);
		bucketObjectService.put(bucketObject);

		// When
		Assert.verifyException(bucketObjectService, BucketObjectNotFoundException.class).delete(new BucketObjectId(bucket, "non-existing"));
		
		// Then
		BucketObject actual = em.find(BucketObject.class, bucketObject.getId());
		Bucket actualBucket = em.find(Bucket.class, bucket.getId());
		Storage actualStorage = em.find(Storage.class, bucket.getStorage().getId());

		assertEquals(bucketObject.getContentVersionUuid(), actual.getContentVersionUuid());
		assertEquals(BucketObjectState.CREATED, actual.getState());
		
		ObjectStatistics expectedStatistics = new ObjectStatistics();
		expectedStatistics.setObjectCount(1L);
		expectedStatistics.setObjectBytes(10L);
		expectedStatistics.setObjectVersionCount(1L);
		expectedStatistics.setObjectVersionBytes(10L);
		expectedStatistics.setPendingObjectCount(1L);
		expectedStatistics.setPendingObjectBytes(10L);
		expectedStatistics.setPendingObjectVersionCount(1L);
		expectedStatistics.setPendingObjectVersionBytes(10L);
		assertEquals(expectedStatistics, actualBucket.getStatistics());
		assertEquals(expectedStatistics, actualStorage.getStatistics());
	}
	
	/**************************
	 * delete(List<BucketObjectId>)
	 **************************/
	
	@Test
	public void testDeleteMultiple_mixedExistsMultipleBuckets() throws Exception {
		// Given
		Bucket bucket1 = dataService.persist(createBucket("bucket1"));
		BucketObject bucketObject1_1 = createBucketObject(bucket1, "test1");
		bucketObject1_1.getContentVersion().setContentLength(10L);
		bucketObjectService.put(bucketObject1_1);
		BucketObject bucketObject1_2 = createBucketObject(bucket1, "test2");
		bucketObject1_2.getContentVersion().setContentLength(10L);
		bucketObjectService.put(bucketObject1_2);
		
		Bucket bucket2 = dataService.persist(createBucket("bucket2"));
		BucketObject bucketObject2_1_1 = createBucketObject(bucket2, "test1");
		bucketObject2_1_1.getContentVersion().setContentLength(10L);
		bucketObjectService.put(bucketObject2_1_1);
		BucketObject bucketObject2_1_2 = createBucketObject(bucket2, "test1");
		bucketObject2_1_2.getContentVersion().setContentLength(10L);
		bucketObjectService.put(bucketObject2_1_2);

		// When
		BucketObjectDeleteReport report = bucketObjectService.delete(Arrays.asList(bucketObject1_2.getId(), bucketObject2_1_2.getId(), new BucketObjectId(bucket2.getId(), "non-existing")));
		
		// Then
		assertEquals(4, report.getItems().size());
		assertTrue(report.getItems().remove(BucketObjectDeleteReportItem.deleted(bucketObject1_2.getId(), bucketObject1_2.getContentVersionUuid())));
		assertTrue(report.getItems().remove(BucketObjectDeleteReportItem.deleted(bucketObject2_1_1.getId(), bucketObject2_1_1.getContentVersionUuid())));
		assertTrue(report.getItems().remove(BucketObjectDeleteReportItem.deleted(bucketObject2_1_2.getId(), bucketObject2_1_2.getContentVersionUuid())));
		assertTrue(report.getItems().remove(BucketObjectDeleteReportItem.error(new BucketObjectId(bucket2.getId(), "non-existing"), "BucketObjectNotFound", "not found")));
		
		BucketObject actual1_1 = em.find(BucketObject.class, bucketObject1_1.getId());
		BucketObject actual1_2 = em.find(BucketObject.class, bucketObject1_2.getId());
		BucketObject actual2_1 = em.find(BucketObject.class, bucketObject2_1_2.getId());
		BucketObjectVersion actual2_1_1 = em.find(BucketObjectVersion.class, bucketObject2_1_1.getContentVersion().getId());
		BucketObjectVersion actual2_1_2 = em.find(BucketObjectVersion.class, bucketObject2_1_2.getContentVersion().getId());
		Bucket actualBucket1 = em.find(Bucket.class, bucket1.getId());
		Bucket actualBucket2 = em.find(Bucket.class, bucket2.getId());
		Storage actualStorage = em.find(Storage.class, defaultStorage.getId());

		assertEquals(BucketObjectState.CREATED, actual1_1.getState());
		assertEquals(BucketObjectState.REMOVING, actual1_2.getState());
		assertEquals(BucketObjectState.REMOVING, actual2_1.getState());
		assertEquals(BucketObjectState.REMOVING, actual2_1_1.getState());
		assertEquals(BucketObjectState.REMOVING, actual2_1_2.getState());
		
		ObjectStatistics expectedBucket1Statistics = new ObjectStatistics();
		expectedBucket1Statistics.setObjectCount(1L);
		expectedBucket1Statistics.setObjectBytes(10L);
		expectedBucket1Statistics.setObjectVersionCount(1L);
		expectedBucket1Statistics.setObjectVersionBytes(10L);
		expectedBucket1Statistics.setPendingObjectCount(2L);
		expectedBucket1Statistics.setPendingObjectBytes(20L);
		expectedBucket1Statistics.setPendingObjectVersionCount(2L);
		expectedBucket1Statistics.setPendingObjectVersionBytes(20L);
		assertEquals(expectedBucket1Statistics, actualBucket1.getStatistics());

		ObjectStatistics expectedBucket2Statistics = new ObjectStatistics();
		expectedBucket2Statistics.setObjectCount(0L);
		expectedBucket2Statistics.setObjectBytes(0L);
		expectedBucket2Statistics.setObjectVersionCount(0L);
		expectedBucket2Statistics.setObjectVersionBytes(0L);
		expectedBucket2Statistics.setPendingObjectCount(1L);
		expectedBucket2Statistics.setPendingObjectBytes(10L);
		expectedBucket2Statistics.setPendingObjectVersionCount(2L);
		expectedBucket2Statistics.setPendingObjectVersionBytes(20L);
		assertEquals(expectedBucket2Statistics, actualBucket2.getStatistics());
		
		ObjectStatistics expectedStorageStatistics = expectedBucket1Statistics.plus(expectedBucket2Statistics);
		assertEquals(expectedStorageStatistics, actualStorage.getStatistics());
	}
	
	/**************************
	 * deleteVersion(BucketObjectVersionId)
	 **************************/
	
	@Test
	public void testDeleteVersion_whenSuccessful() throws Exception {
		// Given
		Bucket bucket = dataService.persist(createBucket());
		BucketObject bucketObject1_1 = createBucketObject(bucket, "test");
		bucketObject1_1.getContentVersion().setContentLength(10L);
		bucketObjectService.put(bucketObject1_1);
		BucketObject bucketObject1_2 = createBucketObject(bucket, "test");
		bucketObject1_2.getContentVersion().setContentLength(20L);
		bucketObjectService.put(bucketObject1_2);

		// When
		bucketObjectService.deleteVersion(bucketObject1_1.getContentVersion().getId());
		em.clear();
		
		// Then
		BucketObject actualObject = em.find(BucketObject.class, bucketObject1_2.getId());
		BucketObjectVersion actualVersion1_1 = em.find(BucketObjectVersion.class, bucketObject1_1.getContentVersion().getId());
		BucketObjectVersion actualVersion1_2 = em.find(BucketObjectVersion.class, bucketObject1_2.getContentVersion().getId());
		Bucket actualBucket = em.find(Bucket.class, bucket.getId());
		Storage actualStorage = em.find(Storage.class, bucket.getStorage().getId());
		
		assertEquals(BucketObjectState.CREATED, actualObject.getState());
		assertEquals(BucketObjectState.REMOVING, actualVersion1_1.getState());
		assertEquals(BucketObjectState.CREATED, actualVersion1_2.getState());
		
		ObjectStatistics expectedStatistics = new ObjectStatistics();
		expectedStatistics.setObjectCount(1L);
		expectedStatistics.setObjectBytes(20L);
		expectedStatistics.setObjectVersionCount(1L);
		expectedStatistics.setObjectVersionBytes(20L);
		expectedStatistics.setPendingObjectCount(1L);
		expectedStatistics.setPendingObjectBytes(20L);
		expectedStatistics.setPendingObjectVersionCount(2L);
		expectedStatistics.setPendingObjectVersionBytes(30L);
		assertEquals(expectedStatistics, actualBucket.getStatistics());
		assertEquals(expectedStatistics, actualStorage.getStatistics());
	}
	
	@Test
	public void testDeleteVersion_whenNotExists() throws Exception {
		// Given
		Bucket bucket = dataService.persist(createBucket());
		BucketObject bucketObject = createBucketObject(bucket, "test");
		bucketObject.getContentVersion().setContentLength(10L);
		bucketObjectService.put(bucketObject);

		// When
		Assert.verifyException(bucketObjectService, BucketObjectNotFoundException.class).deleteVersion(new BucketObjectVersionId(bucket.getId(), "non-existing", "123"));
		
		// Then
		BucketObject actual = em.find(BucketObject.class, bucketObject.getId());
		Bucket actualBucket = em.find(Bucket.class, bucket.getId());
		Storage actualStorage = em.find(Storage.class, bucket.getStorage().getId());

		assertEquals(bucketObject.getContentVersionUuid(), actual.getContentVersionUuid());
		assertEquals(BucketObjectState.CREATED, actual.getState());
		
		ObjectStatistics expectedStatistics = new ObjectStatistics();
		expectedStatistics.setObjectCount(1L);
		expectedStatistics.setObjectBytes(10L);
		expectedStatistics.setObjectVersionCount(1L);
		expectedStatistics.setObjectVersionBytes(10L);
		expectedStatistics.setPendingObjectCount(1L);
		expectedStatistics.setPendingObjectBytes(10L);
		expectedStatistics.setPendingObjectVersionCount(1L);
		expectedStatistics.setPendingObjectVersionBytes(10L);
		assertEquals(expectedStatistics, actualBucket.getStatistics());
		assertEquals(expectedStatistics, actualStorage.getStatistics());
	}
	
	@Test
	public void testDeleteVersion_whenCurrentVersion() throws Exception {
		// Given
		Bucket bucket = dataService.persist(createBucket());
		BucketObject bucketObject = createBucketObject(bucket, "test");
		bucketObject.getContentVersion().setContentLength(10L);
		bucketObjectService.put(bucketObject);

		// When
		Assert.verifyException(bucketObjectService, StorageException.class).deleteVersion(bucketObject.getContentVersion().getId());
		
		// Then
		BucketObject actual = em.find(BucketObject.class, bucketObject.getId());
		Bucket actualBucket = em.find(Bucket.class, bucket.getId());
		Storage actualStorage = em.find(Storage.class, bucket.getStorage().getId());

		assertEquals(bucketObject.getContentVersionUuid(), actual.getContentVersionUuid());
		assertEquals(BucketObjectState.CREATED, actual.getState());
		
		ObjectStatistics expectedStatistics = new ObjectStatistics();
		expectedStatistics.setObjectCount(1L);
		expectedStatistics.setObjectBytes(10L);
		expectedStatistics.setObjectVersionCount(1L);
		expectedStatistics.setObjectVersionBytes(10L);
		expectedStatistics.setPendingObjectCount(1L);
		expectedStatistics.setPendingObjectBytes(10L);
		expectedStatistics.setPendingObjectVersionCount(1L);
		expectedStatistics.setPendingObjectVersionBytes(10L);
		assertEquals(expectedStatistics, actualBucket.getStatistics());
		assertEquals(expectedStatistics, actualStorage.getStatistics());
	}
	
	/**************************
	 * deleteVersions(List<BucketObjectVersionId>)
	 **************************/
	
	@Test
	public void testDeleteVersionsMultiple_mixedExistsMultipleBuckets() throws Exception {
		// Given
		Bucket bucket1 = dataService.persist(createBucket("bucket1"));
		BucketObject bucketObject1_1_1 = createBucketObject(bucket1, "test1");
		bucketObject1_1_1.getContentVersion().setContentLength(10L);
		bucketObjectService.put(bucketObject1_1_1);
		BucketObject bucketObject1_1_2 = createBucketObject(bucket1, "test1");
		bucketObject1_1_2.getContentVersion().setContentLength(30L);
		bucketObjectService.put(bucketObject1_1_2);
		
		Bucket bucket2 = dataService.persist(createBucket("bucket2"));
		BucketObject bucketObject2_1_1 = createBucketObject(bucket2, "test1");
		bucketObject2_1_1.getContentVersion().setContentLength(10L);
		bucketObjectService.put(bucketObject2_1_1);
		BucketObject bucketObject2_1_2 = createBucketObject(bucket2, "test1");
		bucketObject2_1_2.getContentVersion().setContentLength(40L);
		bucketObjectService.put(bucketObject2_1_2);

		// When
		BucketObjectDeleteReport report = bucketObjectService.deleteVersions(Arrays.asList(bucketObject1_1_1.getContentVersion().getId(), bucketObject2_1_1.getContentVersion().getId(), new BucketObjectVersionId(bucket2.getId(), "non-existing", "123")));
		
		// Then
		assertEquals(3, report.getItems().size());
		assertTrue(report.getItems().remove(BucketObjectDeleteReportItem.deleted(bucketObject1_1_1.getId(), bucketObject1_1_1.getContentVersionUuid())));
		assertTrue(report.getItems().remove(BucketObjectDeleteReportItem.deleted(bucketObject2_1_1.getId(), bucketObject2_1_1.getContentVersionUuid())));
		assertTrue(report.getItems().remove(BucketObjectDeleteReportItem.error(new BucketObjectId(bucket2.getId(), "non-existing"), "123", "BucketObjectVersionNotFound", "not found")));
		
		BucketObject actual1_1 = em.find(BucketObject.class, bucketObject1_1_1.getId());
		BucketObject actual2_1 = em.find(BucketObject.class, bucketObject2_1_2.getId());
		BucketObjectVersion actual1_1_1 = em.find(BucketObjectVersion.class, bucketObject1_1_1.getContentVersion().getId());
		BucketObjectVersion actual1_1_2 = em.find(BucketObjectVersion.class, bucketObject1_1_2.getContentVersion().getId());
		BucketObjectVersion actual2_1_1 = em.find(BucketObjectVersion.class, bucketObject2_1_1.getContentVersion().getId());
		BucketObjectVersion actual2_1_2 = em.find(BucketObjectVersion.class, bucketObject2_1_2.getContentVersion().getId());
		Bucket actualBucket1 = em.find(Bucket.class, bucket1.getId());
		Bucket actualBucket2 = em.find(Bucket.class, bucket2.getId());
		Storage actualStorage = em.find(Storage.class, defaultStorage.getId());

		assertEquals(BucketObjectState.CREATED, actual1_1.getState());
		assertEquals(BucketObjectState.REMOVING, actual1_1_1.getState());
		assertEquals(BucketObjectState.CREATED, actual1_1_2.getState());
		assertEquals(BucketObjectState.CREATED, actual2_1.getState());
		assertEquals(BucketObjectState.REMOVING, actual2_1_1.getState());
		assertEquals(BucketObjectState.CREATED, actual2_1_2.getState());
		
		ObjectStatistics expectedBucket1Statistics = new ObjectStatistics();
		expectedBucket1Statistics.setObjectCount(1L);
		expectedBucket1Statistics.setObjectBytes(30L);
		expectedBucket1Statistics.setObjectVersionCount(1L);
		expectedBucket1Statistics.setObjectVersionBytes(30L);
		expectedBucket1Statistics.setPendingObjectCount(1L);
		expectedBucket1Statistics.setPendingObjectBytes(30L);
		expectedBucket1Statistics.setPendingObjectVersionCount(2L);
		expectedBucket1Statistics.setPendingObjectVersionBytes(40L);
		assertEquals(expectedBucket1Statistics, actualBucket1.getStatistics());

		ObjectStatistics expectedBucket2Statistics = new ObjectStatistics();
		expectedBucket2Statistics.setObjectCount(1L);
		expectedBucket2Statistics.setObjectBytes(40L);
		expectedBucket2Statistics.setObjectVersionCount(1L);
		expectedBucket2Statistics.setObjectVersionBytes(40L);
		expectedBucket2Statistics.setPendingObjectCount(1L);
		expectedBucket2Statistics.setPendingObjectBytes(40L);
		expectedBucket2Statistics.setPendingObjectVersionCount(2L);
		expectedBucket2Statistics.setPendingObjectVersionBytes(50L);
		assertEquals(expectedBucket2Statistics, actualBucket2.getStatistics());
		
		ObjectStatistics expectedStorageStatistics = expectedBucket1Statistics.plus(expectedBucket2Statistics);
		assertEquals(expectedStorageStatistics, actualStorage.getStatistics());
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
	
	private InputStream createTempFile(String content) {
		Path basePath = createBasePath();
		try {
			return Files.newInputStream(basePath.resolve(createFile(basePath, content)), StandardOpenOption.READ);
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
	
	private Bucket createBucket() {
		return createBucket(UUID.randomUUID().toString());
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
