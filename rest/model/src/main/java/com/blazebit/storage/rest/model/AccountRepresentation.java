package com.blazebit.storage.rest.model;


public class AccountRepresentation extends AccountUpdate {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String key;

	public AccountRepresentation() {
	}

	public AccountRepresentation(Long id, String key, String name) {
		super(name);
		this.id = id;
		this.key = key;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}
