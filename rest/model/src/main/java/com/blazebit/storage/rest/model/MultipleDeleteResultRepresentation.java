package com.blazebit.storage.rest.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MultipleDeleteResultRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<MultipleDeleteObjectResultRepresentation> deleted = new ArrayList<MultipleDeleteObjectResultRepresentation>(0);
	private List<ErrorRepresentation> errors = new ArrayList<ErrorRepresentation>(0);

	public MultipleDeleteResultRepresentation() {
	}

	public MultipleDeleteResultRepresentation(List<MultipleDeleteObjectResultRepresentation> deleted, List<ErrorRepresentation> errors) {
		this.deleted = deleted;
		this.errors = errors;
	}

	public List<MultipleDeleteObjectResultRepresentation> getDeleted() {
		return deleted;
	}

	public void setDeleted(List<MultipleDeleteObjectResultRepresentation> deleted) {
		this.deleted = deleted;
	}

	public List<ErrorRepresentation> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorRepresentation> errors) {
		this.errors = errors;
	}
}
