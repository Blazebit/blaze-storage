package com.blazebit.storage.rest.impl.view;

import java.util.Calendar;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.ObjectStatistics;
import com.blazebit.storage.core.model.jpa.StorageId;
import com.blazebit.storage.rest.model.BucketHeadRepresentation;
import com.blazebit.storage.rest.model.StatisticsRepresentation;
import com.fasterxml.jackson.annotation.JsonIgnore;

@EntityView(Bucket.class)
public abstract class BucketHeadRepresentationView extends BucketHeadRepresentation {

	private static final long serialVersionUID = 1L;

	public BucketHeadRepresentationView(
			@Mapping("id") String id,
			@Mapping("owner.key") String ownerKey,
			@Mapping("storage.owner.key") String storageOwner,
			@Mapping("storage.id") StorageId storageId,
			@Mapping("creationDate") Calendar creationDate,
			@Mapping("statistics") ObjectStatistics statistics) {
		super(storageOwner, storageId.getName(), id, ownerKey, creationDate, null, toStatistics(statistics));
	}
	
	@JsonIgnore
	@IdMapping("id")
	public abstract String getId();
	
	@JsonIgnore
	@Mapping("owner.id")
	public abstract Long getOwnerId();
	
	private static StatisticsRepresentation toStatistics(ObjectStatistics statistics) {
		StatisticsRepresentation result = new StatisticsRepresentation();
		result.setObjectBytes(statistics.getObjectBytes());
		result.setObjectCount(statistics.getObjectCount());
		result.setObjectVersionBytes(statistics.getObjectVersionBytes());
		result.setObjectVersionCount(statistics.getObjectVersionCount());
		return result;
	}
	
}
