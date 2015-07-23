package com.blazebit.storage.core.model.jpa;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class SequenceBaseEntity extends BaseEntity<Long> {

	private static final long serialVersionUID = 1L;
	
	public SequenceBaseEntity() {
	}

	protected SequenceBaseEntity(Long id) {
		super(id);
	}

	@Id
	@Override
	@GeneratedValue(generator = "idGenerator", strategy = GenerationType.SEQUENCE)
	public Long getId() {
		return id();
	}

}
