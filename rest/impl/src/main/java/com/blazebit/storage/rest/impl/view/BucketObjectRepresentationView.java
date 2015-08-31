package com.blazebit.storage.rest.impl.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.storage.core.model.jpa.BucketObject;
import com.blazebit.storage.core.model.jpa.BucketObjectId;

@EntityView(BucketObject.class)
public interface BucketObjectRepresentationView {
	
	@IdMapping("id")
	public BucketObjectId getId();

	@Mapping("bucket.owner.id")
	public Long getOwnerId();
	
	public BucketObjectVersionRepresentationView getContentVersion();
	
}
