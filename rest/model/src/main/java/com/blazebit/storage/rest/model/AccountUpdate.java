package com.blazebit.storage.rest.model;

import java.io.Serializable;

public class AccountUpdate implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	public AccountUpdate() {
	}

	public AccountUpdate(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
