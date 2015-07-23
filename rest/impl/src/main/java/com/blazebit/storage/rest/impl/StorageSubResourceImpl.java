package com.blazebit.storage.rest.impl;

import com.blazebit.storage.rest.api.DirectoriesSubResource;
import com.blazebit.storage.rest.api.FilesSubResource;
import com.blazebit.storage.rest.api.StorageSubResource;
import com.blazebit.storage.rest.model.StorageRepresentation;

public class StorageSubResourceImpl extends AbstractResource implements StorageSubResource {
	
	private String id;

	public StorageSubResourceImpl(String id) {
		this.id = id;
	}

	@Override
	public StorageRepresentation get() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DirectoriesSubResource getDirectories() {
		return inject(new DirectoriesSubResourceImpl());
	}

	@Override
	public FilesSubResource getFiles() {
		return inject(new FilesSubResourceImpl());
	}

}
