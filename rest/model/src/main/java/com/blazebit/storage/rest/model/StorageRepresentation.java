package com.blazebit.storage.rest.model;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import com.blazebit.storage.rest.model.config.StorageTypeConfigElementRepresentation;

public class StorageRepresentation extends StorageUpdateRepresentation<StorageTypeConfigElementRepresentation> {

	private static final long serialVersionUID = 1L;

	private String name;
	private Calendar creationDate;
	private StatisticsRepresentation statistics;

	public StorageRepresentation() {
	}

	public StorageRepresentation(String type, StorageQuotaPlanChoiceRepresentation quotaPlan,
			Set<StorageTypeConfigElementRepresentation> configuration, Map<String, String> tags, String name,
			Calendar creationDate, StatisticsRepresentation statistics) {
		super(type, quotaPlan, configuration, tags);
		this.name = name;
		this.creationDate = creationDate;
		this.statistics = statistics;
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

	public StatisticsRepresentation getStatistics() {
		return statistics;
	}

	public void setStatistics(StatisticsRepresentation statistics) {
		this.statistics = statistics;
	}
}
