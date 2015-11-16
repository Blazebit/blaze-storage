package com.blazebit.storage.rest.model;

public final class BlazeStorageHeaders {

	public static final String BUCKET_NAME = "x-blz-bucket-name";
	public static final String TIMESTAMP = "x-blz-timestamp";
	public static final String NEXT_MARKER = "x-blz-next-marker";
	public static final String OBJECT_BYTES = "x-blz-object-bytes";
	public static final String OBJECT_COUNT = "x-blz-object-count";

	public static final String OWNER_KEY = "x-blz-owner-key";
	public static final String DEFAULT_STORAGE_OWNER = "x-blz-default-storage-owner";
	public static final String DEFAULT_STORAGE_NAME = "x-blz-default-storage-name";
	public static final String STORAGE_OWNER = "x-blz-storage-owner";
	public static final String STORAGE_NAME = "x-blz-storage-name";
	public static final String TAG_PREFIX = "x-blz-storage-tag-";
	public static final String ERROR_CODE = "x-blz-error-code";
	public static final String CONTENT_KEY = "x-blz-content-key";
	public static final String CONTENT_MD5 = "x-blz-content-md5";
	
	private BlazeStorageHeaders() {
	}
	
}
