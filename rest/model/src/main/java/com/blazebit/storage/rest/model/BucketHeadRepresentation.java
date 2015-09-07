package com.blazebit.storage.rest.model;

import java.util.Calendar;

public class BucketHeadRepresentation extends BucketUpdateRepresentation {

	private static final long serialVersionUID = 1L;
	private String name;
	private String ownerKey;
	private Calendar creationDate;
	private String nextMarker;
	private StatisticsRepresentation statistics;

	public BucketHeadRepresentation() {
	}

	public BucketHeadRepresentation(String defaultStorageOwner, String defaultStorageName, String name, String ownerKey, Calendar creationDate, String nextMarker, StatisticsRepresentation statistics) {
		super(defaultStorageOwner, defaultStorageName);
		this.name = name;
		this.ownerKey = ownerKey;
		this.creationDate = creationDate;
		this.nextMarker = nextMarker;
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

	public String getNextMarker() {
		return nextMarker;
	}

	public void setNextMarker(String nextMarker) {
		this.nextMarker = nextMarker;
	}

	public StatisticsRepresentation getStatistics() {
		return statistics;
	}

	public void setStatistics(StatisticsRepresentation statistics) {
		this.statistics = statistics;
	}

}
