package com.blazebit.storage.rest.model;

import java.io.Serializable;
import java.util.Map;

import com.blazebit.storage.rest.model.rs.ContentDisposition;

public class BucketObjectBaseRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

//	@HeaderParam(HttpHeaders.CONTENT_TYPE)
	private String contentType = "binary/octet-stream";
//	@HeaderParam(HttpHeaders.CONTENT_DISPOSITION)
	private ContentDisposition contentDisposition;
//	@HeaderParam(HttpHeaders.CONTENT_LENGTH)
	private long size;
//	@HeaderParam("x-blz-storage-name")
	private String storageName;
//	@HeaderParam("x-blz-storage-owner")
	private String storageOwner;
//	@HeaderParam("x-blz-tags") 
	private Map<String, String> tags; 

	public BucketObjectBaseRepresentation() {
	}

	public BucketObjectBaseRepresentation(String contentType, ContentDisposition contentDisposition, long size, String storageName, String storageOwner, Map<String, String> tags) {
		this.contentType = contentType;
		this.contentDisposition = contentDisposition;
		this.size = size;
		this.storageName = storageName;
		this.storageOwner = storageOwner;
		this.tags = tags;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public ContentDisposition getContentDisposition() {
		return contentDisposition;
	}

	public void setContentDisposition(ContentDisposition contentDisposition) {
		this.contentDisposition = contentDisposition;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getStorageName() {
		return storageName;
	}

	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}

	public String getStorageOwner() {
		return storageOwner;
	}

	public void setStorageOwner(String storageOwner) {
		this.storageOwner = storageOwner;
	}

	public Map<String, String> getTags() {
		return tags;
	}

	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}

}
