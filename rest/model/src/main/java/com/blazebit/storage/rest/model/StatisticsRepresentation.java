package com.blazebit.storage.rest.model;

import java.io.Serializable;

public class StatisticsRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;
	private long objectCount;
	private long objectBytes;

	public StatisticsRepresentation() {
	}

	public StatisticsRepresentation(long objectCount, long objectBytes) {
		this.objectCount = objectCount;
		this.objectBytes = objectBytes;
	}

	public long getObjectCount() {
		return objectCount;
	}

	public void setObjectCount(long objectCount) {
		this.objectCount = objectCount;
	}

	public long getObjectBytes() {
		return objectBytes;
	}

	public void setObjectBytes(long objectBytes) {
		this.objectBytes = objectBytes;
	}
}
