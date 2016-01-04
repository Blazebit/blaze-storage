package com.blazebit.storage.rest.model;

import java.io.Serializable;

public class MultipleDeleteObjectResultRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

	private String key;

	public MultipleDeleteObjectResultRepresentation() {
	}

	public MultipleDeleteObjectResultRepresentation(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
