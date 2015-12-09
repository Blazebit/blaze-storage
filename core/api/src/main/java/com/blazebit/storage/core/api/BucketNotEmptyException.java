package com.blazebit.storage.core.api;

public class BucketNotEmptyException extends StorageException {
	
	private static final long serialVersionUID = 1L;

	public BucketNotEmptyException() {
		super();
	}

	public BucketNotEmptyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BucketNotEmptyException(String message, Throwable cause) {
		super(message, cause);
	}

	public BucketNotEmptyException(String message) {
		super(message);
	}

	public BucketNotEmptyException(Throwable cause) {
		super(cause);
	}

}
