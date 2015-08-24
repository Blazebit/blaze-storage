package com.blazebit.storage.rest.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.StorageQuotaModelDataAccess;
import com.blazebit.storage.core.api.StorageQuotaModelService;
import com.blazebit.storage.core.model.jpa.StorageQuotaModel;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlan;
import com.blazebit.storage.rest.api.StorageQuotaModelSubResource;
import com.blazebit.storage.rest.api.StorageQuotaModelsResource;
import com.blazebit.storage.rest.impl.view.StorageQuotaModelRepresentationView;
import com.blazebit.storage.rest.model.StorageQuotaModelRepresentation;
import com.blazebit.storage.rest.model.StorageQuotaModelUpdateRepresentation;

public class StorageQuotaModelSubResourceImpl extends AbstractResource implements StorageQuotaModelSubResource {
	
	private String id;
	
	@Inject
	private StorageQuotaModelService storageQuotaModelService;
	@Inject
	private StorageQuotaModelDataAccess storageQuotaModelDataAccess;

	public StorageQuotaModelSubResourceImpl(String id) {
		this.id = id;
	}

	@Override
	public StorageQuotaModelRepresentation get() {
		StorageQuotaModelRepresentation result = storageQuotaModelDataAccess.findById(id, EntityViewSetting.create(StorageQuotaModelRepresentationView.class));
		if (result == null) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("Storage quota model not found").build());
		}
		
		return result;
	}

	@Override
	public Response put(StorageQuotaModelUpdateRepresentation quotaModelUpdate) {
		StorageQuotaModel storageQuotaModel = getById(id);
		boolean isNew = storageQuotaModel == null;
		
		if (isNew) {
			storageQuotaModel = new StorageQuotaModel(id);
		}
		
		storageQuotaModel.setName(quotaModelUpdate.getName());
		storageQuotaModel.setDescription(quotaModelUpdate.getDescription());
		storageQuotaModel.setPlans(getPlans(storageQuotaModel, quotaModelUpdate.getLimits()));
		
		if (isNew) {
			storageQuotaModelService.create(storageQuotaModel);
		} else {
			storageQuotaModelService.update(storageQuotaModel);
		}
		
		URI uri = uriInfo.getRequestUriBuilder()
			.path(StorageQuotaModelsResource.class, "get")
			.build(storageQuotaModel.getId());
		return Response.created(uri).build();
	}
	
	private List<StorageQuotaPlan> getPlans(StorageQuotaModel storageQuotaModel, Set<Integer> limits) {
		List<StorageQuotaPlan> plans = new ArrayList<>(limits.size());
		for (Integer limit : limits) {
			StorageQuotaPlan plan = new StorageQuotaPlan();
			plan.setQuotaModel(storageQuotaModel);
			plan.setGigabyteLimit(limit);
			// TODO: We might need to allow setting that in the future
			plan.setAlertPercent((short) 100);
			plans.add(plan);
		}
		return plans;
	}

	private StorageQuotaModel getById(String id) {
		return storageQuotaModelDataAccess.findById(id);
	}

}
