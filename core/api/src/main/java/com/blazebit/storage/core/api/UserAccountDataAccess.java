package com.blazebit.storage.core.api;

import java.util.List;

import com.blazebit.persistence.QueryBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.model.jpa.UserAccount;

public interface UserAccountDataAccess {

	public <T> List<T> findAll(EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting);

	public UserAccount findById(long userAccountId);
	
	public UserAccount findByKey(String key);
}
