package com.blazebit.storage.core.model.jpa;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class StringBaseEntity extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;
	
	public StringBaseEntity() {
	}

	protected StringBaseEntity(String id) {
		super(id);
	}

	@Id
	@Override
	public String getId() {
		return id();
	}

}
