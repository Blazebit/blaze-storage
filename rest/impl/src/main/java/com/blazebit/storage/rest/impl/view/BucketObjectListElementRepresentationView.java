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
			@Mapping("contentVersion.lastModified") Long lastModified, 
			@Mapping("contentVersion.entityTag") String entityTag, 
			@Mapping("contentVersion.contentLength") Long size) {
		super(id.getName(), fromTimestamp(lastModified), entityTag, size);
	}

	@JsonIgnore
	@IdMapping("id")
	public abstract BucketObjectId getId();

	private static Calendar fromTimestamp(Long timestamp) {
		if (timestamp == null) {
			return null;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		return calendar;
	}
}
