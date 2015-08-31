package com.blazebit.storage.core.api;

public class StorageNotFoundException extends StorageException {
	
	private static final long serialVersionUID = 1L;

	public StorageNotFoundException() {
		super();
	}

	public StorageNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public StorageNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public StorageNotFoundException(String message) {
		super(message);
	}

	public StorageNotFoundException(Throwable cause) {
		super(cause);
	}

}
