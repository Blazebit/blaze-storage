package com.blazebit.storage.rest.impl.view;

import java.net.URI;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.persistence.view.MappingParameter;
import com.blazebit.storage.core.api.StorageProviderFactoryDataAccess;
import com.blazebit.storage.core.api.spi.StorageProviderConfigurationElement;
import com.blazebit.storage.core.api.spi.StorageProviderFactory;
import com.blazebit.storage.core.api.spi.StorageProviderMetamodel;
import com.blazebit.storage.core.model.jpa.ObjectStatistics;
import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.jpa.StorageId;
import com.blazebit.storage.rest.model.StatisticsRepresentation;
import com.blazebit.storage.rest.model.StorageRepresentation;
import com.blazebit.storage.rest.model.config.StorageTypeConfigElementRepresentation;
import com.fasterxml.jackson.annotation.JsonIgnore;

@EntityView(Storage.class)
public abstract class StorageRepresentationView extends StorageRepresentation {

	private static final long serialVersionUID = 1L;

	public StorageRepresentationView(
			@Mapping("id.name") String name,
			@Mapping("uri") URI uri,
			@Mapping("creationDate") Calendar creationDate,
			@Mapping("quotaPlan") StorageQuotaPlanChoiceRepresentationView quotaPlan,
			@Mapping("tags") Map<String, String> tags,
			@Mapping("statistics") ObjectStatistics statistics,
			@MappingParameter("storageProviderFactoryDataAccess") StorageProviderFactoryDataAccess storageProviderFactoryDataAccess) {
		super(storageProviderFactoryDataAccess.getType(uri), quotaPlan, toConfiguration(storageProviderFactoryDataAccess, uri), tags, name, creationDate, toStatistics(statistics));
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
	
	private static Set<StorageTypeConfigElementRepresentation> toConfiguration(StorageProviderFactoryDataAccess storageProviderFactoryDataAccess, URI uri) {
		Map<String, String> configMap = storageProviderFactoryDataAccess.getConfiguration(uri);
		StorageProviderFactory factory = storageProviderFactoryDataAccess.findByScheme(storageProviderFactoryDataAccess.getType(uri));
		StorageProviderMetamodel metamodel = factory.getMetamodel();
		Set<StorageTypeConfigElementRepresentation> result = new LinkedHashSet<>(configMap.size());
		
		for (Map.Entry<String, String> entry : configMap.entrySet()) {
			StorageProviderConfigurationElement configElement = metamodel.getConfigurationElement(entry.getKey());
			StorageTypeConfigElementRepresentation resultConfig = new StorageTypeConfigElementRepresentation();
			resultConfig.setKey(entry.getKey());
			resultConfig.setType(configElement.getType());
			resultConfig.setValue(entry.getValue());
			resultConfig.setName(configElement.getName());
			resultConfig.setDescription(configElement.getDescription());
			result.add(resultConfig);
		}
		
		return result;
	}
	
}
