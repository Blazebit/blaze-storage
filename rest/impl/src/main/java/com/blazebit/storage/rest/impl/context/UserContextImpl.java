package com.blazebit.storage.rest.impl.context;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.blazebit.storage.core.api.context.UserContext;

public class UserContextImpl implements UserContext {

	private final String userId;
	private final Set<String> userRoles;
	private final Locale locale;
	private final List<Locale> locales;
	
	public UserContextImpl(String userId, Set<String> userRoles, Locale locale, List<Locale> locales) {
		this.userId = userId;
		this.locale = locale;
		this.locales = locales;
		
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

	@Override
	public List<Locale> getLocales() {
		return locales;
	}

}
