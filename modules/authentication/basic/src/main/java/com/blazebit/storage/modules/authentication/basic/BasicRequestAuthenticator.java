package com.blazebit.storage.modules.authentication.basic;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.blazebit.storage.modules.authentication.api.RequestAuthenticator;

public class BasicRequestAuthenticator implements RequestAuthenticator {

	@Override
	public String getAccountKey(HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		
		if (principal == null) {
			return null;
		}
		
		return principal.getName();
	}

	@Override
	public Set<String> getAccountRoles(HttpServletRequest request, Set<String> allRoles) {
		Principal principal = request.getUserPrincipal();
		
		if (principal == null) {
			return null;
		}
		
		Set<String> userRoles = new HashSet<>(allRoles.size());
		for (String role : allRoles) {
			if (request.isUserInRole(role)) {
				userRoles.add(role);
			}
		}
		
		return userRoles;
	}

}
