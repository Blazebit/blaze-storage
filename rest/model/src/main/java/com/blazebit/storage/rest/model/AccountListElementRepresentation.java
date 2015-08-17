package com.blazebit.storage.rest.model;

import java.util.Map;

public class AccountListElementRepresentation extends AccountUpdateRepresentation {

	private static final long serialVersionUID = 1L;

	private String key;

	public AccountListElementRepresentation() {
	}

	public AccountListElementRepresentation(String key, String name) {
		super(name);
		this.key = key;
	}

	public AccountListElementRepresentation(String name, Map<String, String> tags, String key) {
		super(name, tags);
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}
