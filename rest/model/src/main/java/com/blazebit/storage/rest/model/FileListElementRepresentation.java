package com.blazebit.storage.rest.model;

import java.io.Serializable;
import java.util.Calendar;

public class FileListElementRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

	private String key;
	private Calendar lastModified;
	private String eTag;
	private long size;

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

	public String geteTag() {
		return eTag;
	}

	public void seteTag(String eTag) {
		this.eTag = eTag;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

}
