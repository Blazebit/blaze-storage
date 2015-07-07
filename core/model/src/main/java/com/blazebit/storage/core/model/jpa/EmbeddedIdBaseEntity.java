package com.blazebit.storage.core.model.jpa;

import java.io.Serializable;

import javax.persistence.EmbeddedId;

public abstract class EmbeddedIdBaseEntity<I extends Serializable> extends BaseEntity<I> {

	private static final long serialVersionUID = 1L;

	public EmbeddedIdBaseEntity(I id) {
		super(id);
	}
	
	@EmbeddedId
	@Override
	public I getId() {
		return id();
	}

}
