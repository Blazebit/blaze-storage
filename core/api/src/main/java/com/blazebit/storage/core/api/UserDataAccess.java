package com.blazebit.storage.core.api;

import java.util.List;

import com.blazebit.persistence.QueryBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.model.jpa.User;

public interface UserDataAccess {

	public <T> List<T> findAll(EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting);

	public User findById(String userId);
}
