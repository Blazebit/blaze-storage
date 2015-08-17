package com.blazebit.storage.core.impl;

import javax.ejb.Stateless;

import com.blazebit.storage.core.api.AccountService;
import com.blazebit.storage.core.model.jpa.Account;
import com.blazebit.storage.core.model.jpa.User;

@Stateless
public class AccountServiceImpl extends AbstractService implements AccountService {

	@Override
	public void create(Account account) {
		account.setOwner(em.getReference(User.class, account.getOwner().getId()));
		em.persist(account);
	}

	@Override
	public void update(Account account) {
		em.merge(account);
	}

	@Override
	public void delete(long accountId) {
		throw new UnsupportedOperationException("Deletion of accounts not yet supported!");
	}

}
