package com.blazebit.storage.rest.impl;

import java.util.List;

import javax.inject.Inject;

import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.BucketDataAccess;
import com.blazebit.storage.rest.api.AdminResource;
import com.blazebit.storage.rest.impl.view.BucketListElementRepresentationView;
import com.blazebit.storage.rest.model.BucketListElementRepresentation;

public class AdminResourceImpl extends AbstractResource implements AdminResource {

	@Inject
	private BucketDataAccess bucketDataAccess;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<BucketListElementRepresentation> getBuckets() {
		return (List<BucketListElementRepresentation>) (List<?>) bucketDataAccess.findAll(EntityViewSetting.create(BucketListElementRepresentationView.class));
	}

}
