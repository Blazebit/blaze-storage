package com.blazebit.storage.rest.model;

import java.io.Serializable;
import java.util.Calendar;

public class StorageRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private Calendar creationDate;
	private String type;
	private boolean external;
}
