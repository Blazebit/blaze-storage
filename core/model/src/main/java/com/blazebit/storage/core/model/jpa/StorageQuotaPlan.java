package com.blazebit.storage.core.model.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class StorageQuotaPlan extends EmbeddedIdBaseEntity<StorageQuotaPlanId> implements Comparable<StorageQuotaPlan> {

	private static final long serialVersionUID = 1L;

	private Short alertPercent;

	public StorageQuotaPlan() {
		super(new StorageQuotaPlanId());
	}

	public StorageQuotaPlan(StorageQuotaPlanId id) {
		super(id);
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
		int cmp = getId().getQuotaModel().getId().compareTo(o.getId().getQuotaModel().getId());
		if (cmp != 0) {
			return cmp;
		}
		
		return getId().getGigabyteLimit().compareTo(o.getId().getGigabyteLimit());
	}
	
}
