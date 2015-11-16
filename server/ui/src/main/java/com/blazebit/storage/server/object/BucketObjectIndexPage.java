package com.blazebit.storage.server.object;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.blazebit.storage.rest.client.BlazeStorage;
import com.blazebit.storage.rest.model.BucketRepresentation;
import com.blazebit.storage.server.account.AccountSupport;

@Named
@RequestScoped
public class BucketObjectIndexPage {

	@Inject
	private BlazeStorage client;
	
	@Inject
	private AccountSupport accountSupport;
	
	private String bucket;
	private String parent;
	private String account;
	
	@Produces
	@Named("bucketObjectResult")
	@RequestScoped
	public BucketRepresentation createBucketObjectList() {
		return client.buckets().get(bucket).get(parent, null, null);
	}
	
	public String getAccountName(String ownerKey) {
		return accountSupport.getAccountName(ownerKey);
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
}
