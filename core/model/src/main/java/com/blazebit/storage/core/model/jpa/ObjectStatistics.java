package com.blazebit.storage.core.model.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class ObjectStatistics implements Serializable {

	private static final long serialVersionUID = 1L;

	private long objectCount;
	private long objectBytes;

	@NotNull
	@Column(name = "object_count")
	public long getObjectCount() {
		return objectCount;
	}

	public void setObjectCount(long objectCount) {
		this.objectCount = objectCount;
	}

	@NotNull
	@Column(name = "object_bytes")
	public long getObjectBytes() {
		return objectBytes;
	}

	public void setObjectBytes(long objectBytes) {
		this.objectBytes = objectBytes;
	}

}
