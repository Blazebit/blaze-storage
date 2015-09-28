package com.blazebit.storage.rest.impl.view;

import java.util.Calendar;
import java.util.List;

import com.blazebit.persistence.view.CollectionMapping;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.ObjectStatistics;
import com.blazebit.storage.core.model.jpa.StorageId;
import com.blazebit.storage.rest.model.BucketObjectListElementRepresentation;
import com.blazebit.storage.rest.model.BucketRepresentation;
import com.blazebit.storage.rest.model.StatisticsRepresentation;
import com.fasterxml.jackson.annotation.JsonIgnore;

@EntityView(Bucket.class)
public abstract class BucketRepresentationView extends BucketRepresentation {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BucketRepresentationView(
			@Mapping("id") String id,
			@Mapping("owner.key") String ownerKey,
			@Mapping("storage.owner.key") String storageOwner,
			@Mapping("storage.id") StorageId storageId,
			@Mapping("creationDate") Calendar creationDate,
			@Mapping("statistics") ObjectStatistics statistics,
			@Mapping("objects") @CollectionMapping(ignoreIndex = true) List<BucketObjectListElementRepresentationView> objects) {
		super(storageOwner, storageId.getName(), id, ownerKey, creationDate, objects.size() > 1000 ? objects.remove(objects.size() - 1).getKey() : null, toStatistics(statistics), (List<BucketObjectListElementRepresentation>) (List) objects);
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
		result.setObjectCount(result.getObjectCount());
		return result;
	}
	
}
