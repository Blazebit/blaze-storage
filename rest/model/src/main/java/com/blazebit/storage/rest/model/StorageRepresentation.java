package com.blazebit.storage.rest.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.blazebit.storage.rest.model.config.StorageTypeConfigElementRepresentation;

public class StorageRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

	private String type;
	private String name;
	private Calendar creationDate;
	private StorageQuotaPlanRepresentation quotaPlan;
	private Set<StorageTypeConfigElementRepresentation> configuration = new LinkedHashSet<>(0);
	private Map<String, String> tags = new HashMap<String, String>(0);
	private StatisticsRepresentation statistics;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Calendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	public StorageQuotaPlanRepresentation getQuotaPlan() {
		return quotaPlan;
	}

	public void setQuotaPlan(StorageQuotaPlanRepresentation quotaPlan) {
		this.quotaPlan = quotaPlan;
	}

	public Set<StorageTypeConfigElementRepresentation> getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Set<StorageTypeConfigElementRepresentation> configuration) {
		this.configuration = configuration;
	}

	public Map<String, String> getTags() {
		return tags;
	}

	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}

	public StatisticsRepresentation getStatistics() {
		return statistics;
	}

	public void setStatistics(StatisticsRepresentation statistics) {
		this.statistics = statistics;
	}
}
