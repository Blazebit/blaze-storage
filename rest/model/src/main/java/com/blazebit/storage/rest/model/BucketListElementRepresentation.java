package com.blazebit.storage.rest.model;

import java.io.Serializable;
import java.util.Calendar;

public class BucketListElementRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private Calendar creationDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Calendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}
}
