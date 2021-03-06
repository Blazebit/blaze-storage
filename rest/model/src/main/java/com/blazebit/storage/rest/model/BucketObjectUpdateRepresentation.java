package com.blazebit.storage.rest.model;

import com.blazebit.storage.rest.model.rs.ContentDisposition;

import java.io.InputStream;
import java.util.Map;

public class BucketObjectUpdateRepresentation extends BucketObjectBaseRepresentation {

	private static final long serialVersionUID = 1L;

	private String contentMD5; 
	private String externalContentKey;
	private TransferableContent content;
	private String copySource;
	
	public BucketObjectUpdateRepresentation() {
	}

	public BucketObjectUpdateRepresentation(String contentType, ContentDisposition contentDisposition, long size, String storageName, String storageOwner, Map<String, String> tags, String contentMD5, String externalContentKey, InputStream content, String copySource) {
		super(contentType, contentDisposition, size, storageName, storageOwner, tags);
		this.contentMD5 = contentMD5;
		this.externalContentKey = externalContentKey;
		this.content = TransferableContent.forInputStream(content);
		this.copySource = copySource;
	}

	public BucketObjectUpdateRepresentation(BucketObjectBaseRepresentation bucketObject) {
		this(bucketObject.getContentType(), bucketObject.getContentDisposition(), bucketObject.getSize(), bucketObject.getStorageName(), bucketObject.getStorageOwner(), bucketObject.getTags(), null, null, 
				bucketObject instanceof BucketObjectRepresentation ? ((BucketObjectRepresentation) bucketObject).getContent() : null, null);
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

	public TransferableContent getContent() {
		return content;
	}

	public void setContent(TransferableContent content) {
		this.content = content;
	}

	public void setContent(InputStream content) {
		this.content = TransferableContent.forInputStream(content);
	}

	public String getCopySource() {
		return copySource;
	}

	public void setCopySource(String copySource) {
		this.copySource = copySource;
	}
}
