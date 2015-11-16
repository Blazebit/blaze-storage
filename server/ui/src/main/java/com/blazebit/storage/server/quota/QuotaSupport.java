package com.blazebit.storage.server.quota;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.inject.Inject;
import javax.inject.Named;

import com.blazebit.storage.rest.client.BlazeStorage;
import com.blazebit.storage.rest.model.StorageQuotaModelListElementRepresentation;
import com.blazebit.storage.rest.model.StorageQuotaPlanChoiceRepresentation;
import com.blazebit.storage.server.faces.JsonConverter;

public class QuotaSupport {

	@Inject
	private BlazeStorage storage;
	
	@Produces
	@Named("storageQuotaPlanConverter")
	@RequestScoped
	public Converter getStorageQuotaPlanItemConverter() {
		return new JsonConverter(StorageQuotaPlanChoiceRepresentation.class);
	}
	
	@Produces
	@Named("storageQuotaPlanItems")
	@RequestScoped
	public List<SelectItem> getStorageQuotaPlanItems() {
		List<StorageQuotaModelListElementRepresentation> quotaModels = storage.storageQuotaModels().get();
		List<SelectItem> quotaModelPlanItems = new ArrayList<>(quotaModels.size());
		
		for (StorageQuotaModelListElementRepresentation quotaModel : quotaModels) {
			SelectItemGroup group = new SelectItemGroup(quotaModel.getName());
			List<SelectItem> groupItems = new ArrayList<>(quotaModel.getLimits().size());
			
			for (Integer limit : quotaModel.getLimits()) {
				groupItems.add(new SelectItem(new StorageQuotaPlanChoiceRepresentation(quotaModel.getId(), limit), limit + " GB"));
			}
			
			group.setSelectItems(groupItems.toArray(new SelectItem[groupItems.size()]));
			quotaModelPlanItems.add(group);
		}
		
		return quotaModelPlanItems;
	}
}
