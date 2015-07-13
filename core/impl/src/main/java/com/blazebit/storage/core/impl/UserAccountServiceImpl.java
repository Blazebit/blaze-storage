package com.blazebit.storage.core.impl;

import javax.ejb.Stateless;

import com.blazebit.storage.core.api.UserAccountService;
import com.blazebit.storage.core.model.jpa.UserAccount;

@Stateless
public class UserAccountServiceImpl extends AbstractService implements UserAccountService {

	@Override
	public void createUserAccount(UserAccount userAccount) {
		em.persist(userAccount);
	}

	@Override
	public void deleteUserAccount(long userAccountId) {
		throw new UnsupportedOperationException("Deletion of user accounts not yet supported!");
	}

}
