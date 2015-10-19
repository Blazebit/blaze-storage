package com.blazebit.storage.rest.model;

import java.util.Map;

import com.blazebit.storage.rest.model.rs.ContentDisposition;

public class BucketObjectUpdateRepresentation extends BucketObjectBaseRepresentation {

	private static final long serialVersionUID = 1L;
	
//	@HeaderParam("Content-MD5")
	private String contentMD5; 
//	@HeaderParam("x-blz-content-key") 
	private String externalContentKey;
	
	public BucketObjectUpdateRepresentation() {
	}

	public BucketObjectUpdateRepresentation(String contentType, ContentDisposition contentDisposition, long size, String storageName, String storageOwner, Map<String, String> tags, String contentMD5, String externalContentKey) {
		super(contentType, contentDisposition, size, storageName, storageOwner, tags);
		this.contentMD5 = contentMD5;
		this.externalContentKey = externalContentKey;
	}

	public BucketObjectUpdateRepresentation(BucketObjectBaseRepresentation bucketObject) {
		this(bucketObject.getContentType(), bucketObject.getContentDisposition(), bucketObject.getSize(), bucketObject.getStorageName(), bucketObject.getStorageOwner(), bucketObject.getTags(), null, null);
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
}
