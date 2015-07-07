package com.blazebit.storage.core.model.jpa;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity<I extends Serializable> implements IdHolder<I>, Serializable {

	private static final long serialVersionUID = 1L;
	
	private I id;

	public BaseEntity() {
	}

	protected BaseEntity(I id) {
		this.id = id;
	}

	protected I id() {
		return id;
	}

	public void setId(I id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getNoProxyClass(getClass()) != getNoProxyClass(obj.getClass())) {
			return false;
		}
		BaseEntity<?> other = (BaseEntity<?>) obj;
		
		// Entities without identity are not comparable
		if (id == null || other.getId() == null) {
			return false;
		}
		
		return id.equals(other.getId());
	}

	protected static Class<?> getNoProxyClass(Class<?> clazz) {
		while (clazz.getName().contains("$")) {
			clazz = clazz.getSuperclass();
		}
		
		return clazz;
	}
}
