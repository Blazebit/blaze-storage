package com.blazebit.storage.rest.impl.context;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import com.blazebit.storage.core.api.context.UserContext;

public class UserContextImpl implements UserContext {

	private final String userId;
	private final Set<String> userRoles;
	private final Locale locale;
	
	public UserContextImpl(String userId, Set<String> userRoles, Locale locale) {
		this.userId = userId;
		this.locale = locale;
		
		if (userRoles == null) {
			this.userRoles = Collections.emptySet();
		} else {
			this.userRoles = userRoles;
		}
	}

	@Override
	public String getUserId() {
		return userId;
	}

	@Override
	public Set<String> getUserRoles() {
		return userRoles;
	}

	@Override
	public Locale getLocale() {
		return locale;
	}

}
