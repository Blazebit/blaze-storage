package com.blazebit.storage.rest.impl;

import com.blazebit.storage.rest.api.BucketsResource;
import com.blazebit.storage.rest.api.BucketsSubResource;

public class BucketsResourceImpl extends AbstractResource implements BucketsResource {

	@Override
	public BucketsSubResource get() {
		return inject(new BucketsSubResourceImpl(userContext.getAccountId()));
	}

}
