package com.blazebit.storage.server.storage;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.blazebit.storage.rest.model.StorageRepresentation;

@Named
@RequestScoped
public class StorageDetailPage extends StorageBasePage {

	private static final long serialVersionUID = 1L;
	
	private String typeName;
	
	@Override
	protected void init() {
		super.init();
		if (storage == null) {
			this.typeName = null;
		} else {
			this.typeName = client.storageTypes().get(storage.getType()).getName();
		}
	}

	public StorageRepresentation getStorage() {
		return (StorageRepresentation) storage;
	}

	public String getTypeName() {
		return typeName;
	}
}
