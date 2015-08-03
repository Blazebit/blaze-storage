package com.blazebit.storage.rest.impl;

import java.io.InputStream;

import javax.ws.rs.core.Response;

import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.BucketObjectId;
import com.blazebit.storage.rest.api.FileSubResource;

public class FileSubResourceImpl extends AbstractResource implements FileSubResource {
	
	private BucketObjectId id;
	
	public FileSubResourceImpl(String bucketId, String key) {
		this.id = new BucketObjectId(new Bucket(bucketId), key);
	}

	@Override
	public Response get() {
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
	public Response put(InputStream inputStream) {
		// TODO Auto-generated method stub
		return null;
	}

}
