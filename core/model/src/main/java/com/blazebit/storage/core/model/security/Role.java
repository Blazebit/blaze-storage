package com.blazebit.storage.core.model.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class Role {

	public static final String ADMIN = "admin";
	public static final String USER = "user";
	public static final Set<String> ROLES = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
			ADMIN, USER
	)));

	private Role() {
	}
	
}
