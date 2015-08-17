package com.blazebit.storage.modules.authentication.api;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

public interface RequestAuthenticator {

	public String getUserId(HttpServletRequest request);
	
	public Set<String> getUserRoles(HttpServletRequest request, Set<String> allRoles);
}
