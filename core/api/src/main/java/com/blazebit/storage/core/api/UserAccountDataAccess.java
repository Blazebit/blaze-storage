package com.blazebit.storage.core.api;

import com.blazebit.storage.core.model.jpa.UserAccount;

public interface UserAccountDataAccess {

	public UserAccount getUserAccount(long userAccountId);
	
	public UserAccount getUserAccountByKey(String key);
}
