package com.blazebit.storage.core.api;

import java.util.List;

import com.blazebit.persistence.QueryBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.model.jpa.Account;

public interface AccountDataAccess {

	public <T> List<T> findAll(EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting);

	public Account findById(long accountId);
	
	public Account findByKey(String key);

	public <T> T findByKey(String key, EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting);
}
