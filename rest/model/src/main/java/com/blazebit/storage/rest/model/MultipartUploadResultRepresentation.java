package com.blazebit.storage.rest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MultipartUploadResultRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<String> uploaded = new ArrayList<String>(0);
	private List<ErrorRepresentation> errors = new ArrayList<ErrorRepresentation>(0);

	public MultipartUploadResultRepresentation() {
	}

	public MultipartUploadResultRepresentation(List<String> uploaded, List<ErrorRepresentation> errors) {
		this.uploaded = uploaded;
		this.errors = errors;
	}

	public List<String> getUploaded() {
		return uploaded;
	}

	public void setUploaded(List<String> uploaded) {
		this.uploaded = uploaded;
	}

	public List<ErrorRepresentation> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorRepresentation> errors) {
		this.errors = errors;
	}
}
