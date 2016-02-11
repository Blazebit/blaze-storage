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
	private Map<String, String> tags = new TreeMap<>();
	
	public BucketTagObjectStatisticsId() {
	}

	public BucketTagObjectStatisticsId(Bucket bucket, Map<String, String> tags) {
		if (bucket != null) {
			this.bucketId = bucket.getId();
		}
		
		this.tags = tags;
	}

	public BucketTagObjectStatisticsId(String bucketId, Map<String, String> tags) {
		this.bucketId = bucketId;
		this.tags = tags;
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
		result = prime * result + ((bucketId == null) ? 0 : bucketId.hashCode());
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
		BucketTagObjectStatisticsId other = (BucketTagObjectStatisticsId) obj;
		if (bucketId == null) {
			if (other.bucketId != null)
				return false;
		} else if (!bucketId.equals(other.bucketId))
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
		return "BucketTagObjectStatisticsId [bucketId=" + bucketId + ", tags=" + tags + "]";
	}
}
