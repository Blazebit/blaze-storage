package com.blazebit.storage.rest.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.blazebit.storage.rest.model.config.StorageTypeConfigEntryRepresentation;

public class StorageUpdateRepresentation<T extends StorageTypeConfigEntryRepresentation> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String type;
	private StorageQuotaPlanChoiceRepresentation quotaPlan;
	private Set<T> configuration = new LinkedHashSet<>(0);
	private Map<String, String> tags = new HashMap<String, String>(0);

	public StorageUpdateRepresentation() {
		super();
	}

	public StorageUpdateRepresentation(String type, StorageQuotaPlanChoiceRepresentation quotaPlan, Set<T> configuration, Map<String, String> tags) {
		this.type = type;
		this.quotaPlan = quotaPlan;
		this.configuration = configuration;
		this.tags = tags;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public StorageQuotaPlanChoiceRepresentation getQuotaPlan() {
		return quotaPlan;
	}

	public void setQuotaPlan(StorageQuotaPlanChoiceRepresentation quotaPlan) {
		this.quotaPlan = quotaPlan;
	}

	public Set<T> getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Set<T> configuration) {
		this.configuration = configuration;
	}

	public Map<String, String> getTags() {
		return tags;
	}

	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}
}
