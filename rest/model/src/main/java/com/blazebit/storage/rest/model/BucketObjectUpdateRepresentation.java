package com.blazebit.storage.rest.model;

import java.io.InputStream;
import java.util.Map;

import com.blazebit.storage.rest.model.rs.ContentDisposition;

public class BucketObjectUpdateRepresentation extends BucketObjectBaseRepresentation {

	private static final long serialVersionUID = 1L;
	
	private String contentMD5; 
	private String externalContentKey;
	
	private InputStream content;
	
	public BucketObjectUpdateRepresentation() {
	}

	public BucketObjectUpdateRepresentation(String contentType, ContentDisposition contentDisposition, long size, String storageName, String storageOwner, Map<String, String> tags, String contentMD5, String externalContentKey, InputStream content) {
		super(contentType, contentDisposition, size, storageName, storageOwner, tags);
		this.contentMD5 = contentMD5;
		this.externalContentKey = externalContentKey;
		this.content = content;
	}

	public BucketObjectUpdateRepresentation(BucketObjectBaseRepresentation bucketObject) {
		this(bucketObject.getContentType(), bucketObject.getContentDisposition(), bucketObject.getSize(), bucketObject.getStorageName(), bucketObject.getStorageOwner(), bucketObject.getTags(), null, null, bucketObject instanceof BucketObjectRepresentation ? ((BucketObjectRepresentation) bucketObject).getContent() : null);
	}

	public String getContentMD5() {
		return contentMD5;
	}

	public void setContentMD5(String contentMD5) {
		this.contentMD5 = contentMD5;
	}

	public String getExternalContentKey() {
		return externalContentKey;
	}

	public void setExternalContentKey(String externalContentKey) {
		this.externalContentKey = externalContentKey;
	}

	public InputStream getContent() {
		return content;
	}

	public void setContent(InputStream content) {
		this.content = content;
	}
}
