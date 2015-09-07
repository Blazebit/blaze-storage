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
	
	@Produces
	@Named("bucketList")
	@RequestScoped
	public List<BucketListElementRepresentation> createBucketList() {
		return client.buckets().get();
	}
	
	public String getAccountName(String ownerKey) {
		return accountSupport.getAccountName(ownerKey);
	}
}
