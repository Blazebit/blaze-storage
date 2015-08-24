package com.blazebit.storage.rest.impl.view;

import java.net.URI;
import java.util.Calendar;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.persistence.view.MappingParameter;
import com.blazebit.storage.core.api.StorageProviderFactoryDataAccess;
import com.blazebit.storage.core.model.jpa.ObjectStatistics;
import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.jpa.StorageId;
import com.blazebit.storage.rest.model.StatisticsRepresentation;
import com.blazebit.storage.rest.model.StorageListElementRepresentation;
import com.fasterxml.jackson.annotation.JsonIgnore;

@EntityView(Storage.class)
public abstract class StorageListElementRepresentationView extends StorageListElementRepresentation {

	private static final long serialVersionUID = 1L;

	public StorageListElementRepresentationView(
			@Mapping("id.name") String name,
			@Mapping("uri") URI uri,
			@Mapping("creationDate") Calendar creationDate,
			@Mapping("quotaPlan") StorageQuotaPlanChoiceRepresentationView quotaPlan,
			@Mapping("statistics") ObjectStatistics statistics,
			@MappingParameter("storageProviderFactoryDataAccess") StorageProviderFactoryDataAccess storageProviderFactoryDataAccess) {
		super(name, storageProviderFactoryDataAccess.getType(uri), creationDate, quotaPlan, toStatistics(statistics));
	}
	
	@JsonIgnore
	@IdMapping("id")
	public abstract StorageId getId();
	
	private static StatisticsRepresentation toStatistics(ObjectStatistics statistics) {
		StatisticsRepresentation result = new StatisticsRepresentation();
		result.setObjectBytes(statistics.getObjectBytes());
		result.setObjectCount(result.getObjectCount());
		return result;
	}
	
}
