package com.blazebit.storage.examples.localstorage.authentication;

import java.util.Collections;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.blazebit.storage.modules.authentication.api.RequestAuthenticator;

public class AlwaysAdminRequestAuthenticator implements RequestAuthenticator {

	@Override
	public String getUserId(HttpServletRequest request) {
		// This is just for test purposes, DO NOT USE IN PRODUCTION!
		return "admin";
	}

	@Override
	public Set<String> getUserRoles(HttpServletRequest request, Set<String> allRoles) {
		return Collections.singleton("admin");
	}

}
