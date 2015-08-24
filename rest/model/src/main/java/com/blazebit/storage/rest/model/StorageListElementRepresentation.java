package com.blazebit.storage.rest.model;

import java.io.Serializable;
import java.util.Calendar;

public class StorageListElementRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String type;
	private Calendar creationDate;
	private StorageQuotaPlanChoiceRepresentation quotaPlan;
	private StatisticsRepresentation statistics;

	public StorageListElementRepresentation() {
	}

	public StorageListElementRepresentation(String name, String type, Calendar creationDate, StorageQuotaPlanChoiceRepresentation quotaPlan, StatisticsRepresentation statistics) {
		this.name = name;
		this.type = type;
		this.creationDate = creationDate;
		this.quotaPlan = quotaPlan;
		this.statistics = statistics;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Calendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	public StorageQuotaPlanChoiceRepresentation getQuotaPlan() {
		return quotaPlan;
	}

	public void setQuotaPlan(StorageQuotaPlanChoiceRepresentation quotaPlan) {
		this.quotaPlan = quotaPlan;
	}

	public StatisticsRepresentation getStatistics() {
		return statistics;
	}

	public void setStatistics(StatisticsRepresentation statistics) {
		this.statistics = statistics;
	}
}
