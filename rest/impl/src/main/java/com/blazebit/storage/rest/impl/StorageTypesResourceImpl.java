package com.blazebit.storage.rest.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.blazebit.storage.core.api.StorageProviderFactoryDataAccess;
import com.blazebit.storage.core.api.spi.StorageProviderConfigurationElement;
import com.blazebit.storage.core.api.spi.StorageProviderFactory;
import com.blazebit.storage.core.api.spi.StorageProviderMetamodel;
import com.blazebit.storage.rest.api.StorageTypesResource;
import com.blazebit.storage.rest.model.StorageTypeListElementRepresentation;
import com.blazebit.storage.rest.model.StorageTypeRepresentation;
import com.blazebit.storage.rest.model.config.StorageTypeConfigElementRepresentation;

@ApplicationScoped
public class StorageTypesResourceImpl extends AbstractResource implements StorageTypesResource {
	
	private final List<StorageProviderMetamodel> metamodelList = new ArrayList<>();
	private final Map<String, StorageProviderMetamodel> metamodelMap = new HashMap<>();
	
	@Inject
	private StorageProviderFactoryDataAccess storageProviderFactoryDataAccess;
	
	@PostConstruct
	public void init() {
		for (StorageProviderFactory factory : storageProviderFactoryDataAccess.findAll()) {
			StorageProviderMetamodel metamodel = factory.getMetamodel();
			metamodelList.add(metamodel);
			metamodelMap.put(metamodel.getScheme(), metamodel);
		}
	}
	
	@Override
	public List<StorageTypeListElementRepresentation> get() {
		List<StorageTypeListElementRepresentation> list = new ArrayList<>(metamodelList.size());
		
		for (StorageProviderMetamodel metamodel : metamodelList) {
			StorageTypeListElementRepresentation element = new StorageTypeListElementRepresentation();
			element.setKey(metamodel.getScheme());
			element.setName(metamodel.getName());
			element.setDescription(metamodel.getDescription());
			list.add(element);
		}
		
		return list;
	}

	@Override
	public StorageTypeRepresentation get(String type) {
		StorageProviderMetamodel metamodel = metamodelMap.get(type);
		
		if (metamodel == null) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN_TYPE).entity("Could not find storage type!").build());
		}

		StorageTypeRepresentation element = new StorageTypeRepresentation();
		element.setKey(metamodel.getScheme());
		element.setName(metamodel.getName());
		element.setDescription(metamodel.getDescription());
		
		Set<StorageProviderConfigurationElement> configElements = metamodel.getConfigurationElements();
		List<StorageTypeConfigElementRepresentation> storageTypeConfigs = new ArrayList<>(configElements.size());
		
		for (StorageProviderConfigurationElement config : configElements) {
			StorageTypeConfigElementRepresentation storageTypeConfig = new StorageTypeConfigElementRepresentation();
			storageTypeConfig.setKey(config.getKey());
			storageTypeConfig.setType(config.getType());
			storageTypeConfig.setValue(config.getDefaultValue());
			storageTypeConfig.setName(config.getName());
			storageTypeConfig.setDescription(config.getDescription());
			storageTypeConfigs.add(storageTypeConfig);
		}
		
		element.setConfiguration(storageTypeConfigs);
		return element;
	}

}
