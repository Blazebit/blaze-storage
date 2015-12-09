package com.blazebit.storage.core.model.jpa;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class StorageQuotaPlan extends BaseEntity<StorageQuotaPlanId> implements Comparable<StorageQuotaPlan> {

	private static final long serialVersionUID = 1L;

	private StorageQuotaModel quotaModel;
	private Short alertPercent;

	public StorageQuotaPlan() {
		super(new StorageQuotaPlanId());
	}

	public StorageQuotaPlan(StorageQuotaPlanId id) {
		super(id);
	}
	
	@EmbeddedId
	@Override
	public StorageQuotaPlanId getId() {
		return id();
	}

	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "quota_model_id", foreignKey = @ForeignKey(name = RdbmsConstants.PREFIX + "storage_quota_plan_fk_quota_model"), insertable = false, updatable = false)
	public StorageQuotaModel getQuotaModel() {
		return quotaModel;
	}

	public void setQuotaModel(StorageQuotaModel quotaModel) {
		this.quotaModel = quotaModel;
	}

	@Min(0)
	@Max(100)
	@NotNull
	@Column(name = "alert_percent")
	public Short getAlertPercent() {
		return alertPercent;
	}

	public void setAlertPercent(Short alertPercent) {
		this.alertPercent = alertPercent;
	}

	@Override
	public int compareTo(StorageQuotaPlan o) {
		int cmp = getId().getQuotaModelId().compareTo(o.getId().getQuotaModelId());
		if (cmp != 0) {
			return cmp;
		}
		
		return getId().getGigabyteLimit().compareTo(o.getId().getGigabyteLimit());
	}
	
}
