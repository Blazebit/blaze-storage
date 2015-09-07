package com.blazebit.storage.core.model.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Embeddable
public class StorageQuotaPlanId implements Serializable {

	private static final long serialVersionUID = 1L;

	private StorageQuotaModel quotaModel;
	private Integer gigabyteLimit;
	
	public StorageQuotaPlanId() {
	}

	public StorageQuotaPlanId(StorageQuotaModel quotaModel, Integer gigabyteLimit) {
		this.quotaModel = quotaModel;
		this.gigabyteLimit = gigabyteLimit;
	}

	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "quota_model_id", foreignKey = @ForeignKey(name = RdbmsConstants.PREFIX + "storage_quota_plan_fk_quota_model"))
	public StorageQuotaModel getQuotaModel() {
		return quotaModel;
	}

	public void setQuotaModel(StorageQuotaModel quotaModel) {
		this.quotaModel = quotaModel;
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
		result = prime * result + ((quotaModel == null) ? 0 : quotaModel.hashCode());
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
		if (quotaModel == null) {
			if (other.quotaModel != null)
				return false;
		} else if (!quotaModel.equals(other.quotaModel))
			return false;
		return true;
	}
}
