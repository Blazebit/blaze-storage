package com.blazebit.storage.core.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.QueryBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.AccountDataAccess;
import com.blazebit.storage.core.model.jpa.Account;

@Stateless
public class AccountDataAccessImpl extends AbstractDataAccess implements AccountDataAccess {
	
	@Override
	public <T> List<T> findAll(EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting) {
		CriteriaBuilder<Account> cb = cbf.create(em, Account.class);
		return evm.applySetting(setting, cb).getResultList();
	}
	
	@Override
	public Account findById(long accountId) {
		try {
			return cbf.create(em, Account.class)
					.where("id").eq(accountId)
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	@Override
	public Account findByKey(String key) {
		try {
			return cbf.create(em, Account.class)
					.where("key").eq(key)
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	@Override
	public <T> T findByKey(String key, EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting) {
		try {
			CriteriaBuilder<Account> cb =  cbf.create(em, Account.class)
					.where("key").eq(key);
			return evm.applySetting(setting, cb).getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

}
