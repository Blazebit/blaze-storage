package com.blazebit.storage.server.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.blazebit.storage.rest.client.BlazeStorage;
import com.blazebit.storage.rest.model.StorageListElementRepresentation;
import com.blazebit.storage.rest.model.StorageTypeListElementRepresentation;

@Named
@RequestScoped
public class StorageIndexPage {

	@Inject
	private BlazeStorage storage;

	@Inject
	@Named("storageTypes")
	private List<StorageTypeListElementRepresentation> storageTypes;

	protected String accountKey;
	protected Map<String, String> typeMap;
	
	@PostConstruct
	public void init() {
		this.typeMap = new HashMap<>(storageTypes.size());
		for (StorageTypeListElementRepresentation element : storageTypes) {
			typeMap.put(element.getKey(), element.getName());
		}
	}
	
	@Produces
	@Named("storageList")
	@RequestScoped
	public List<StorageListElementRepresentation> createAccountList() {
		return storage.accounts().get(accountKey).getStorages().get().getStorages();
	}
	
	public String getTypeName(String type) {
		return typeMap.get(type);
	}

	public String getAccountKey() {
		return accountKey;
	}

	public void setAccountKey(String accountKey) {
		this.accountKey = accountKey;
	}
}
