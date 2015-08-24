package com.blazebit.storage.rest.impl.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.storage.core.model.jpa.StorageQuotaPlan;
import com.blazebit.storage.rest.model.StorageQuotaPlanChoiceRepresentation;
import com.fasterxml.jackson.annotation.JsonIgnore;

@EntityView(StorageQuotaPlan.class)
public abstract class StorageQuotaPlanChoiceRepresentationView extends StorageQuotaPlanChoiceRepresentation {

	private static final long serialVersionUID = 1L;

	public StorageQuotaPlanChoiceRepresentationView(
			@Mapping("quotaModel.id") String modelId,
			@Mapping("gigabyteLimit") Integer gigabyteLimit) {
		super(modelId, gigabyteLimit);
	}

	@JsonIgnore
	@IdMapping("id")
	public abstract Long getId();
}
