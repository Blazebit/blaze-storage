package com.blazebit.storage.rest.model;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

public class StorageQuotaModelUpdateRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String description;
	private Set<Integer> limits = new TreeSet<>();

	public StorageQuotaModelUpdateRepresentation() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Integer> getLimits() {
		return limits;
	}

	public void setLimits(Set<Integer> limits) {
		this.limits = limits;
	}
}
