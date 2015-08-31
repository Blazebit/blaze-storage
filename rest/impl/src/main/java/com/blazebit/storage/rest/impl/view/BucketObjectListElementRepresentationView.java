package com.blazebit.storage.rest.impl.view;

import java.util.Calendar;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.storage.core.model.jpa.BucketObject;
import com.blazebit.storage.core.model.jpa.BucketObjectId;
import com.blazebit.storage.rest.model.BucketObjectListElementRepresentation;
import com.fasterxml.jackson.annotation.JsonIgnore;

@EntityView(BucketObject.class)
public abstract class BucketObjectListElementRepresentationView extends BucketObjectListElementRepresentation {

	private static final long serialVersionUID = 1L;

	public BucketObjectListElementRepresentationView(
			@Mapping("id") BucketObjectId id, 
			@Mapping("lastModified") Calendar lastModified, 
			@Mapping("eTag") String eTag, 
			@Mapping("size") Long size) {
		super(id.getName(), lastModified, eTag, size);
	}

	@JsonIgnore
	@IdMapping("id")
	public abstract BucketObjectId getId();
	
}
