package com.blazebit.storage.core.model.jpa;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Embeddable
public class DirectoryId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private UserAccount owner;
	private String name;
	
	public DirectoryId() {
	}

	public DirectoryId(UserAccount owner, String name) {
		this.owner = owner;
		this.name = name;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	public UserAccount getOwner() {
		return owner;
	}
	
	public void setOwner(UserAccount owner) {
		this.owner = owner;
	}
	
	@Basic(optional = false)
	@Column(length = RdbmsConstants.FILE_NAME_MAX_LENGTH)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DirectoryId other = (DirectoryId) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		return true;
	}
}
