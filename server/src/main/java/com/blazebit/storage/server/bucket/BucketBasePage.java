package com.blazebit.storage.server.bucket;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.blazebit.storage.rest.client.BlazeStorage;
import com.blazebit.storage.rest.model.BucketUpdateRepresentation;
import com.blazebit.storage.server.storage.StorageBasePage;

public class BucketBasePage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(StorageBasePage.class.getName());

	@Inject
	protected BlazeStorage client;
	@Inject
	protected FacesContext facesContext;

	protected String account;
	protected String name;
	protected BucketUpdateRepresentation bucket = new BucketUpdateRepresentation();

	public String viewAction() {
		try {
			if (name != null || name.isEmpty()) {
				bucket = client.buckets().get(name).head();
				if (bucket == null) {
					facesContext.addMessage(null, new FacesMessage("No bucket found for name " + name));
					init();
					return null;
				} else {
					init();
					return "";
				}
			}

			facesContext.addMessage(null, new FacesMessage("Invalid empty name!"));
			return null;
		} catch (RuntimeException ex) {
			facesContext.addMessage(null, new FacesMessage("Could not load bucket"));
			LOG.log(Level.SEVERE, "Could not load bucket " + name, ex);
			return null;
		}
	}
	
	protected void init() {
		
	}
	
	public void put() {
		BucketUpdateRepresentation newBucket = new BucketUpdateRepresentation();
		newBucket.setDefaultStorageOwner(bucket.getDefaultStorageOwner());
		newBucket.setDefaultStorageName(bucket.getDefaultStorageName());
		client.buckets().get(name).put(newBucket, account);
		bucket = newBucket;
	}
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BucketUpdateRepresentation getBucket() {
		return bucket;
	}
}
