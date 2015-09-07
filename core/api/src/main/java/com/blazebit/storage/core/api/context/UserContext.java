package com.blazebit.storage.core.api.context;

import java.util.List;
import java.util.Locale;
import java.util.Set;

public interface UserContext {
	
	public Long getAccountId();
	
	public String getAccountKey();
	
	public Set<String> getAccountRoles();

	public Locale getLocale();

	public List<Locale> getLocales();

}
