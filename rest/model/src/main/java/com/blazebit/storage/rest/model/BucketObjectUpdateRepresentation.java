package com.blazebit.storage.rest.model;

import java.util.Map;

import javax.ws.rs.HeaderParam;

public class BucketObjectUpdateRepresentation extends BucketObjectBaseRepresentation {

	private static final long serialVersionUID = 1L;
	
	@HeaderParam("Content-MD5")
	private String contentMD5; 
	@HeaderParam("x-blz-storage-name")
	private String storageName;
	@HeaderParam("x-blz-content-key") 
	private String externalContentKey;
	@HeaderParam("x-blz-tags") 
	private Map<String, String> tags; 
	
	public BucketObjectUpdateRepresentation() {
	}

	public BucketObjectUpdateRepresentation(String contentType, ContentDisposition contentDisposition, long size) {
		super(contentType, contentDisposition, size);
	}

	public String getContentMD5() {
		return contentMD5;
	}

	public void setContentMD5(String contentMD5) {
		this.contentMD5 = contentMD5;
	}

	public String getStorageName() {
		return storageName;
	}

	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}

	public String getExternalContentKey() {
		return externalContentKey;
	}

	public void setExternalContentKey(String externalContentKey) {
		this.externalContentKey = externalContentKey;
	}

	public Map<String, String> getTags() {
		return tags;
	}

	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}
}
