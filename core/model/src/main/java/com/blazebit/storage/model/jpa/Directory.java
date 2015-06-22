package com.blazebit.storage.model.jpa;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
public class Directory extends EmbeddedIdBaseEntity<DirectoryId> {

	private static final long serialVersionUID = 1L;

	private Directory parent;
	private Storage storage;
	
	public Directory() {
		super(new DirectoryId());
	}
	
	public Directory(DirectoryId id) {
		super(id);
	}

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	public Directory getParent() {
		return parent;
	}

	public void setParent(Directory parent) {
		this.parent = parent;
	}
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}

}
