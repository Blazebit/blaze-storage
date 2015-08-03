package com.blazebit.storage.core.model.jpa;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class Bucket extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;

	private Calendar creationDate;
	private UserAccount owner;
	private Storage storage;
	private Boolean deleted = false;
	private ObjectStatistics statistics = new ObjectStatistics();
	
	public Bucket() {
	}
	
	public Bucket(String id) {
		setId(id);
	}

	@Id
	@Size(min = 1, max = 256)
	@Pattern(regexp = "[^/]*", message = "The slash character is not allowed")
	@Override
	public String getId() {
		return id();
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_date")
	public Calendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id", foreignKey = @ForeignKey(name = RdbmsConstants.PREFIX + "bucket_fk_owner"))
	public UserAccount getOwner() {
		return owner;
	}
	
	public void setOwner(UserAccount owner) {
		this.owner = owner;
	}
	
	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumns(
			foreignKey = @ForeignKey(name = RdbmsConstants.PREFIX + "bucket_fk_storage"),
			value = {
		@JoinColumn(name = "storage_owner", referencedColumnName = "owner_id"),
		@JoinColumn(name = "storage_name", referencedColumnName = "name")
	})
	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}

	@NotNull
	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@Embedded
	public ObjectStatistics getStatistics() {
		return statistics;
	}

	public void setStatistics(ObjectStatistics statistics) {
		this.statistics = statistics;
	}
	
	@PrePersist
	private void prePersist() {
		if (creationDate == null) {
			creationDate = Calendar.getInstance();
		}
	}

}
