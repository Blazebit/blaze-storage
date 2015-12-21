package com.blazebit.storage.core.api;

public class StorageQuotaPlanNotFoundException extends StorageException {
	
	private static final long serialVersionUID = 1L;

	public StorageQuotaPlanNotFoundException() {
		super();
	}

	public StorageQuotaPlanNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public StorageQuotaPlanNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public StorageQuotaPlanNotFoundException(String message) {
		super(message);
	}

	public StorageQuotaPlanNotFoundException(Throwable cause) {
		super(cause);
	}

}
