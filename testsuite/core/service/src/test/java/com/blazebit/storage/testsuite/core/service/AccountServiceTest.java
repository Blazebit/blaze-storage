package com.blazebit.storage.testsuite.core.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

import com.blazebit.storage.core.api.AccountService;
import com.blazebit.storage.core.model.jpa.Account;
import com.blazebit.storage.testsuite.common.AbstractContainerTest;
import com.blazebit.storage.testsuite.common.Assert;
import com.blazebit.storage.testsuite.common.DatabaseAware;
import com.blazebit.storage.testsuite.common.data.AccountTestData;
import com.blazebit.storage.testsuite.common.persistence.PersistenceUnits;

@DatabaseAware(unitName = PersistenceUnits.STORAGE_TEST_MASTER_ONLY)
public class AccountServiceTest extends AbstractContainerTest {

	@Inject
	private AccountService accountService;
	
	@Deployment
	public static Archive<?> createDeployment() {
		WebArchive archive = createBaseDeployment();
		return archive;
	}
	
	/**************************
	 * create(Account)
	 **************************/

	@Test
	public void testCreate_whenNew() throws Exception {
		// Given
		Account account = createAccount();

		// When
		accountService.create(account);
		Account actual = em.find(Account.class, account.getId());
		
		// Then
		assertNotNull(actual);
		assertEquals(account.getId(), actual.getId());
	}

	@Test
	public void testCreate_whenExisting() throws Exception {
		// Given
		Account account = createAccount();
		account.setKey("test");
		dataService.persist(account);

		// When
		Account newAccount = createAccount();
		newAccount.setKey("test");
		Assert.verifyException(accountService, PersistenceException.class).create(newAccount);
	}
	
	// TODO: some more tests for account create and update

	
	/*************************************
	 * Private methods
	 ************************************/

	private Account createAccount() {
		return AccountTestData.createAccount();
	}
}
