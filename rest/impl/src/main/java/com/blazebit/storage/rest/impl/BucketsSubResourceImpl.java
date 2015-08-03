package com.blazebit.storage.rest.impl;

import java.util.List;

import com.blazebit.storage.rest.api.BucketSubResource;
import com.blazebit.storage.rest.api.BucketsSubResource;
import com.blazebit.storage.rest.model.BucketListElementRepresentation;

public class BucketsSubResourceImpl extends AbstractResource implements BucketsSubResource {

	@Override
	public List<BucketListElementRepresentation> get() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BucketSubResource get(String id) {
		return inject(new BucketSubResourceImpl(id));
	}


}
