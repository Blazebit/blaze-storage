package com.blazebit.storage.rest.model;

import java.util.LinkedHashSet;
import java.util.Set;

import com.blazebit.storage.rest.model.config.StorageTypeConfigElementRepresentation;

public class StorageTypeRepresentation extends StorageTypeListElementRepresentation {

	private static final long serialVersionUID = 1L;

	private Set<StorageTypeConfigElementRepresentation> configuration = new LinkedHashSet<>(0);

	public StorageTypeRepresentation() {
	}

	public Set<StorageTypeConfigElementRepresentation> getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Set<StorageTypeConfigElementRepresentation> configuration) {
		this.configuration = configuration;
	}
	
}
