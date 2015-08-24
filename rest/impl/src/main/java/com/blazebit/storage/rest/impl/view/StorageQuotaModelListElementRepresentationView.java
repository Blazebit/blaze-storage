package com.blazebit.storage.rest.impl.view;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.storage.core.model.jpa.StorageQuotaModel;
import com.blazebit.storage.rest.model.StorageQuotaModelListElementRepresentation;
import com.fasterxml.jackson.annotation.JsonIgnore;

@EntityView(StorageQuotaModel.class)
public abstract class StorageQuotaModelListElementRepresentationView extends StorageQuotaModelListElementRepresentation {

	private static final long serialVersionUID = 1L;

	public StorageQuotaModelListElementRepresentationView(
			@Mapping("id") String id,
			@Mapping("name") String name,
			@Mapping("description") String description,
			@Mapping("plans") List<StorageQuotaPlanView> plans) {
		super(name, description, toSet(plans), id);
	}
	
	@JsonIgnore
	@IdMapping("id")
	public abstract String getId();
	
	private static Set<Integer> toSet(List<StorageQuotaPlanView> plans) {
		Set<Integer> limits = new LinkedHashSet<>(plans.size());
		for (StorageQuotaPlanView plan : plans) {
			limits.add(plan.getGigabyteLimit());
		}
		return limits;
	}
}
