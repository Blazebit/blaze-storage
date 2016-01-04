package com.blazebit.storage.rest.model;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MultipartUploadRepresentation implements Serializable, Closeable {

	private static final long serialVersionUID = 1L;

	private boolean quiet;
	private Map<String, BucketObjectUpdateRepresentation> uploads = new HashMap<String, BucketObjectUpdateRepresentation>(0);
	private Closeable inputResource;

	public MultipartUploadRepresentation() {
	}

	public MultipartUploadRepresentation(boolean quiet, Map<String, BucketObjectUpdateRepresentation> uploads, Closeable inputResource) {
		this.quiet = quiet;
		this.uploads = uploads;
		this.inputResource = inputResource;
	}

	public boolean isQuiet() {
		return quiet;
	}

	public void setQuiet(boolean quiet) {
		this.quiet = quiet;
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
