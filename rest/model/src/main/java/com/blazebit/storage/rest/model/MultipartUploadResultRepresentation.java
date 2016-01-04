package com.blazebit.storage.rest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MultipartUploadResultRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<String> uploaded = new ArrayList<String>(0);
	private List<MultipartUploadErrorRepresentation> errors = new ArrayList<MultipartUploadErrorRepresentation>(0);

	public MultipartUploadResultRepresentation() {
	}

	public MultipartUploadResultRepresentation(List<String> uploaded, List<MultipartUploadErrorRepresentation> errors) {
		this.uploaded = uploaded;
		this.errors = errors;
	}

	public List<String> getUploaded() {
		return uploaded;
	}

	public void setUploaded(List<String> uploaded) {
		this.uploaded = uploaded;
	}

	public List<MultipartUploadErrorRepresentation> getErrors() {
		return errors;
	}

	public void setErrors(List<MultipartUploadErrorRepresentation> errors) {
		this.errors = errors;
	}
}
