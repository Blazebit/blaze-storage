package com.blazebit.storage.rest.model;

import java.io.Serializable;

public class OwnerRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String displayName;

	public OwnerRepresentation() {
	}

	public OwnerRepresentation(String id, String displayName) {
		this.id = id;
		this.displayName = displayName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
