package com.blazebit.storage.core.api.context;

import java.util.List;
import java.util.Locale;
import java.util.Set;

public interface UserContext {
	
	public String getUserId();
	
	public Set<String> getUserRoles();

	public Locale getLocale();

	public List<Locale> getLocales();

}
