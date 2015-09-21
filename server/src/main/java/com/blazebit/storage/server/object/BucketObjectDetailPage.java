package com.blazebit.storage.server.object;

import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.blazebit.storage.rest.model.BucketHeadRepresentation;
import com.blazebit.storage.server.account.AccountSupport;

@Named
@RequestScoped
public class BucketObjectDetailPage extends BucketObjectBasePage {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(BucketObjectDetailPage.class.getName());
	
	@Inject
	private AccountSupport accountSupport;

	private String accountName;
	
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
