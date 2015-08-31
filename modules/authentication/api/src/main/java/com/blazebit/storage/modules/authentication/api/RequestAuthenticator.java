package com.blazebit.storage.modules.authentication.api;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

public interface RequestAuthenticator {

	public String getAccountKey(HttpServletRequest request);
	
	public Set<String> getAccountRoles(HttpServletRequest request, Set<String> allRoles);
}
