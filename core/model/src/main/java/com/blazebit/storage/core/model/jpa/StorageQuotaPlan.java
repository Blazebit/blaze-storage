package com.blazebit.storage.core.model.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = RdbmsConstants.PREFIX + "storage_quota_plan_uc_quota_model_gigabyte_limit", columnNames = {"quota_model_id", "gigabyte_limit"}))
@SequenceGenerator(name = "idGenerator", sequenceName = RdbmsConstants.PREFIX + "storage_quota_plan")
public class StorageQuotaPlan extends SequenceBaseEntity {

	private static final long serialVersionUID = 1L;

	private StorageQuotaModel quotaModel;
	private Integer gigabyteLimit;
	private Short alertPercent;

	public StorageQuotaPlan() {
		super();
	}

	public StorageQuotaPlan(Long id) {
		super(id);
	}

	public StorageQuotaPlan(StorageQuotaModel quotaModel, Integer gigabyteLimit) {
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

	@Min(0)
	@NotNull
	@Column(name = "gigabyte_limit")
	public Integer getGigabyteLimit() {
		return gigabyteLimit;
	}

	public void setGigabyteLimit(Integer gigabyteLimit) {
		this.gigabyteLimit = gigabyteLimit;
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
	
}
