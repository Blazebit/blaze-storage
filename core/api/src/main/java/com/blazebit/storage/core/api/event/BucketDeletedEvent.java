package com.blazebit.storage.core.api.event;

import java.io.Serializable;

public class BucketDeletedEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String bucketId;

	public BucketDeletedEvent(String bucketId) {
		this.bucketId = bucketId;
	}

	public String getBucketId() {
		return bucketId;
	}

}
