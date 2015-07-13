package com.blazebit.storage.core.model.jpa;

import java.net.URI;

import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.blazebit.storage.core.model.jpa.converter.URIConverter;

@Entity
@SequenceGenerator(name = "idGenerator", sequenceName = "bucket_object_version_seq")
public class BucketObjectVersion extends SequenceBaseEntity {
	
	private static final long serialVersionUID = 1L;

	public static final String DEFAULT_CONTENT_TYPE = "binary/octet-stream";

	private BucketObject object;
	private BucketObjectState state;
	private Storage storage;
	
	private URI contentUri;
	private long contentLength;
	private String contentType = DEFAULT_CONTENT_TYPE;
	private String contentMD5;
	private String contentDisposition;
	private String eTag;
	private long lastModified;
	
	@ManyToOne(optional = false)
	public BucketObject getObject() {
		return object;
	}

	public void setObject(BucketObject object) {
		this.object = object;
	}

	@Enumerated(EnumType.ORDINAL)
	public BucketObjectState getState() {
		return state;
	}

	public void setState(BucketObjectState state) {
		this.state = state;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}

	@Convert(converter = URIConverter.class)
	public URI getContentUri() {
		return contentUri;
	}

	public void setContentUri(URI contentUri) {
		this.contentUri = contentUri;
	}

	@Basic(optional = false)
	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	@Basic(optional = false)
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Basic(optional = false)
	public String getContentMD5() {
		return contentMD5;
	}

	public void setContentMD5(String contentMD5) {
		this.contentMD5 = contentMD5;
	}

	@Basic(optional = false)
	public String getContentDisposition() {
		return contentDisposition;
	}

	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}

	@Basic(optional = false)
	public String getETag() {
		return eTag;
	}

	public void setETag(String eTag) {
		this.eTag = eTag;
	}

	@Basic(optional = false)
	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
}
