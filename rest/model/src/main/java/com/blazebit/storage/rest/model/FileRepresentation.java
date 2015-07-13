package com.blazebit.storage.rest.model;

import java.io.Serializable;
import java.util.Calendar;

public class FileRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

	private String key;
	private Calendar lastModified;
	private long size;
	
}
