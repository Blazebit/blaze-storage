package com.blazebit.storage.core.model.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Embeddable
public class StorageQuotaPlanId implements Serializable {

	private static final long serialVersionUID = 1L;

	private String quotaModelId;
	private Integer gigabyteLimit;
	
	public StorageQuotaPlanId() {
	}

	public StorageQuotaPlanId(String quotaModelId, Integer gigabyteLimit) {
		this.quotaModelId = quotaModelId;
		this.gigabyteLimit = gigabyteLimit;
	}

	@NotNull
	@Column(name = "quota_model_id")
	public String getQuotaModelId() {
		return quotaModelId;
	}

	public void setQuotaModelId(String quotaModelId) {
		this.quotaModelId = quotaModelId;
	}

	@Min(1)
	@NotNull
	@Column(name = "gigabyte_limit")
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
		result = prime * result + ((quotaModelId == null) ? 0 : quotaModelId.hashCode());
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
		StorageQuotaPlanId other = (StorageQuotaPlanId) obj;
		if (gigabyteLimit == null) {
			if (other.gigabyteLimit != null)
				return false;
		} else if (!gigabyteLimit.equals(other.gigabyteLimit))
			return false;
		if (quotaModelId == null) {
			if (other.quotaModelId != null)
				return false;
		} else if (!quotaModelId.equals(other.quotaModelId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StorageQuotaPlanId [quotaModelId=" + quotaModelId + ", gigabyteLimit=" + gigabyteLimit + "]";
	}
}
