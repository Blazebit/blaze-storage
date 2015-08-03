package com.blazebit.storage.rest.model;

import java.io.Serializable;

public class BucketUpdateRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;
	private String defaultStorageName;

	public String getDefaultStorageName() {
		return defaultStorageName;
	}

	public void setDefaultStorageName(String defaultStorageName) {
		this.defaultStorageName = defaultStorageName;
	}

}
