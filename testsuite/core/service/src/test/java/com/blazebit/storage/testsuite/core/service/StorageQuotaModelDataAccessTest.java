package com.blazebit.storage.testsuite.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.StorageQuotaModelDataAccess;
import com.blazebit.storage.core.model.jpa.StorageQuotaModel;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlan;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlanId;
import com.blazebit.storage.testsuite.common.AbstractContainerTest;
import com.blazebit.storage.testsuite.common.DatabaseAware;
import com.blazebit.storage.testsuite.common.data.QuotaPlanTestData;
import com.blazebit.storage.testsuite.common.persistence.PersistenceUnits;
import com.blazebit.storage.testsuite.core.common.view.StorageQuotaModelIdHolderView;

@DatabaseAware(unitName = PersistenceUnits.STORAGE_TEST_MASTER_ONLY)
public class StorageQuotaModelDataAccessTest extends AbstractContainerTest {

	@Inject
	private StorageQuotaModelDataAccess storageQuotaModelDataAccess;
	
	@Deployment
	public static Archive<?> createDeployment() {
		WebArchive archive = createBaseDeployment();
		return archive;
	}
	
	/**************************
	 * findAll(EntityViewSetting)
	 **************************/

	@Test
	public void testFindAll_whenNone() throws Exception {
		// Given
		EntityViewSetting<StorageQuotaModelIdHolderView, CriteriaBuilder<StorageQuotaModelIdHolderView>> setting = EntityViewSetting.create(StorageQuotaModelIdHolderView.class);

		// When
		List<StorageQuotaModelIdHolderView> list = storageQuotaModelDataAccess.findAll(setting);
		
		// Then
		assertEquals(0, list.size());
	}

	@Test
	public void testFindAll_whenHasOne() throws Exception {
		// Given
		EntityViewSetting<StorageQuotaModelIdHolderView, CriteriaBuilder<StorageQuotaModelIdHolderView>> setting = EntityViewSetting.create(StorageQuotaModelIdHolderView.class);
		StorageQuotaModel quotaModel = QuotaPlanTestData.createModel("my-model");
		dataService.persist(quotaModel);

		// When
		List<StorageQuotaModelIdHolderView> list = storageQuotaModelDataAccess.findAll(setting);
		
		// Then
		assertEquals(1, list.size());
		assertEquals(quotaModel.getId(), list.get(0).getId());
	}
	
	/**************************
	 * findById(String)
	 **************************/

	@Test
	public void testFindById_whenNull() throws Exception {
		// Given
		String id = null;

		// When
		StorageQuotaModel actual = storageQuotaModelDataAccess.findById(id);
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testFindById_whenUnknown() throws Exception {
		// Given
		String id = "not-existing";

		// When
		StorageQuotaModel actual = storageQuotaModelDataAccess.findById(id);
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testFindById_whenHasOne() throws Exception {
		// Given
		StorageQuotaModel storageQuotaModel = QuotaPlanTestData.createModel("model-1");
		dataService.persist(storageQuotaModel);

		// When
		StorageQuotaModel actual = storageQuotaModelDataAccess.findById(storageQuotaModel.getId());
		
		// Then
		assertNotNull(actual);
		assertEquals(storageQuotaModel.getId(), actual.getId());
	}
	
	/**************************
	 * findQuotaPlanById(String)
	 **************************/

	@Test
	public void testFindQuotaPlanById_whenNull() throws Exception {
		// Given
		StorageQuotaPlanId id = null;

		// When
		StorageQuotaPlan actual = storageQuotaModelDataAccess.findQuotaPlanById(id);
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testFindQuotaPlanById_whenUnknown() throws Exception {
		// Given
		StorageQuotaPlanId id = new StorageQuotaPlanId("not-existing", 1);

		// When
		StorageQuotaPlan actual = storageQuotaModelDataAccess.findQuotaPlanById(id);
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testFindQuotaPlanById_whenHasOne() throws Exception {
		// Given
		StorageQuotaModel storageQuotaModel = QuotaPlanTestData.createModel("model-1");
		dataService.persist(storageQuotaModel);
		StorageQuotaPlan storageQuotaPlan = QuotaPlanTestData.createPlan(storageQuotaModel, 1);
		dataService.persist(storageQuotaPlan);

		// When
		StorageQuotaPlan actual = storageQuotaModelDataAccess.findQuotaPlanById(storageQuotaPlan.getId());
		
		// Then
		assertNotNull(actual);
		assertEquals(storageQuotaPlan.getId(), actual.getId());
	}
	
	/**************************
	 * findById(String, EntityViewSetting)
	 **************************/

	@Test
	public void testFindByIdWithSetting_whenNull() throws Exception {
		// Given
		EntityViewSetting<StorageQuotaModelIdHolderView, CriteriaBuilder<StorageQuotaModelIdHolderView>> setting = EntityViewSetting.create(StorageQuotaModelIdHolderView.class);
		String id = null;

		// When
		StorageQuotaModelIdHolderView actual = storageQuotaModelDataAccess.findById(id, setting);
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testFindByIdWithSetting_whenEmpty() throws Exception {
		// Given
		EntityViewSetting<StorageQuotaModelIdHolderView, CriteriaBuilder<StorageQuotaModelIdHolderView>> setting = EntityViewSetting.create(StorageQuotaModelIdHolderView.class);
		String id = "not-existing";

		// When
		StorageQuotaModelIdHolderView actual = storageQuotaModelDataAccess.findById(id, setting);
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testFindByIdWithSetting_whenHasOne() throws Exception {
		// Given
		EntityViewSetting<StorageQuotaModelIdHolderView, CriteriaBuilder<StorageQuotaModelIdHolderView>> setting = EntityViewSetting.create(StorageQuotaModelIdHolderView.class);
		StorageQuotaModel storageQuotaModel = QuotaPlanTestData.createModel("model-1");
		dataService.persist(storageQuotaModel);

		// When
		StorageQuotaModelIdHolderView actual = storageQuotaModelDataAccess.findById(storageQuotaModel.getId(), setting);
		
		// Then
		assertNotNull(actual);
		assertEquals(storageQuotaModel.getId(), actual.getId());
	}
}
