package com.blazebit.storage.rest.impl.view;

import java.util.Calendar;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.rest.model.BucketListElementRepresentation;
import com.fasterxml.jackson.annotation.JsonIgnore;

@EntityView(Bucket.class)
public abstract class BucketListElementRepresentationView extends BucketListElementRepresentation {

	private static final long serialVersionUID = 1L;

	public BucketListElementRepresentationView(
			@Mapping("id") String id,
			@Mapping("creationDate") Calendar creationDate) {
		super(id, creationDate);
	}

	@JsonIgnore
	@IdMapping("id")
	public abstract String getId();
	
}
