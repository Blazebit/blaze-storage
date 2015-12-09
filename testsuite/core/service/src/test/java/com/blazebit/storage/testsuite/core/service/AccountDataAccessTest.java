package com.blazebit.storage.testsuite.core.service;

import static org.junit.Assert.*;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.AccountDataAccess;
import com.blazebit.storage.core.model.jpa.Account;
import com.blazebit.storage.testsuite.common.AbstractContainerTest;
import com.blazebit.storage.testsuite.common.DatabaseAware;
import com.blazebit.storage.testsuite.common.data.AccountTestData;
import com.blazebit.storage.testsuite.common.persistence.PersistenceUnits;
import com.blazebit.storage.testsuite.common.view.LongIdHolderView;

@DatabaseAware(unitName = PersistenceUnits.STORAGE_TEST_MASTER_ONLY)
public class AccountDataAccessTest extends AbstractContainerTest {

	@Inject
	private AccountDataAccess accountDataAccess;
	
	@Deployment
	public static Archive<?> createDeployment() {
		WebArchive archive = createBaseDeployment();
		return archive;
	}
	
	/**************************
	 * findAll(EntityViewSetting)
	 **************************/

	@Test
	public void testFindAll_whenEmpty() throws Exception {
		// Given
		EntityViewSetting<LongIdHolderView, CriteriaBuilder<LongIdHolderView>> setting = EntityViewSetting.create(LongIdHolderView.class);

		// When
		List<LongIdHolderView> list = accountDataAccess.findAll(setting);
		
		// Then
		assertEquals(0, list.size());
	}

	@Test
	public void testFindAll_whenHasOne() throws Exception {
		// Given
		EntityViewSetting<LongIdHolderView, CriteriaBuilder<LongIdHolderView>> setting = EntityViewSetting.create(LongIdHolderView.class);
		Account account = createAccount();
		dataService.persist(account);

		// When
		List<LongIdHolderView> list = accountDataAccess.findAll(setting);
		
		// Then
		assertEquals(1, list.size());
		assertEquals(account.getId(), list.get(0).getId());
	}
	
	/**************************
	 * findById(long)
	 **************************/

	@Test
	public void testFindById_whenUnknown() throws Exception {
		// Given
		long id = -1;

		// When
		Account actual = accountDataAccess.findById(id);
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testFindById_whenHasOne() throws Exception {
		// Given
		Account account = createAccount();
		dataService.persist(account);

		// When
		Account actual = accountDataAccess.findById(account.getId());
		
		// Then
		assertNotNull(actual);
		assertEquals(account.getId(), actual.getId());
	}
	
	/**************************
	 * findByKey(String)
	 **************************/

	@Test
	public void testFindByKey_whenNull() throws Exception {
		// Given
		String key = null;

		// When
		Account actual = accountDataAccess.findByKey(key);
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testFindByKey_whenUnknown() throws Exception {
		// Given
		String key = "";

		// When
		Account actual = accountDataAccess.findByKey(key);
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testFindByKey_whenHasOne() throws Exception {
		// Given
		Account account = createAccount();
		dataService.persist(account);

		// When
		Account actual = accountDataAccess.findByKey(account.getKey());
		
		// Then
		assertNotNull(actual);
		assertEquals(account.getId(), actual.getId());
	}
	
	/**************************
	 * findByKey(String, EntityViewSetting)
	 **************************/

	@Test
	public void testFindByKeyWithSetting_whenNull() throws Exception {
		// Given
		EntityViewSetting<LongIdHolderView, CriteriaBuilder<LongIdHolderView>> setting = EntityViewSetting.create(LongIdHolderView.class);
		String key = null;

		// When
		LongIdHolderView actual = accountDataAccess.findByKey(key, setting);
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testFindByKeyWithSetting_whenUnknown() throws Exception {
		// Given
		EntityViewSetting<LongIdHolderView, CriteriaBuilder<LongIdHolderView>> setting = EntityViewSetting.create(LongIdHolderView.class);
		String key = "";

		// When
		LongIdHolderView actual = accountDataAccess.findByKey(key, setting);
		
		// Then
		assertNull(actual);
	}

	@Test
	public void testFindByKeyWithSetting_whenHasOne() throws Exception {
		// Given
		EntityViewSetting<LongIdHolderView, CriteriaBuilder<LongIdHolderView>> setting = EntityViewSetting.create(LongIdHolderView.class);
		Account account = createAccount();
		dataService.persist(account);

		// When
		LongIdHolderView actual = accountDataAccess.findByKey(account.getKey(), setting);
		
		// Then
		assertNotNull(actual);
		assertEquals(account.getId(), actual.getId());
	}
	
	/*************************************
	 * Private methods
	 ************************************/
	
	private Account createAccount() {
		return AccountTestData.createAccount();
	}
}
