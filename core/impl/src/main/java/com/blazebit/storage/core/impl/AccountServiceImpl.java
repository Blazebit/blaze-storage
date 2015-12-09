package com.blazebit.storage.core.impl;

import javax.ejb.Stateless;

import com.blazebit.storage.core.api.AccountService;
import com.blazebit.storage.core.model.jpa.Account;

@Stateless
public class AccountServiceImpl extends AbstractService implements AccountService {

	@Override
	public void create(Account account) {
		em.persist(account);
		em.flush();
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
