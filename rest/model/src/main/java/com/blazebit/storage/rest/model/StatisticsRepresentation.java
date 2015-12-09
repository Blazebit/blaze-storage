package com.blazebit.storage.rest.model;

import java.io.Serializable;

public class StatisticsRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;
	private long objectCount;
	private long objectBytes;
	private long objectVersionCount;
	private long objectVersionBytes;

	public StatisticsRepresentation() {
	}

	public StatisticsRepresentation(long objectCount, long objectBytes, long objectVersionCount, long objectVersionBytes) {
		this.objectCount = objectCount;
		this.objectBytes = objectBytes;
		this.objectVersionCount = objectVersionCount;
		this.objectVersionBytes = objectVersionBytes;
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

	public long getObjectVersionCount() {
		return objectVersionCount;
	}

	public void setObjectVersionCount(long objectVersionCount) {
		this.objectVersionCount = objectVersionCount;
	}

	public long getObjectVersionBytes() {
		return objectVersionBytes;
	}

	public void setObjectVersionBytes(long objectVersionBytes) {
		this.objectVersionBytes = objectVersionBytes;
	}
}
