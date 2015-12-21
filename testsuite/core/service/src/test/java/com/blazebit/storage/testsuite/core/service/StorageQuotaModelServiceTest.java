package com.blazebit.storage.testsuite.core.service;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

import com.blazebit.storage.core.api.StorageQuotaModelService;
import com.blazebit.storage.core.model.jpa.StorageQuotaModel;
import com.blazebit.storage.testsuite.common.AbstractContainerTest;
import com.blazebit.storage.testsuite.common.Assert;
import com.blazebit.storage.testsuite.common.DatabaseAware;
import com.blazebit.storage.testsuite.common.data.QuotaPlanTestData;
import com.blazebit.storage.testsuite.common.persistence.PersistenceUnits;

@DatabaseAware(unitName = PersistenceUnits.STORAGE_TEST_MASTER_ONLY)
public class StorageQuotaModelServiceTest extends AbstractContainerTest {

	@Inject
	private StorageQuotaModelService storageQuotaModelService;
	
	@Deployment
	public static Archive<?> createDeployment() {
		WebArchive archive = createBaseDeployment();
		return archive;
	}
	
	/**************************
	 * create(StorageQuotaModel)
	 **************************/
	
	@Test
	public void testCreate_whenSuccessful() throws Exception {
		// Given
		StorageQuotaModel quotaModel = QuotaPlanTestData.createModel("my-model-1");
		quotaModel.getPlans().add(QuotaPlanTestData.createPlan(quotaModel, 1));
		quotaModel.getPlans().add(QuotaPlanTestData.createPlan(quotaModel, 2));

		// When
		storageQuotaModelService.create(quotaModel);
		
		// Then
		StorageQuotaModel actualQuotaModel = cbf.create(em, StorageQuotaModel.class).fetch("plans").where("id").eq(quotaModel.getId()).getSingleResult();
		
		assertEquals(quotaModel.getId(), actualQuotaModel.getId());
		assertEquals(2, actualQuotaModel.getPlans().size());
	}
	
	@Test
	public void testCreate_whenAlreadyExists() throws Exception {
		// Given
		StorageQuotaModel quotaModel = QuotaPlanTestData.createModel("my-model-1");
		quotaModel.getPlans().add(QuotaPlanTestData.createPlan(quotaModel, 1));
		quotaModel.getPlans().add(QuotaPlanTestData.createPlan(quotaModel, 2));
		dataService.persist(quotaModel);

		quotaModel = QuotaPlanTestData.createModel("my-model-1");
		quotaModel.getPlans().add(QuotaPlanTestData.createPlan(quotaModel, 10));

		// When
		Assert.verifyException(storageQuotaModelService, PersistenceException.class).create(quotaModel);
		
		// Then
		StorageQuotaModel actualQuotaModel = cbf.create(em, StorageQuotaModel.class).fetch("plans").where("id").eq(quotaModel.getId()).getSingleResult();
		
		assertEquals(quotaModel.getId(), actualQuotaModel.getId());
		assertEquals(2, actualQuotaModel.getPlans().size());
	}
	
	/**************************
	 * update(StorageQuotaModel)
	 **************************/
	
	@Test
	public void testUpdate_whenNotExists() throws Exception {
		// Given
		StorageQuotaModel quotaModel = QuotaPlanTestData.createModel("my-model-1");

		// When
		storageQuotaModelService.update(quotaModel);
		
		// Then
		StorageQuotaModel actualQuotaModel = cbf.create(em, StorageQuotaModel.class).fetch("plans").where("id").eq(quotaModel.getId()).getSingleResult();
		
		assertEquals(quotaModel.getId(), actualQuotaModel.getId());
		assertEquals(0, actualQuotaModel.getPlans().size());
	}
	
	@Test
	public void testUpdate_whenExisting() throws Exception {
		// Given
		StorageQuotaModel quotaModel = QuotaPlanTestData.createModel("my-model-1");
		quotaModel.getPlans().add(QuotaPlanTestData.createPlan(quotaModel, 1));
		quotaModel.getPlans().add(QuotaPlanTestData.createPlan(quotaModel, 2));
		dataService.persist(quotaModel);

		quotaModel = QuotaPlanTestData.createModel("my-model-1");
		quotaModel.getPlans().add(QuotaPlanTestData.createPlan(quotaModel, 10));

		// When
		storageQuotaModelService.update(quotaModel);
		
		// Then
		StorageQuotaModel actualQuotaModel = cbf.create(em, StorageQuotaModel.class).fetch("plans").where("id").eq(quotaModel.getId()).getSingleResult();
		
		assertEquals(quotaModel.getId(), actualQuotaModel.getId());
		assertEquals(1, actualQuotaModel.getPlans().size());
	}
}
