package com.blazebit.storage.server.storage;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import com.blazebit.storage.rest.client.BlazeStorage;
import com.blazebit.storage.rest.model.StorageTypeListElementRepresentation;

public class StorageSupport {

	@Inject
	private BlazeStorage client;
	
	@Produces
	@Named("storageTypes")
	@RequestScoped
	public List<StorageTypeListElementRepresentation> getStorageTypes() {
		return client.storageTypes().get();
	}
	
	@Produces
	@Named("storageTypeItems")
	@RequestScoped
	public List<SelectItem> getStorageTypeItems(@Named("storageTypes") List<StorageTypeListElementRepresentation> storageTypes) {
		List<SelectItem> storageTypeItems = new ArrayList<>(storageTypes.size());
		
		for (StorageTypeListElementRepresentation storageType : storageTypes) {
			storageTypeItems.add(new SelectItem(storageType.getKey(), storageType.getName(), storageType.getDescription()));
		}
		
		return storageTypeItems;
	}
}
