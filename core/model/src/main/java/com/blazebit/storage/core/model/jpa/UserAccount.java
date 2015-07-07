package com.blazebit.storage.core.model.jpa;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class UserAccount extends SequenceBaseEntity {

	private static final long serialVersionUID = 1L;

	private String name;

	@Basic(optional = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
