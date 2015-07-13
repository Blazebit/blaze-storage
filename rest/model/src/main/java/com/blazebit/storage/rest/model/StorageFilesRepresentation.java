package com.blazebit.storage.rest.model;

import java.io.Serializable;
import java.util.List;

public class StorageFilesRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

	private OwnerRepresentation owner;
	private List<StorageRepresentation> storages;
}
