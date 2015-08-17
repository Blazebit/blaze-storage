package com.blazebit.storage.rest.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AccountUpdateRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private Map<String, String> tags = new HashMap<String, String>();

	public AccountUpdateRepresentation() {
	}

	public AccountUpdateRepresentation(String name) {
		this.name = name;
	}

	public AccountUpdateRepresentation(String name, Map<String, String> tags) {
		this.name = name;
		this.tags = tags;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getTags() {
		return tags;
	}

	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}
}
