package com.blazebit.storage.core.api;

import com.blazebit.storage.core.model.jpa.UserAccount;

public interface UserAccountService {

	public void createUserAccount(UserAccount userAccount);

	public void deleteUserAccount(long userAccountId);
}
