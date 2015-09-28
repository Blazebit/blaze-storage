package com.blazebit.storage.rest.model;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Map;

import com.blazebit.storage.rest.model.rs.ContentDisposition;

public class BucketObjectRepresentation extends BucketObjectHeadRepresentation {

	private static final long serialVersionUID = 1L;

	private InputStream content;

	public BucketObjectRepresentation() {
		super();
	}

	public BucketObjectRepresentation(String contentType, ContentDisposition contentDisposition, long size, String storageName, String storageOwner, Map<String, String> tags, Calendar lastModified, String entityTag, InputStream content) {
		super(contentType, contentDisposition, size, storageName, storageOwner, tags, lastModified, entityTag);
		this.content = content;
	}

	public InputStream getContent() {
		return content;
	}

	public void setContent(InputStream content) {
		this.content = content;
	}

}
