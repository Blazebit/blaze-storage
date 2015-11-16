package com.blazebit.storage.server.quota;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.blazebit.storage.rest.client.BlazeStorage;
import com.blazebit.storage.rest.model.StorageQuotaModelListElementRepresentation;

@Named
@RequestScoped
public class QuotaIndexPage {

	@Inject
	private BlazeStorage client;
	
	@Produces
	@Named("quotaList")
	@RequestScoped
	public List<StorageQuotaModelListElementRepresentation> createAccountList() {
		return client.storageQuotaModels().get();
	}
}
