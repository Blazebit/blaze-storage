package com.blazebit.storage.rest.model;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MultipartUploadRepresentation implements Serializable, Closeable {

	private static final long serialVersionUID = 1L;

	private Map<String, BucketObjectUpdateRepresentation> uploads = new HashMap<String, BucketObjectUpdateRepresentation>(0);
	private Closeable inputResource;

	public MultipartUploadRepresentation() {
	}

	public MultipartUploadRepresentation(Map<String, BucketObjectUpdateRepresentation> uploads, Closeable inputResource) {
		this.uploads = uploads;
		this.inputResource = inputResource;
	}

	public Map<String, BucketObjectUpdateRepresentation> getUploads() {
		return uploads;
	}

	public void setUploads(Map<String, BucketObjectUpdateRepresentation> uploads) {
		this.uploads = uploads;
	}
	
	@Override
	public void close() {
		if (inputResource != null) {
			try {
				inputResource.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				inputResource = null;
			}
		}
	}
}
