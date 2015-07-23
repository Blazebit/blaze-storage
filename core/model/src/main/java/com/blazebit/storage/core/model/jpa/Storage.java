package com.blazebit.storage.core.model.jpa;

import java.net.URI;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.blazebit.storage.core.model.jpa.converter.URIConverter;

@Entity
public class Storage extends EmbeddedIdBaseEntity<StorageId> {

	private static final long serialVersionUID = 1L;
	
	private URI uri;
	private Calendar creationDate;
	private StorageQuotaPlan quotaPlan;
	private ObjectStatistics statistics = new ObjectStatistics();

	public Storage() {
		super(new StorageId());
	}
	
	public Storage(StorageId id) {
		super(id);
	}

	@NotNull
	@Convert(converter = URIConverter.class)
	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
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
	@JoinColumn(name = "quota_plan_id", foreignKey = @ForeignKey(name = RdbmsConstants.PREFIX + "storage_fk_quota_plan"))
	public StorageQuotaPlan getQuotaPlan() {
		return quotaPlan;
	}

	public void setQuotaPlan(StorageQuotaPlan quotaPlan) {
		this.quotaPlan = quotaPlan;
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
