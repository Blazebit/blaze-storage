package com.blazebit.storage.core.api;

import java.util.List;

import com.blazebit.persistence.QueryBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.model.jpa.UserAccount;

public interface UserAccountDataAccess {

	public <T> List<T> getUserAccounts(EntityViewSetting<T, ? extends QueryBuilder<T,?>> setting);

	public UserAccount getUserAccount(long userAccountId);
	
	public UserAccount getUserAccountByKey(String key);
}
