package com.blazebit.storage.core.model.jpa;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name = "idGenerator", sequenceName = "user_account_seq")
public class UserAccount extends SequenceBaseEntity {

	private static final long serialVersionUID = 1L;

	private String key;
	private String name;
	
	public UserAccount() {
		super();
	}

	@Basic(optional = false)
	@Column(unique = true)
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Basic(optional = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
