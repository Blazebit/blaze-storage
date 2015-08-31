package com.blazebit.storage.rest.impl;

import java.util.List;

import javax.inject.Inject;

import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.BucketDataAccess;
import com.blazebit.storage.rest.api.BucketSubResource;
import com.blazebit.storage.rest.api.BucketsSubResource;
import com.blazebit.storage.rest.impl.view.BucketListElementRepresentationView;
import com.blazebit.storage.rest.model.BucketListElementRepresentation;

public class BucketsSubResourceImpl extends AbstractResource implements BucketsSubResource {
	
	@Inject
	private BucketDataAccess bucketDataAccess;
	
	private final long accountId;

	public BucketsSubResourceImpl(long accountId) {
		this.accountId = accountId;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<BucketListElementRepresentation> get() {
		return (List<BucketListElementRepresentation>) (List<?>) bucketDataAccess.findByAccountId(accountId, EntityViewSetting.create(BucketListElementRepresentationView.class));
	}

	@Override
	public BucketSubResource get(String id) {
		return inject(new BucketSubResourceImpl(accountId, id));
	}


}
