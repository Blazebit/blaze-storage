package com.blazebit.storage.server.quota;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.blazebit.storage.rest.model.StorageQuotaModelRepresentation;

@Named
@RequestScoped
public class QuotaDetailPage extends QuotaBasePage {

	private static final long serialVersionUID = 1L;
	
	public StorageQuotaModelRepresentation getQuota() {
		return (StorageQuotaModelRepresentation) quota;
	}
	
}
