package com.blazebit.storage.core.impl;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import com.blazebit.storage.core.api.UserAccountDataAccess;
import com.blazebit.storage.core.model.jpa.UserAccount;

@Stateless
public class UserAccountDataAccessImpl extends AbstractDataAccess implements UserAccountDataAccess {

	@Override
	public UserAccount getUserAccount(long userAccountId) {
		try {
			return cbf.create(em, UserAccount.class)
					.where("id").eq(userAccountId)
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	@Override
	public UserAccount getUserAccountByKey(String key) {
		try {
			return cbf.create(em, UserAccount.class)
					.where("key").eq(key)
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

}
