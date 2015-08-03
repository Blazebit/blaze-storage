package com.blazebit.storage.core.api;

import com.blazebit.storage.core.model.jpa.UserAccount;

public interface UserAccountService {

	public void create(UserAccount userAccount);

	public void delete(long userAccountId);
}
