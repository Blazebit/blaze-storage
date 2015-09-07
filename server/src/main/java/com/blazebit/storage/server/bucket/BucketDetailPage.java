package com.blazebit.storage.server.bucket;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.blazebit.storage.rest.model.BucketHeadRepresentation;
import com.blazebit.storage.server.account.AccountSupport;

@Named
@RequestScoped
public class BucketDetailPage extends BucketBasePage {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private AccountSupport accountSupport;

	protected String accountName;
	
	@Override
	protected void init() {
		super.init();
		if (bucket == null) {
			this.accountName = null;
		} else {
			this.accountName = accountSupport.getAccountName(bucket.getDefaultStorageOwner());
		}
	}
	
	public BucketHeadRepresentation getBucket() {
		return (BucketHeadRepresentation) bucket;
	}

	public String getAccountName() {
		return accountName;
	}
	
}
