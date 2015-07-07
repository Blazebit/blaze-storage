package com.blazebit.storage.core.model.jpa;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public abstract class SequenceBaseEntity extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;

	@Id
	@Override
	@GeneratedValue(generator = "idGenerator", strategy = GenerationType.SEQUENCE)
	public Long getId() {
		return id();
	}

}
