package com.blazebit.storage.core.model.jpa;

import java.util.Calendar;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Bucket extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;

	private Calendar creationDate;
	private UserAccount owner;
	private Storage storage;
	private BucketStatistics statistics = new BucketStatistics();
	
	public Bucket() {
	}
	
	public Bucket(String id) {
		setId(id);
	}

	@Override
	public String getId() {
		return id();
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	public UserAccount getOwner() {
		return owner;
	}
	
	public void setOwner(UserAccount owner) {
		this.owner = owner;
	}
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "storage_owner", referencedColumnName = "owner_id"),
		@JoinColumn(name = "storage_name", referencedColumnName = "name")
	})
	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}

	@Embedded
	public BucketStatistics getStatistics() {
		return statistics;
	}

	public void setStatistics(BucketStatistics statistics) {
		this.statistics = statistics;
	}
	
	@PrePersist
	private void prePersist() {
		if (creationDate == null) {
			creationDate = Calendar.getInstance();
		}
	}

}
