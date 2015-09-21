package com.blazebit.storage.rest.model;

import java.io.Serializable;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.HttpHeaders;

import com.blazebit.storage.rest.model.rs.ContentDisposition;

public class BucketObjectBaseRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

	@HeaderParam(HttpHeaders.CONTENT_TYPE)
	private String contentType;
	@HeaderParam(HttpHeaders.CONTENT_DISPOSITION)
	private ContentDisposition contentDisposition;
	@HeaderParam(HttpHeaders.CONTENT_LENGTH)
	private long size;

	public BucketObjectBaseRepresentation() {
	}

	public BucketObjectBaseRepresentation(String contentType, ContentDisposition contentDisposition, long size) {
		this.contentType = contentType;
		this.contentDisposition = contentDisposition;
		this.size = size;
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

}
