package com.blazebit.storage.examples.localstorage.authentication;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.blazebit.storage.modules.authentication.api.RequestAuthenticator;

public class AlwaysAdminRequestAuthenticator implements RequestAuthenticator {

	@Override
	public String getAccountKey(HttpServletRequest request) {
		// This is just for test purposes, DO NOT USE IN PRODUCTION!
		String accountKey = request.getHeader("x-blz-account-key");
		
		if (accountKey == null || accountKey.isEmpty()) {
			return "admin";
		}
		
		return accountKey;
	}

	@Override
	public Set<String> getAccountRoles(HttpServletRequest request, Set<String> allRoles) {
		return allRoles;
	}

}
