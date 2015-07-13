package com.blazebit.storage.core.model.jpa;

public enum BucketObjectState {

	/**
	 * This is, or will be used for long/multipart uploads.
	 */
	CREATING,
	/**
	 * The object is created and usable.
	 */
	CREATED,
	/**
	 * Deletion of the object was requested, not usable anymore.
	 */
	REMOVING;
	
}
