package com.blazebit.storage.rest.model;

import java.io.Serializable;

public class StorageQuotaPlanChoiceRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

	private String modelId;
	private Integer gigabyteLimit;

	public StorageQuotaPlanChoiceRepresentation() {
	}

	public StorageQuotaPlanChoiceRepresentation(String modelId, Integer gigabyteLimit) {
		this.modelId = modelId;
		this.gigabyteLimit = gigabyteLimit;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public Integer getGigabyteLimit() {
		return gigabyteLimit;
	}

	public void setGigabyteLimit(Integer gigabyteLimit) {
		this.gigabyteLimit = gigabyteLimit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gigabyteLimit == null) ? 0 : gigabyteLimit.hashCode());
		result = prime * result + ((modelId == null) ? 0 : modelId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StorageQuotaPlanChoiceRepresentation other = (StorageQuotaPlanChoiceRepresentation) obj;
		if (gigabyteLimit == null) {
			if (other.gigabyteLimit != null)
				return false;
		} else if (!gigabyteLimit.equals(other.gigabyteLimit))
			return false;
		if (modelId == null) {
			if (other.modelId != null)
				return false;
		} else if (!modelId.equals(other.modelId))
			return false;
		return true;
	}
}
