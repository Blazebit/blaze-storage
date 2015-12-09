package com.blazebit.storage.testsuite.common.data;

import java.util.UUID;

import com.blazebit.storage.core.model.jpa.Account;

public class AccountTestData {

	public static Account createAccount() {
		Account defaultOwner = new Account();
		defaultOwner.setKey(UUID.randomUUID().toString());
		defaultOwner.setName("test");
		return defaultOwner;
	}
}
