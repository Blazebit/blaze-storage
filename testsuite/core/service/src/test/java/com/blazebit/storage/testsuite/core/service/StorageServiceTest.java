package com.blazebit.storage.testsuite.core.service;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;

import com.blazebit.storage.core.api.AccountNotFoundException;
import com.blazebit.storage.core.api.StorageQuotaPlanNotFoundException;
import com.blazebit.storage.core.api.StorageService;
import com.blazebit.storage.core.model.jpa.Account;
import com.blazebit.storage.core.model.jpa.ObjectStatistics;
import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.jpa.StorageQuotaModel;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlan;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlanId;
import com.blazebit.storage.testsuite.common.AbstractContainerTest;
import com.blazebit.storage.testsuite.common.Assert;
import com.blazebit.storage.testsuite.common.DatabaseAware;
import com.blazebit.storage.testsuite.common.data.AccountTestData;
import com.blazebit.storage.testsuite.common.data.QuotaPlanTestData;
import com.blazebit.storage.testsuite.common.data.StorageTestData;
import com.blazebit.storage.testsuite.common.persistence.PersistenceUnits;

@DatabaseAware(unitName = PersistenceUnits.STORAGE_TEST_MASTER_ONLY)
public class StorageServiceTest extends AbstractContainerTest {

	@Inject
	private StorageService storageService;

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
	 * put(Bucket)
	 **************************/
	
	@Test
	public void testPut_whenCreating() throws Exception {
		// Given
		Storage storage = StorageTestData.createStorage(defaultOwner, defaultQuotaPlan, "test");

		// When
		storageService.put(storage);
		
		// Then
		Storage actualStorage = em.find(Storage.class, storage.getId());
		
		assertEquals(storage.getQuotaPlan(), actualStorage.getQuotaPlan());
		
		ObjectStatistics expectedStatistics = new ObjectStatistics();
		expectedStatistics.setObjectCount(0L);
		expectedStatistics.setObjectBytes(0L);
		expectedStatistics.setObjectVersionCount(0L);
		expectedStatistics.setObjectVersionBytes(0L);
		expectedStatistics.setPendingObjectCount(0L);
		expectedStatistics.setPendingObjectBytes(0L);
		expectedStatistics.setPendingObjectVersionCount(0L);
		expectedStatistics.setPendingObjectVersionBytes(0L);
		assertEquals(expectedStatistics, actualStorage.getStatistics());
	}
	
	@Test
	public void testPut_whenUpdating() throws Exception {
		// Given
		Storage storage = StorageTestData.createStorage(defaultOwner, defaultQuotaPlan, "test");
		storage = dataService.persist(storage);

		storage.setTags(new HashMap<String, String>());
		storage.getTags().put("test", "test");

		// When
		storageService.put(storage);
		
		// Then
		Storage actualStorage = cbf.create(em, Storage.class).fetch("tags").where("id").eq(storage.getId()).getSingleResult();
		
		assertEquals("test", actualStorage.getTags().get("test"));
		
		ObjectStatistics expectedStatistics = new ObjectStatistics();
		expectedStatistics.setObjectCount(0L);
		expectedStatistics.setObjectBytes(0L);
		expectedStatistics.setObjectVersionCount(0L);
		expectedStatistics.setObjectVersionBytes(0L);
		expectedStatistics.setPendingObjectCount(0L);
		expectedStatistics.setPendingObjectBytes(0L);
		expectedStatistics.setPendingObjectVersionCount(0L);
		expectedStatistics.setPendingObjectVersionBytes(0L);
		assertEquals(expectedStatistics, actualStorage.getStatistics());
	}
	
	@Test
	public void testPut_whenUpdatingIgnoredProperties() throws Exception {
		// Given
		Storage storage = StorageTestData.createStorage(defaultOwner, defaultQuotaPlan, "test");
		storage = dataService.persist(storage);

		Storage newStorage = StorageTestData.createStorage(defaultOwner, defaultQuotaPlan, "test");
		newStorage.setOwner(null);

		// When
		storageService.put(newStorage);
		
		// Then
		Storage actualStorage = em.find(Storage.class, storage.getId());
		
		assertEquals(storage.getOwner(), actualStorage.getOwner());
		
		ObjectStatistics expectedStatistics = new ObjectStatistics();
		expectedStatistics.setObjectCount(0L);
		expectedStatistics.setObjectBytes(0L);
		expectedStatistics.setObjectVersionCount(0L);
		expectedStatistics.setObjectVersionBytes(0L);
		expectedStatistics.setPendingObjectCount(0L);
		expectedStatistics.setPendingObjectBytes(0L);
		expectedStatistics.setPendingObjectVersionCount(0L);
		expectedStatistics.setPendingObjectVersionBytes(0L);
		assertEquals(expectedStatistics, actualStorage.getStatistics());
	}
	
	@Test
	public void testPut_whenCreatingForNonExistingAccount() throws Exception {
		// Given
		Storage storage = StorageTestData.createStorage(new Account(-1L), defaultQuotaPlan, "test");

		// When & Then
		Assert.verifyException(storageService, AccountNotFoundException.class).put(storage);
	}
	
	@Test
	public void testPut_whenCreatingForNonExistingStorageQuotaPlan() throws Exception {
		// Given
		Storage storage = StorageTestData.createStorage(defaultOwner, new StorageQuotaPlan(new StorageQuotaPlanId("not-existing", -1)), "test");

		// When & Then
		Assert.verifyException(storageService, StorageQuotaPlanNotFoundException.class).put(storage);
	}
	
	@Test
	public void testPut_whenUpdatingWithNonExistingStorageQuotaPlan() throws Exception {
		// Given
		Storage storage = StorageTestData.createStorage(defaultOwner, defaultQuotaPlan, "test");
		storage = dataService.persist(storage);
		
		Storage newStorage = StorageTestData.createStorage(defaultOwner, new StorageQuotaPlan(new StorageQuotaPlanId("not-existing", -1)), "test");

		// When & Then
		Assert.verifyException(storageService, StorageQuotaPlanNotFoundException.class).put(newStorage);
	}
}
