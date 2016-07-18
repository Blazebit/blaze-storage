package com.blazebit.storage.core.model.jpa;

import java.util.Map;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@AttributeOverrides({
	@AttributeOverride(name = "id.ownerId", column = @Column(name = "storage_owner_id")),
	@AttributeOverride(name = "id.name", column = @Column(name = "storage_name"))
})
public class StorageTagObjectStatistics extends ObjectStatistics implements IdHolder<StorageTagObjectStatisticsId> {

	private static final long serialVersionUID = 1L;

	private StorageTagObjectStatisticsId id;
	
	private Storage storage;

	public StorageTagObjectStatistics() {
		super();
	}

	public StorageTagObjectStatistics(Storage storage, String tagKey, String tagValue) {
		this.id = new StorageTagObjectStatisticsId(storage, tagKey, tagValue); 
		this.storage = storage;
	}

	public StorageTagObjectStatistics(long objectCount, long objectBytes, long objectVersionCount,
			long objectVersionBytes, long pendingObjectCount, long pendingObjectBytes, long pendingObjectVersionCount,
			long pendingObjectVersionBytes, Storage storage, String tagKey, String tagValue) {
		super(objectCount, objectBytes, objectVersionCount, objectVersionBytes, pendingObjectCount, pendingObjectBytes, pendingObjectVersionCount, pendingObjectVersionBytes);
		this.id = new StorageTagObjectStatisticsId(storage, tagKey, tagValue); 
		this.storage = storage;
	}

	public StorageTagObjectStatistics(long objectCount, long objectBytes, long objectVersionCount,
			long objectVersionBytes, long pendingObjectCount, long pendingObjectBytes, long pendingObjectVersionCount,
			long pendingObjectVersionBytes, StorageTagObjectStatisticsId id, Storage storage) {
		super(objectCount, objectBytes, objectVersionCount, objectVersionBytes, pendingObjectCount, pendingObjectBytes, pendingObjectVersionCount, pendingObjectVersionBytes);
		this.id = id; 
		this.storage = storage;
	}

	@Override
	public StorageTagObjectStatistics copy() {
		return new StorageTagObjectStatistics(objectCount, objectBytes, objectVersionCount, objectVersionBytes, pendingObjectCount, pendingObjectBytes, pendingObjectVersionCount, pendingObjectVersionBytes, id, storage);
	}

	@EmbeddedId
	public StorageTagObjectStatisticsId getId() {
		return id;
	}

	public void setId(StorageTagObjectStatisticsId id) {
		this.id = id;
	}

	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumns(value = {
		@JoinColumn(name = "storage_owner_id", referencedColumnName = "owner_id", insertable = false, updatable = false),
		@JoinColumn(name = "storage_name", referencedColumnName = "name", insertable = false, updatable = false)
	}, foreignKey = @ForeignKey(name = RdbmsConstants.PREFIX + "storage_tag_object_statistics_fk_storage"))
	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		StorageTagObjectStatistics other = (StorageTagObjectStatistics) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
