package com.blazebit.storage.testsuite.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.StorageDataAccess;
import com.blazebit.storage.core.model.jpa.Account;
import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.jpa.StorageId;
import com.blazebit.storage.core.model.jpa.StorageQuotaModel;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlan;
import com.blazebit.storage.testsuite.common.AbstractContainerTest;
import com.blazebit.storage.testsuite.common.DatabaseAware;
import com.blazebit.storage.testsuite.common.data.AccountTestData;
import com.blazebit.storage.testsuite.common.data.QuotaPlanTestData;
import com.blazebit.storage.testsuite.common.data.StorageTestData;
import com.blazebit.storage.testsuite.common.persistence.PersistenceUnits;
import com.blazebit.storage.testsuite.core.common.view.StorageIdHolderView;

@DatabaseAware(unitName = PersistenceUnits.STORAGE_TEST_MASTER_ONLY)
public class StorageDataAccessTest extends AbstractContainerTest {

	@Inject
	private StorageDataAccess storageDataAccess;

	private StorageQuotaModel defaultQuotaModel;
	private StorageQuotaPlan defaultQuotaPlan;
	
	private Account defaultOwner;
	
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
	}
	
	/**************************
	 * findAllByAccountId(long, EntityViewSetting)
	 **************************/

	@Test
	public void testFindAllByAccountId_whenUnknown() throws Exception {
		// Given
		EntityViewSetting<StorageIdHolderView, CriteriaBuilder<StorageIdHolderView>> setting = EntityViewSetting.create(StorageIdHolderView.class);

		// When
		List<StorageIdHolderView> list = storageDataAccess.findAllByAccountId(-1L, setting);
		
		// Then
		assertEquals(0, list.size());
	}

	@Test
	public void testFindAllByAccountId_whenHasOne() throws Exception {
		// Given
		EntityViewSetting<StorageIdHolderView, CriteriaBuilder<StorageIdHolderView>> setting = EntityViewSetting.create(StorageIdHolderView.class);
		Storage storage = createStorage();
		dataService.persist(storage);

		// When
		List<StorageIdHolderView> list = storageDataAccess.findAllByAccountId(defaultOwner.getId(), setting);
		
		// Then
		assertEquals(1, list.size());
		assertEquals(storage.getId(), list.get(0).getId());
	}
	
	/**************************
	 * findById(StorageId)
	 **************************/

	@Test
	public void testFindById_whenNull() throws Exception {
		// Given
		StorageId id = null;

		// When
		Storage actual = storageDataAccess.findById(id);
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testFindById_whenUnknown() throws Exception {
		// Given
		StorageId id = new StorageId(defaultOwner.getId(), "not-existing");

		// When
		Storage actual = storageDataAccess.findById(id);
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testFindById_whenHasOne() throws Exception {
		// Given
		Storage storage = createStorage();
		dataService.persist(storage);

		// When
		Storage actual = storageDataAccess.findById(storage.getId());
		
		// Then
		assertNotNull(actual);
		assertEquals(storage.getId(), actual.getId());
	}
	
	/**************************
	 * findByBucketId(String)
	 **************************/

	@Test
	public void testFindByBucketId_whenNull() throws Exception {
		// Given
		String bucketId = null;
		Storage storage = createStorage();
		dataService.persist(storage);

		// When
		Storage actual = storageDataAccess.findByBucketId(bucketId);
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testFindByBucketId_whenUnknown() throws Exception {
		// Given
		String bucketId = "not-existing";
		Storage storage = createStorage();
		dataService.persist(storage);

		// When
		Storage actual = storageDataAccess.findByBucketId(bucketId);
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testFindByBucketId_whenHasOne() throws Exception {
		// Given
		String bucketId = "test";
		Storage storage = createStorage();
		dataService.persist(storage);
		Bucket bucket = createBucket(bucketId, defaultOwner, storage);
		dataService.persist(bucket);

		// When
		Storage actual = storageDataAccess.findByBucketId(bucketId);
		
		// Then
		assertNotNull(actual);
		assertEquals(storage.getId(), actual.getId());
	}
	
	/**************************
	 * findById(StorageId, EntityViewSetting)
	 **************************/

	@Test
	public void testFindByIdWithSetting_whenNull() throws Exception {
		// Given
		EntityViewSetting<StorageIdHolderView, CriteriaBuilder<StorageIdHolderView>> setting = EntityViewSetting.create(StorageIdHolderView.class);
		StorageId id = null;

		// When
		StorageIdHolderView actual = storageDataAccess.findById(id, setting);
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testFindByIdWithSetting_whenEmpty() throws Exception {
		// Given
		EntityViewSetting<StorageIdHolderView, CriteriaBuilder<StorageIdHolderView>> setting = EntityViewSetting.create(StorageIdHolderView.class);
		StorageId id = new StorageId(defaultOwner.getId(), "not-existing");

		// When
		StorageIdHolderView actual = storageDataAccess.findById(id, setting);
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testFindByIdWithSetting_whenHasOne() throws Exception {
		// Given
		EntityViewSetting<StorageIdHolderView, CriteriaBuilder<StorageIdHolderView>> setting = EntityViewSetting.create(StorageIdHolderView.class);
		Storage storage = createStorage();
		dataService.persist(storage);

		// When
		StorageIdHolderView actual = storageDataAccess.findById(storage.getId(), setting);
		
		// Then
		assertNotNull(actual);
		assertEquals(storage.getId(), actual.getId());
	}
	
	/*************************************
	 * Private methods
	 ************************************/
	
	private Storage createStorage() {
		return StorageTestData.createStorage(defaultOwner, defaultQuotaPlan);
	}
	
	private Bucket createBucket(String id, Account owner, Storage storage) {
		Bucket bucket = new Bucket();
		bucket.setId(id);
		bucket.setOwner(owner);
		bucket.setStorage(storage);
		return bucket;
	}
}
