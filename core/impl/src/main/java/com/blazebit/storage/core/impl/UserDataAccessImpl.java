package com.blazebit.storage.core.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.QueryBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.UserDataAccess;
import com.blazebit.storage.core.model.jpa.User;

@Stateless
public class UserDataAccessImpl extends AbstractDataAccess implements UserDataAccess {
	
	@Override
	public <T> List<T> findAll(EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting) {
		CriteriaBuilder<User> cb = cbf.create(em, User.class);
		return evm.applySetting(setting, cb).getResultList();
	}
	
	@Override
	public User findById(String userId) {
		try {
			return cbf.create(em, User.class)
					.where("id").eq(userId)
					.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

}
