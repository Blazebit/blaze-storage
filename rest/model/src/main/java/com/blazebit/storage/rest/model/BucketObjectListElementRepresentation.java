package com.blazebit.storage.rest.model;

import java.io.Serializable;
import java.util.Calendar;

public class BucketObjectListElementRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

	// TODO: contentType?
	private String key;
	private Calendar lastModified;
	private String entityTag;
	private long size;

	public BucketObjectListElementRepresentation() {
	}

	public BucketObjectListElementRepresentation(String key, Calendar lastModified, String entityTag, long size) {
		this.key = key;
		this.lastModified = lastModified;
		this.entityTag = entityTag;
		this.size = size;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

}
