package com.blazebit.storage.testsuite.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.BucketDataAccess;
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
import com.blazebit.storage.testsuite.common.DatabaseAware;
import com.blazebit.storage.testsuite.common.data.AccountTestData;
import com.blazebit.storage.testsuite.common.data.QuotaPlanTestData;
import com.blazebit.storage.testsuite.common.data.StorageTestData;
import com.blazebit.storage.testsuite.common.persistence.PersistenceUnits;
import com.blazebit.storage.testsuite.core.common.view.BucketContentView;
import com.blazebit.storage.testsuite.core.common.view.BucketView;

@DatabaseAware(unitName = PersistenceUnits.STORAGE_TEST_MASTER_ONLY)
public class BucketDataAccessTest extends AbstractContainerTest {

	@Inject
	private BucketDataAccess bucketDataAccess;

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
	 * findByName(String)
	 **************************/

	@Test
	public void testFindByName_whenUnkown() throws Exception {
		// Given
		String name = "";

		// When
		Bucket actual = bucketDataAccess.findByName(name);
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testFindByName_whenHasOne() throws Exception {
		// Given
		Bucket bucket = createBucket();
		dataService.persist(bucket);

		// When
		Bucket actual = bucketDataAccess.findByName(bucket.getId());
		
		// Then
		assertNotNull(actual);
		assertEquals(bucket.getId(), actual.getId());
	}

	@Test
	public void testFindByName_whenOnlyDeleted() throws Exception {
		// Given
		Bucket bucket = createBucket();
		bucket.setDeleted(true);
		dataService.persist(bucket);

		// When
		Bucket actual = bucketDataAccess.findByName(bucket.getId());
		
		// Then
		assertNull(actual);
	}
	
	/**************************
	 * findByName(String, EntityViewSetting)
	 **************************/

	@Test
	public void testFindByNameWithSetting_whenUnkown() throws Exception {
		// Given
		EntityViewSetting<BucketView, CriteriaBuilder<BucketView>> setting = EntityViewSetting.create(BucketView.class);
		String name = "";

		// When
		BucketView actual = bucketDataAccess.findByName(name, setting);
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testFindByNameWithSetting_whenHasOne() throws Exception {
		// Given
		EntityViewSetting<BucketView, CriteriaBuilder<BucketView>> setting = EntityViewSetting.create(BucketView.class);
		Bucket bucket = createBucket();
		dataService.persist(bucket);

		// When
		BucketView actual = bucketDataAccess.findByName(bucket.getId(), setting);
		
		// Then
		assertNotNull(actual);
		assertEquals(bucket.getId(), actual.getId());
	}

	@Test
	public void testFindByNameWithSetting_whenOnlyDeleted() throws Exception {
		// Given
		EntityViewSetting<BucketView, CriteriaBuilder<BucketView>> setting = EntityViewSetting.create(BucketView.class);
		Bucket bucket = createBucket();
		bucket.setDeleted(true);
		dataService.persist(bucket);

		// When
		BucketView actual = bucketDataAccess.findByName(bucket.getId(), setting);
		
		// Then
		assertNull(actual);
	}
	
	/**************************
	 * findByName(String, String, Integer, String, EntityViewSetting)
	 **************************/

	@Test
	public void testFindByNameForContentWithSetting_whenUnkown() throws Exception {
		// Given
		EntityViewSetting<BucketView, CriteriaBuilder<BucketView>> setting = EntityViewSetting.create(BucketView.class);
		String name = "";

		// When
		BucketView actual = bucketDataAccess.findByName(name, null, null, null, setting);
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testFindByNameForContentWithSetting_whenPrefixMatches() throws Exception {
		// Given
		EntityViewSetting<BucketContentView, CriteriaBuilder<BucketContentView>> setting = EntityViewSetting.create(BucketContentView.class);
		Bucket bucket = createBucket();
		dataService.persist(bucket);
		BucketObject object1 = persist(createBucketObject(bucket, "test/obj1"));
		BucketObject object2 = persist(createBucketObject(bucket, "test/obj2"));
		BucketObject object3 = persist(createBucketObject(bucket, "test2/obj3"));

		// When
		BucketContentView actual = bucketDataAccess.findByName(bucket.getId(), "test/", null, null, setting);
		
		// Then
		assertNotNull(actual);
		assertEquals(2, actual.getObjects().size());
		assertEquals(object1.getId(), actual.getObjects().get(0).getId());
		assertEquals(object2.getId(), actual.getObjects().get(1).getId());
	}

	@Test
	public void testFindByNameForContentWithSetting_whenLimitReached() throws Exception {
		// Given
		EntityViewSetting<BucketContentView, CriteriaBuilder<BucketContentView>> setting = EntityViewSetting.create(BucketContentView.class);
		Bucket bucket = createBucket();
		dataService.persist(bucket);
		BucketObject object1 = persist(createBucketObject(bucket, "test/obj1"));
		BucketObject object2 = persist(createBucketObject(bucket, "test/obj2"));
		BucketObject object3 = persist(createBucketObject(bucket, "test/obj3"));
		BucketObject object4 = persist(createBucketObject(bucket, "test/obj4"));

		// When 1
		BucketContentView actual = bucketDataAccess.findByName(bucket.getId(), null, 2, null, setting);
		
		// Then 1
		assertNotNull(actual);
		assertEquals(2, actual.getObjects().size());
		assertEquals(object1.getId(), actual.getObjects().get(0).getId());
		assertEquals(object2.getId(), actual.getObjects().get(1).getId());
		
		// When 2
		actual = bucketDataAccess.findByName(bucket.getId(), null, 1, actual.getObjects().get(1).getId().getName(), setting);
		
		// Then 2
		assertNotNull(actual);
		assertEquals(1, actual.getObjects().size());
		assertEquals(object3.getId(), actual.getObjects().get(0).getId());
	}
	
	/**************************
	 * findByAccountId(long, EntityViewSetting)
	 **************************/

	@Test
	public void testFindByAccountId_whenUnkown() throws Exception {
		// Given
		EntityViewSetting<BucketView, CriteriaBuilder<BucketView>> setting = EntityViewSetting.create(BucketView.class);
		long accountId = -1;

		// When
		List<BucketView> actual = bucketDataAccess.findByAccountId(accountId, setting);
		
		// Then
		assertEquals(0, actual.size());
	}

	@Test
	public void testFindByAccountId_whenHasOne() throws Exception {
		// Given
		EntityViewSetting<BucketView, CriteriaBuilder<BucketView>> setting = EntityViewSetting.create(BucketView.class);
		Bucket bucket = createBucket();
		dataService.persist(bucket);

		// When
		List<BucketView> actual = bucketDataAccess.findByAccountId(defaultOwner.getId(), setting);
		
		// Then
		assertEquals(1, actual.size());
		assertEquals(bucket.getId(), actual.get(0).getId());
	}

	@Test
	public void testFindByAccountId_whenOnlyDeleted() throws Exception {
		// Given
		EntityViewSetting<BucketView, CriteriaBuilder<BucketView>> setting = EntityViewSetting.create(BucketView.class);
		Bucket bucket = createBucket();
		bucket.setDeleted(true);
		dataService.persist(bucket);

		// When
		List<BucketView> actual = bucketDataAccess.findByAccountId(defaultOwner.getId(), setting);
		
		// Then
		assertEquals(0, actual.size());
	}

	@Test
	public void testFindByAccountId_whenMultipleAccountSomeDeleted() throws Exception {
		// Given
		EntityViewSetting<BucketView, CriteriaBuilder<BucketView>> setting = EntityViewSetting.create(BucketView.class);
		Bucket bucket11 = createBucket();
		bucket11.setDeleted(true);
		dataService.persist(bucket11);
		
		Bucket bucket12 = createBucket();
		bucket12.setDeleted(false);
		dataService.persist(bucket12);

		
		Account owner2 = AccountTestData.createAccount();
		dataService.persist(owner2);
		
		Storage storage2 = StorageTestData.createStorage(owner2, defaultQuotaPlan);
		dataService.persist(storage2);
		
		Bucket bucket21 = createBucket(owner2, storage2);
		bucket21.setDeleted(true);
		dataService.persist(bucket21);
		
		Bucket bucket22 = createBucket(owner2, storage2);
		bucket22.setDeleted(false);
		dataService.persist(bucket22);

		// When
		List<BucketView> actual = bucketDataAccess.findByAccountId(defaultOwner.getId(), setting);
		
		// Then
		assertEquals(1, actual.size());
		assertEquals(bucket12.getId(), actual.get(0).getId());
	}
	
	/**************************
	 * findByAccountIdAndStorageName(long, String, EntityViewSetting)
	 **************************/

	@Test
	public void testFindByAccountIdAndStorage_whenHasOneNotDeleted() throws Exception {
		// Given
		EntityViewSetting<BucketView, CriteriaBuilder<BucketView>> setting = EntityViewSetting.create(BucketView.class);
		Bucket bucket11 = createBucket();
		bucket11.setDeleted(true);
		dataService.persist(bucket11);
		
		Bucket bucket12 = createBucket();
		bucket12.setDeleted(false);
		dataService.persist(bucket12);

		// When
		List<BucketView> actual = bucketDataAccess.findByAccountIdAndStorageName(defaultOwner.getId(), defaultStorage.getId().getName(), setting);
		
		// Then
		assertEquals(1, actual.size());
		assertEquals(bucket12.getId(), actual.get(0).getId());
	}
	
	/**************************
	 * findAll(EntityViewSetting)
	 **************************/

	@Test
	public void testFindAll_whenHasOneNotDeleted() throws Exception {
		// Given
		EntityViewSetting<BucketView, CriteriaBuilder<BucketView>> setting = EntityViewSetting.create(BucketView.class);
		Bucket bucket11 = createBucket();
		bucket11.setDeleted(true);
		dataService.persist(bucket11);
		
		Bucket bucket12 = createBucket();
		bucket12.setDeleted(false);
		dataService.persist(bucket12);

		// When
		List<BucketView> actual = bucketDataAccess.findAll(setting);
		
		// Then
		assertEquals(1, actual.size());
		assertEquals(bucket12.getId(), actual.get(0).getId());
	}
	
	/*************************************
	 * Private methods
	 ************************************/
	
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
