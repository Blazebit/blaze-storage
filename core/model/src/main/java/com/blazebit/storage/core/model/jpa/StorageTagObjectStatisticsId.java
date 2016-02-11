package com.blazebit.storage.core.model.jpa;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.blazebit.storage.core.model.jpa.converter.TagMapConverter;

@Embeddable
public class StorageTagObjectStatisticsId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long storageOwnerId;
	private String storageName;
	private Map<String, String> tags = new TreeMap<>();
	
	public StorageTagObjectStatisticsId() {
	}

	public StorageTagObjectStatisticsId(Storage storage, Map<String, String> tags) {
		if (storage != null) {
			this.storageOwnerId = storage.getId().getOwnerId();
			this.storageName = storage.getId().getName();
		}
		
		this.tags = tags;
	}

	public StorageTagObjectStatisticsId(Long storageOwnerId, String storageName, Map<String, String> tags) {
		this.storageOwnerId = storageOwnerId;
		this.storageName = storageName;
		this.tags = tags;
	}

	@NotNull
	@Column(name = "storage_owner_id")
	public Long getStorageOwnerId() {
		return storageOwnerId;
	}

	public void setStorageOwnerId(Long storageOwnerId) {
		this.storageOwnerId = storageOwnerId;
	}

	@NotNull
	@Size(min = 1, max = 256)
	@Pattern(regexp = "[^/]*", message = "The slash character is not allowed")
	public String getStorageName() {
		return storageName;
	}

	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}

	@NotNull
	@Column(name = "tags", nullable = false, length = RdbmsConstants.TAGS_MAX_LENGTH)
	@Convert(converter = TagMapConverter.class)
	public Map<String, String> getTags() {
		return tags;
	}

	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((storageName == null) ? 0 : storageName.hashCode());
		result = prime * result + ((storageOwnerId == null) ? 0 : storageOwnerId.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
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
		StorageTagObjectStatisticsId other = (StorageTagObjectStatisticsId) obj;
		if (storageName == null) {
			if (other.storageName != null)
				return false;
		} else if (!storageName.equals(other.storageName))
			return false;
		if (storageOwnerId == null) {
			if (other.storageOwnerId != null)
				return false;
		} else if (!storageOwnerId.equals(other.storageOwnerId))
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StorageTagObjectStatisticsId [storageOwnerId=" + storageOwnerId + ", storageName=" + storageName
				+ ", tags=" + tags + "]";
	}
}
