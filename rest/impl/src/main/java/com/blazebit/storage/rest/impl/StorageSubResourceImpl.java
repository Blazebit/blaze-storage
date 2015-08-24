package com.blazebit.storage.rest.impl;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.StorageDataAccess;
import com.blazebit.storage.core.api.StorageProviderFactoryDataAccess;
import com.blazebit.storage.core.api.StorageService;
import com.blazebit.storage.core.model.jpa.Account;
import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.jpa.StorageId;
import com.blazebit.storage.core.model.jpa.StorageQuotaModel;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlan;
import com.blazebit.storage.rest.api.AccountSubResource;
import com.blazebit.storage.rest.api.AccountsResource;
import com.blazebit.storage.rest.api.StorageSubResource;
import com.blazebit.storage.rest.api.StoragesSubResource;
import com.blazebit.storage.rest.impl.view.StorageRepresentationView;
import com.blazebit.storage.rest.model.StorageRepresentation;
import com.blazebit.storage.rest.model.StorageUpdateRepresentation;
import com.blazebit.storage.rest.model.config.StorageTypeConfigEntryRepresentation;

public class StorageSubResourceImpl extends AbstractResource implements StorageSubResource {
	
	private StorageId id;
	
	@Inject
	private StorageDataAccess storageDataAccess;
	@Inject
	private StorageService storageService;
	@Inject
	private StorageProviderFactoryDataAccess storageProviderFactoryDataAccess;

	public StorageSubResourceImpl(Account owner, String storageName) {
		this.id = new StorageId(owner, storageName);
	}

	@Override
	public StorageRepresentation get() {
		StorageRepresentation result = storageDataAccess.findById(id, EntityViewSetting.create(StorageRepresentationView.class));
		if (result == null) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("Storage not found").build());
		}
		
		return result;
	}

	@Override
	public Response put(StorageUpdateRepresentation<StorageTypeConfigEntryRepresentation> storageUpdate) {
		Storage storage = getStorageById(id);
		boolean isNew = storage == null;
		
		if (isNew) {
			storage = new Storage(id);
		}

		storage.setQuotaPlan(new StorageQuotaPlan(new StorageQuotaModel(storageUpdate.getQuotaPlan().getModelId()), storageUpdate.getQuotaPlan().getGigabyteLimit()));
		storage.setTags(storageUpdate.getTags());
		
		Map<String, String> configurationMap = new LinkedHashMap<>(storageUpdate.getConfiguration().size());
		for (StorageTypeConfigEntryRepresentation entry : storageUpdate.getConfiguration()) {
			configurationMap.put(entry.getKey(), entry.getValue());
		}
		
		storage.setUri(storageProviderFactoryDataAccess.getConfigurationUri(storageUpdate.getType(), configurationMap));
		
		if (isNew) {
			storageService.create(storage);
		} else {
			storageService.update(storage);
		}
		
		URI uri = uriInfo.getRequestUriBuilder()
			.path(AccountsResource.class, "get")
			.path(AccountSubResource.class, "getStorages")
			.path(StoragesSubResource.class, "get")
			.build(storage.getId().getOwner().getId(), storage.getId().getName());
		
		return Response.created(uri).build();
	}
	
	private Storage getStorageById(StorageId id) {
		return storageDataAccess.findById(id);
	}

}