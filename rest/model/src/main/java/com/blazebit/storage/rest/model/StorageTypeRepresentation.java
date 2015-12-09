package com.blazebit.storage.rest.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.blazebit.storage.rest.model.config.StorageTypeConfigElementRepresentation;

public class StorageTypeRepresentation extends StorageTypeListElementRepresentation {

	private static final long serialVersionUID = 1L;

	private List<StorageTypeConfigElementRepresentation> configuration = new ArrayList<>(0);

	public StorageTypeRepresentation() {
	}

	public List<StorageTypeConfigElementRepresentation> getConfiguration() {
		return configuration;
	}

	public void setConfiguration(List<StorageTypeConfigElementRepresentation> configuration) {
		this.configuration = configuration;
	}
	
}
