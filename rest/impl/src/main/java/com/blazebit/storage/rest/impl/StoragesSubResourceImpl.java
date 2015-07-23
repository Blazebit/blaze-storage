package com.blazebit.storage.rest.impl;

import java.util.List;

import javax.ws.rs.core.Response;

import com.blazebit.storage.rest.api.StorageSubResource;
import com.blazebit.storage.rest.api.StoragesSubResource;
import com.blazebit.storage.rest.model.StorageRepresentation;

public class StoragesSubResourceImpl extends AbstractResource implements StoragesSubResource {

	@Override
	public List<StorageRepresentation> getList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StorageSubResource get(String id) {
		return inject(new StorageSubResourceImpl(id));
	}

	@Override
	public Response createStorage(String path) {
		// TODO Auto-generated method stub
		return null;
	}

}
