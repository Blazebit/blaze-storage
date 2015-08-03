package com.blazebit.storage.rest.model;


public class AccountRepresentation extends AccountUpdateRepresentation {

	private static final long serialVersionUID = 1L;

	private String key;

	public AccountRepresentation() {
	}

	public AccountRepresentation(String key, String name) {
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
