package com.blazebit.storage.rest.impl.context;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.blazebit.storage.core.api.AccountDataAccess;
import com.blazebit.storage.core.api.AccountService;
import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.context.UserContext;
import com.blazebit.storage.core.model.jpa.Account;
import com.blazebit.storage.core.model.security.Role;
import com.blazebit.storage.modules.authentication.api.RequestAuthenticator;

@ApplicationScoped
public class UserContextProducer {
	
	private static final Logger LOG = Logger.getLogger(UserContextProducer.class.getName());
	
	@Inject
	private RequestAuthenticator requestAuthenticator;
	@Inject
	private AccountDataAccess accountDataAccess;
	@Inject
	private AccountService accountService;

	@Produces
	@RequestScoped
	public UserContext produceUserContext(HttpServletRequest request) {
		Account account = null;
		String accountKey = requestAuthenticator.getAccountKey(request);
		Set<String> accountRoles = requestAuthenticator.getAccountRoles(request, Role.ROLES);
		
		if (accountKey == null && !accountRoles.contains(Role.ADMIN)) {
			throw new RuntimeException("The request could not be associated with an account!");
		}

		account = accountDataAccess.findByKey(accountKey);
		
		if (account == null) {
			account = new Account();
			account.setKey(accountKey);
			
			try {
				accountService.create(account);
				LOG.fine("Created account for key '" + accountKey + "' with id: " + account.getId() + "'");
			} catch (StorageException e) {
				// if parallel requests are processed for the same account, another thread might have already created the account
				Account existingAccount = accountDataAccess.findByKey(accountKey);
				if (existingAccount == null) {
					throw new RuntimeException("Account with key [" + accountKey + "] unexpectedly absent");
				}
				
				account = existingAccount;
			}
		}
			
		return new UserContextImpl(account.getId(), accountRoles, Locale.ENGLISH, Collections.list(request.getLocales()));
	}
}