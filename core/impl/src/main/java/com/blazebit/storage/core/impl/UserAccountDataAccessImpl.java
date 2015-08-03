package com.blazebit.storage.core.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.QueryBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.UserAccountDataAccess;
import com.blazebit.storage.core.model.jpa.UserAccount;

@Stateless
public class UserAccountDataAccessImpl extends AbstractDataAccess implements UserAccountDataAccess {
	
	@Override
	public <T> List<T> findAll(EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting) {
		CriteriaBuilder<UserAccount> cb = cbf.create(em, UserAccount.class);
		return evm.applySetting(setting, cb).getResultList();
	}
	
	@Override
	public UserAccount findById(long userAccountId) {
		try {
			return cbf.create(em, UserAccount.class)
					.where("id").eq(userAccountId)
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	@Override
	public UserAccount findByKey(String key) {
		try {
			return cbf.create(em, UserAccount.class)
					.where("key").eq(key)
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

}
