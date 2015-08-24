package com.blazebit.storage.server.storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.blazebit.storage.rest.client.BlazeStorage;
import com.blazebit.storage.rest.model.StorageUpdateRepresentation;
import com.blazebit.storage.rest.model.config.StorageTypeConfigEntryRepresentation;
import com.blazebit.storage.server.faces.configuration.ConfigurationEntry;
import com.blazebit.storage.server.faces.configuration.ConfigurationHolder;
import com.blazebit.storage.server.faces.tag.TagEntry;
import com.blazebit.storage.server.faces.tag.TagsHolder;

public class StorageBasePage implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(StorageBasePage.class.getName());

	@Inject
	protected BlazeStorage client;
	@Inject
	protected FacesContext facesContext;

	protected String accountKey;
	protected String name;
	protected TagsHolder tagsHolder = new TagsHolder();
	protected ConfigurationHolder configurationHolder = new ConfigurationHolder();
	protected StorageUpdateRepresentation<? extends StorageTypeConfigEntryRepresentation> storage = new StorageUpdateRepresentation<StorageTypeConfigEntryRepresentation>();
	
	public String viewAction() {
		try {
			if (name != null || name.isEmpty()) {
				storage = client.accounts().get(accountKey).getStorages().get(name).get();
				if (storage == null) {
					tagsHolder.setTagEntries(new ArrayList<TagEntry>());
					configurationHolder.setConfigurationEntries(new ArrayList<ConfigurationEntry>());
					facesContext.addMessage(null, new FacesMessage("No storage found for name " + name));
					init();
					return null;
				} else {
					tagsHolder.setTags(storage.getTags());
					configurationHolder.setConfiguration(storage.getConfiguration());
					init();
					return "";
				}
			}

			facesContext.addMessage(null, new FacesMessage("Invalid empty name!"));
			return null;
		} catch (RuntimeException ex) {
			facesContext.addMessage(null, new FacesMessage("Could not load storage"));
			LOG.log(Level.SEVERE, "Could not load storage " + name, ex);
			return null;
		}
	}
	
	protected void init() {
		
	}
	
	public void onTypeChanged() {
		configurationHolder.setConfiguration(client.storageTypes().get(storage.getType()).getConfiguration());
	}
	
	protected void put() {
		StorageUpdateRepresentation<StorageTypeConfigEntryRepresentation> newStorage = new StorageUpdateRepresentation<StorageTypeConfigEntryRepresentation>();
		newStorage.setType(storage.getType());
		newStorage.setQuotaPlan(storage.getQuotaPlan());
		newStorage.setTags(tagsHolder.getTags());
		newStorage.setConfiguration(configurationHolder.getConfiguration());
		client.accounts().get(accountKey).getStorages().get(name).put(newStorage);
		storage = newStorage;
	}

	public String getAccountKey() {
		return accountKey;
	}

	public void setAccountKey(String accountKey) {
		this.accountKey = accountKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StorageUpdateRepresentation<? extends StorageTypeConfigEntryRepresentation> getStorage() {
		return storage;
	}

	public TagsHolder getTagsHolder() {
		return tagsHolder;
	}

	public ConfigurationHolder getConfigurationHolder() {
		return configurationHolder;
	}

}
