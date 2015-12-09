package com.blazebit.storage.core.api;

public class BucketObjectNotFoundException extends StorageException {
	
	private static final long serialVersionUID = 1L;

	public BucketObjectNotFoundException() {
		super();
	}

	public BucketObjectNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BucketObjectNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public BucketObjectNotFoundException(String message) {
		super(message);
	}

	public BucketObjectNotFoundException(Throwable cause) {
		super(cause);
	}

}
