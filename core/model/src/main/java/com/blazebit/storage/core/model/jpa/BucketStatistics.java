package com.blazebit.storage.core.model.jpa;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class BucketStatistics implements Serializable {

	private static final long serialVersionUID = 1L;

	private long objectCount;
	private long objectBytes;

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
