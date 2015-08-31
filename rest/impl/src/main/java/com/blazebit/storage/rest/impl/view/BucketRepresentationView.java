package com.blazebit.storage.rest.impl.view;

import java.util.List;

import com.blazebit.persistence.view.CollectionMapping;
import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.persistence.view.MappingParameter;
import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.ObjectStatistics;
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
			@MappingParameter("prefix") String prefix,
			@MappingParameter("limit") Integer limit,
			@MappingParameter("marker") String marker,
			@Mapping("statistics") ObjectStatistics statistics,
			@Mapping("objects") @CollectionMapping(ignoreIndex = true) List<BucketObjectListElementRepresentationView> objects) {
		super(id, prefix, limit, marker, false, toStatistics(statistics), (List<BucketObjectListElementRepresentation>) (List) objects);
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
