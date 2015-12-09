package com.blazebit.storage.rest.impl.view;

import java.util.Calendar;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.ObjectStatistics;
import com.blazebit.storage.rest.model.BucketListElementRepresentation;
import com.blazebit.storage.rest.model.StatisticsRepresentation;
import com.fasterxml.jackson.annotation.JsonIgnore;

@EntityView(Bucket.class)
public abstract class BucketListElementRepresentationView extends BucketListElementRepresentation {

	private static final long serialVersionUID = 1L;

	public BucketListElementRepresentationView(
			@Mapping("id") String id,
			@Mapping("owner.key") String ownerKey,
			@Mapping("creationDate") Calendar creationDate,
			@Mapping("statistics") ObjectStatistics statistics) {
		super(id, ownerKey, creationDate, toStatistics(statistics));
	}

	@JsonIgnore
	@IdMapping("id")
	public abstract String getId();
	
	private static StatisticsRepresentation toStatistics(ObjectStatistics statistics) {
		StatisticsRepresentation result = new StatisticsRepresentation();
		result.setObjectBytes(statistics.getObjectBytes());
		result.setObjectCount(statistics.getObjectCount());
		result.setObjectVersionBytes(statistics.getObjectVersionBytes());
		result.setObjectVersionCount(statistics.getObjectVersionCount());
		return result;
	}
	
}
