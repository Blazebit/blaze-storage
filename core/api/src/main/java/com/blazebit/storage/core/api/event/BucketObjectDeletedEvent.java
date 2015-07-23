package com.blazebit.storage.core.api.event;

import java.io.Serializable;

import com.blazebit.storage.core.model.jpa.BucketObjectId;

public class BucketObjectDeletedEvent implements Serializable {

	private static final long serialVersionUID = 1L;
	private final BucketObjectId bucketObjectId;

	public BucketObjectDeletedEvent(BucketObjectId bucketObjectId) {
		this.bucketObjectId = bucketObjectId;
	}

	public BucketObjectId getBucketObjectId() {
		return bucketObjectId;
	}

}
