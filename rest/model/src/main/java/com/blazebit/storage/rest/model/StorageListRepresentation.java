package com.blazebit.storage.rest.model;

import java.io.Serializable;
import java.util.List;

public class StorageListRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

	private OwnerRepresentation owner;
	private List<StorageListElementRepresentation> storages;

	public StorageListRepresentation() {
	}

	public StorageListRepresentation(OwnerRepresentation owner, List<StorageListElementRepresentation> storages) {
		this.owner = owner;
		this.storages = storages;
	}

	public OwnerRepresentation getOwner() {
		return owner;
	}

	public void setOwner(OwnerRepresentation owner) {
		this.owner = owner;
	}

	public List<StorageListElementRepresentation> getStorages() {
		return storages;
	}

	public void setStorages(List<StorageListElementRepresentation> storages) {
		this.storages = storages;
	}
}
