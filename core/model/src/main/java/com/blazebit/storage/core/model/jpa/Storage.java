package com.blazebit.storage.core.model.jpa;

public class Storage extends EmbeddedIdBaseEntity<StorageId> {

	private static final long serialVersionUID = 1L;

	public Storage() {
		super(new StorageId());
	}
	
	public Storage(StorageId id) {
		super(id);
	}

}
