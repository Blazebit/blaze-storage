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
	private String tagKey;
	private String tagValue;
	
	public StorageTagObjectStatisticsId() {
	}

	public StorageTagObjectStatisticsId(Storage storage, String tagKey, String tagValue) {
		if (storage != null) {
			this.storageOwnerId = storage.getId().getOwnerId();
			this.storageName = storage.getId().getName();
		}
		
		this.tagKey = tagKey;
		this.tagValue = tagValue;
	}

	public StorageTagObjectStatisticsId(Long storageOwnerId, String storageName, String tagKey, String tagValue) {
		this.storageOwnerId = storageOwnerId;
		this.storageName = storageName;
		this.tagKey = tagKey;
		this.tagValue = tagValue;
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
	@Column(name = "tag_key", nullable = false, length = RdbmsConstants.TAG_KEY_MAX_LENGTH)
	public String getTagKey() {
		return tagKey;
	}

	public void setTagKey(String tagKey) {
		this.tagKey = tagKey;
	}

	@NotNull
	@Column(name = "tag_value", nullable = false, length = RdbmsConstants.TAG_VALUE_MAX_LENGTH)
	public String getTagValue() {
		return tagValue;
	}

	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((storageName == null) ? 0 : storageName.hashCode());
		result = prime * result + ((storageOwnerId == null) ? 0 : storageOwnerId.hashCode());
		result = prime * result + ((tagKey == null) ? 0 : tagKey.hashCode());
		result = prime * result + ((tagValue == null) ? 0 : tagValue.hashCode());
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
		if (tagKey == null) {
			if (other.tagKey != null)
				return false;
		} else if (!tagKey.equals(other.tagKey))
			return false;
		if (tagValue == null) {
			if (other.tagValue != null)
				return false;
		} else if (!tagValue.equals(other.tagValue))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StorageTagObjectStatisticsId [storageOwnerId=" + storageOwnerId + ", storageName=" + storageName
				+ ", tagKey=" + tagKey + ", tagValue=" + tagValue + "]";
	}
}
