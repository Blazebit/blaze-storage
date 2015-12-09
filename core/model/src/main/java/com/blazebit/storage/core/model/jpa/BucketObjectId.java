package com.blazebit.storage.core.model.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class BucketObjectId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String bucketId;
	private String name;
	
	public BucketObjectId() {
	}
	
	public BucketObjectId(Bucket bucket, String name) {
		if (bucket == null) {
			this.bucketId = null;
		} else {
			this.bucketId = bucket.getId();
		}
		this.name = name;
	}

	public BucketObjectId(String bucketId, String name) {
		this.bucketId = bucketId;
		this.name = name;
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
	@Size(min = 1, max = RdbmsConstants.FILE_NAME_MAX_LENGTH)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bucketId == null) ? 0 : bucketId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		BucketObjectId other = (BucketObjectId) obj;
		if (bucketId == null) {
			if (other.bucketId != null)
				return false;
		} else if (!bucketId.equals(other.bucketId))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BucketObjectId [bucketId=" + bucketId + ", name=" + name + "]";
	}
}
