package com.blazebit.storage.core.model.jpa;

public interface RdbmsConstants {

	public static final String PREFIX = "stor_"; 
	public static final int NAME_MAX_LENGTH = 255;
	public static final int FILE_NAME_MAX_LENGTH = 1024;
	public static final int TAGS_MAX_LENGTH = 32704; // NOTE: we chose this because that is the maximum length in DB2
}
