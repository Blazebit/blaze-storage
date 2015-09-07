package com.blazebit.storage.rest.model;

import java.io.Serializable;

public class BucketUpdateRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;
	private String defaultStorageOwner;
	private String defaultStorageName;

	public BucketUpdateRepresentation() {
	}

	public BucketUpdateRepresentation(String defaultStorageOwner, String defaultStorageName) {
		this.defaultStorageOwner = defaultStorageOwner;
		this.defaultStorageName = defaultStorageName;
	}

	public String getDefaultStorageOwner() {
		return defaultStorageOwner;
	}

	public void setDefaultStorageOwner(String defaultStorageOwner) {
		this.defaultStorageOwner = defaultStorageOwner;
	}

	public String getDefaultStorageName() {
		return defaultStorageName;
	}

	public void setDefaultStorageName(String defaultStorageName) {
		this.defaultStorageName = defaultStorageName;
	}

}
