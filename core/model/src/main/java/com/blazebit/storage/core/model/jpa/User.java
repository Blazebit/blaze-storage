package com.blazebit.storage.core.model.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;

	public User() {
		super();
	}

	public User(String id) {
		super(id);
	}

	@Id
	@Override
	public String getId() {
		return id();
	}
	
	
}
