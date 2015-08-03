package com.blazebit.storage.rest.model;

import java.io.Serializable;
import java.util.List;

public class StorageListRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

	private OwnerRepresentation owner;
	private List<StorageRepresentation> storages;

	public OwnerRepresentation getOwner() {
		return owner;
	}

	public void setOwner(OwnerRepresentation owner) {
		this.owner = owner;
	}

	public List<StorageRepresentation> getStorages() {
		return storages;
	}

	public void setStorages(List<StorageRepresentation> storages) {
		this.storages = storages;
	}
}
