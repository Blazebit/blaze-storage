package com.blazebit.storage.rest.model;

import java.io.Serializable;
import java.util.Calendar;

public class BucketListElementRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String ownerKey;
	private Calendar creationDate;
	private StatisticsRepresentation statistics;

	public BucketListElementRepresentation() {
	}

	public BucketListElementRepresentation(String name, String ownerKey, Calendar creationDate, StatisticsRepresentation statistics) {
		this.name = name;
		this.ownerKey = ownerKey;
		this.creationDate = creationDate;
		this.statistics = statistics;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwnerKey() {
		return ownerKey;
	}

	public void setOwnerKey(String ownerKey) {
		this.ownerKey = ownerKey;
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
