package com.blazebit.storage.rest.model;

import java.util.Calendar;
import java.util.Map;

import com.blazebit.storage.rest.model.rs.ContentDisposition;

public class BucketObjectHeadRepresentation extends BucketObjectBaseRepresentation {

	private static final long serialVersionUID = 1L;
	
	private Calendar lastModified;
	private String entityTag;

	public BucketObjectHeadRepresentation() {
		super();
	}

	public BucketObjectHeadRepresentation(String contentType, ContentDisposition contentDisposition, long size, String storageName, String storageOwner, Map<String, String> tags, Calendar lastModified, String entityTag) {
		super(contentType, contentDisposition, size, storageName, storageOwner, tags);
		this.lastModified = lastModified;
		this.entityTag = entityTag;
	}

	public Calendar getLastModified() {
		return lastModified;
	}

	public void setLastModified(Calendar lastModified) {
		this.lastModified = lastModified;
	}

	public String getEntityTag() {
		return entityTag;
	}

	public void setEntityTag(String entityTag) {
		this.entityTag = entityTag;
	}

}