package com.blazebit.storage.rest.model;

import java.io.InputStream;
import java.util.Calendar;

import com.blazebit.storage.rest.model.rs.ContentDisposition;

public class BucketObjectRepresentation extends BucketObjectBaseRepresentation {

	private static final long serialVersionUID = 1L;

	private Calendar lastModified;
	private String entityTag;
	
	private InputStream content;

	public BucketObjectRepresentation() {
		super();
	}

	public BucketObjectRepresentation(String contentType, ContentDisposition contentDisposition, long size) {
		super(contentType, contentDisposition, size);
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

	public InputStream getContent() {
		return content;
	}

	public void setContent(InputStream content) {
		this.content = content;
	}

}
