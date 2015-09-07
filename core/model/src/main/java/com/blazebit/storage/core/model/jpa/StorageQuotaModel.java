package com.blazebit.storage.core.model.jpa;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class StorageQuotaModel extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String description;
	private Set<StorageQuotaPlan> plans = new TreeSet<>();

	public StorageQuotaModel() {
		super(null);
	}
	
	public StorageQuotaModel(String id) {
		super(id);
	}
	
	@Id
	@Override
	public String getId() {
		return id();
	}

	@NotNull
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotNull
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(mappedBy = "id.quotaModel", cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<StorageQuotaPlan> getPlans() {
		return plans;
	}

	public void setPlans(Set<StorageQuotaPlan> plans) {
		this.plans = plans;
	}

	
}
