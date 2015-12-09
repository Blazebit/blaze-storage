package com.blazebit.storage.testsuite.common.context;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;

import com.blazebit.storage.core.api.context.UserContext;

@ApplicationScoped
public class TestUserContext implements UserContext, Serializable {

	private static final long serialVersionUID = 1L;

	private Long accountId;
	private String accountKey;
	private Set<String> accountRoles;
	private Locale locale;
	private List<Locale> locales;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAccountKey() {
		return accountKey;
	}

	public void setAccountKey(String accountKey) {
		this.accountKey = accountKey;
	}

	public Set<String> getAccountRoles() {
		return accountRoles;
	}

	public void setAccountRoles(Set<String> accountRoles) {
		this.accountRoles = accountRoles;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public List<Locale> getLocales() {
		return locales;
	}

	public void setLocales(List<Locale> locales) {
		this.locales = locales;
	}
}
