package com.blazebit.storage.server.quota;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.blazebit.storage.rest.client.BlazeStorage;
import com.blazebit.storage.rest.model.StorageQuotaModelUpdateRepresentation;
import com.blazebit.storage.server.faces.limit.LimitEntry;
import com.blazebit.storage.server.faces.limit.LimitsHolder;

public class QuotaBasePage implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(QuotaBasePage.class.getName());

	@Inject
	protected BlazeStorage client;
	@Inject
	protected FacesContext facesContext;
	
	protected String id;
	protected LimitsHolder limitsHolder = new LimitsHolder();
	protected StorageQuotaModelUpdateRepresentation quota = new StorageQuotaModelUpdateRepresentation();
	
	public String viewAction() {
		try {
			if (id != null || id.isEmpty()) {
				quota = client.storageQuotaModels().get(id).get();
				if (quota == null) {
					limitsHolder.setLimitEntries(new ArrayList<LimitEntry>());
					facesContext.addMessage(null, new FacesMessage("No storage quota model found for id " + id));
					return null;
				} else {
					limitsHolder.setLimits(quota.getLimits());
					return "";
				}
			}

			facesContext.addMessage(null, new FacesMessage("Invalid empty id!"));
			return null;
		} catch (RuntimeException ex) {
			facesContext.addMessage(null, new FacesMessage("Could not load storage quota model"));
			LOG.log(Level.SEVERE, "Could not load storage quota model " + id, ex);
			return null;
		}
	}
	
	protected void put() {
		StorageQuotaModelUpdateRepresentation newQuota = new StorageQuotaModelUpdateRepresentation();
		newQuota.setName(quota.getName());
		newQuota.setDescription(quota.getDescription());
		newQuota.setLimits(limitsHolder.getLimits());
		client.storageQuotaModels().get(id).put(newQuota);
		quota = newQuota;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public StorageQuotaModelUpdateRepresentation getQuota() {
		return quota;
	}

	public LimitsHolder getLimitsHolder() {
		return limitsHolder;
	}
}
