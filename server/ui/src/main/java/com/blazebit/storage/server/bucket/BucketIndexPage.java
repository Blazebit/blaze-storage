package com.blazebit.storage.server.bucket;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.blazebit.storage.rest.client.BlazeStorage;
import com.blazebit.storage.rest.model.BucketListElementRepresentation;
import com.blazebit.storage.server.account.AccountSupport;

@Named
@RequestScoped
public class BucketIndexPage {

	@Inject
	private BlazeStorage client;
	
	@Inject
	private AccountSupport accountSupport;
	
	private boolean admin;
	private String account;
	
	@Produces
	@Named("bucketList")
	@RequestScoped
	public List<BucketListElementRepresentation> createBucketList() {
		if (admin) {
			return client.admin().getBuckets();
		} else {
			if (account != null) {
				return client.accounts().get(account).getBuckets();
			} else {
				return client.buckets().get();
			}
		}
	}
	
	public String getAccountName(String ownerKey) {
		return accountSupport.getAccountName(ownerKey);
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
}
