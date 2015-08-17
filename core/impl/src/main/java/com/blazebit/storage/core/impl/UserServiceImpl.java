package com.blazebit.storage.core.impl;

import javax.ejb.Stateless;

import com.blazebit.storage.core.api.UserService;
import com.blazebit.storage.core.model.jpa.User;

@Stateless
public class UserServiceImpl extends AbstractService implements UserService {

	@Override
	public void create(User user) {
		em.persist(user);
	}

	@Override
	public void delete(String userId) {
		throw new UnsupportedOperationException("Deletion of user not yet supported!");
	}

}
