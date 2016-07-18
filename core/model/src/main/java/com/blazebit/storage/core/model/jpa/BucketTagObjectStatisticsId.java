package com.blazebit.storage.core.model.jpa;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import com.blazebit.storage.core.model.jpa.converter.TagMapConverter;

@Embeddable
public class BucketTagObjectStatisticsId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String bucketId;
	private String tagKey;
	private String tagValue;
	
	public BucketTagObjectStatisticsId() {
	}

	public BucketTagObjectStatisticsId(Bucket bucket, String tagKey, String tagValue) {
		if (bucket != null) {
			this.bucketId = bucket.getId();
		}

		this.tagKey = tagKey;
		this.tagValue = tagValue;
	}

	public BucketTagObjectStatisticsId(String bucketId, String tagKey, String tagValue) {
		this.bucketId = bucketId;
		this.tagKey = tagKey;
		this.tagValue = tagValue;
	}

	@NotNull
	@Column(name = "bucket_id")
	public String getBucketId() {
		return bucketId;
	}

	public void setBucketId(String bucketId) {
		this.bucketId = bucketId;
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
		result = prime * result + ((bucketId == null) ? 0 : bucketId.hashCode());
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
		BucketTagObjectStatisticsId other = (BucketTagObjectStatisticsId) obj;
		if (bucketId == null) {
			if (other.bucketId != null)
				return false;
		} else if (!bucketId.equals(other.bucketId))
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
		return "BucketTagObjectStatisticsId [bucketId=" + bucketId + ", tagKey=" + tagKey + ", tagValue=" + tagValue
				+ "]";
	}
}
