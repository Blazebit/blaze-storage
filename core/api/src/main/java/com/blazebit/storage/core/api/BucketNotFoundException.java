package com.blazebit.storage.core.api;

public class BucketNotFoundException extends StorageException {
	
	private static final long serialVersionUID = 1L;

	public BucketNotFoundException() {
		super();
	}

	public BucketNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BucketNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public BucketNotFoundException(String message) {
		super(message);
	}

	public BucketNotFoundException(Throwable cause) {
		super(cause);
	}

}
