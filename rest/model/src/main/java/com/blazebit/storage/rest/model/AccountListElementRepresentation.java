package com.blazebit.storage.rest.model;


public class AccountListElementRepresentation extends AccountUpdateRepresentation {

	private static final long serialVersionUID = 1L;

	private String key;

	public AccountListElementRepresentation() {
	}

	public AccountListElementRepresentation(String key, String name) {
		super(name);
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}
