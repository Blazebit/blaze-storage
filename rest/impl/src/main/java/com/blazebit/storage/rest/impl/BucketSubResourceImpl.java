package com.blazebit.storage.rest.impl;

import javax.ws.rs.core.Response;

import com.blazebit.storage.rest.api.BucketSubResource;
import com.blazebit.storage.rest.api.FileSubResource;
import com.blazebit.storage.rest.model.BucketRepresentation;
import com.blazebit.storage.rest.model.BucketUpdateRepresentation;

public class BucketSubResourceImpl extends AbstractResource implements BucketSubResource {
	
	private String id;

	public BucketSubResourceImpl(String id) {
		this.id = id;
	}

	@Override
	public BucketRepresentation get(String prefix, Integer limit, String marker) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response head() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response delete() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response put(BucketUpdateRepresentation bucketUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileSubResource getFile(String key) {
		return inject(new FileSubResourceImpl(id, key));
	}

}
