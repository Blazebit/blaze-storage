package com.blazebit.storage.core.model.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class BucketObjectVersionId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String bucketId;
	private String bucketObjectName;
	private String versionUuid;
	
	public BucketObjectVersionId() {
	}

	public BucketObjectVersionId(BucketObject bucketObject, String versionUuid) {
		if (bucketObject == null) {
			this.bucketId = null;
			this.bucketObjectName = null;
		} else {
			this.bucketId = bucketObject.getId().getBucketId();
			this.bucketObjectName = bucketObject.getId().getName();
		}
		
		this.versionUuid = versionUuid;
	}

	public BucketObjectVersionId(String bucketId, String bucketObjectName, String versionUuid) {
		this.bucketId = bucketId;
		this.bucketObjectName = bucketObjectName;
		this.versionUuid = versionUuid;
	}
	
	public BucketObjectId toBucketObjectId() {
		return new BucketObjectId(bucketId, bucketObjectName);
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
	@Column(name = "object_name")
	public String getBucketObjectName() {
		return bucketObjectName;
	}

	public void setBucketObjectName(String bucketObjectName) {
		this.bucketObjectName = bucketObjectName;
	}

	@NotNull
	@Column(name = "version_uuid")
	public String getVersionUuid() {
		return versionUuid;
	}

	public void setVersionUuid(String versionUuid) {
		this.versionUuid = versionUuid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bucketId == null) ? 0 : bucketId.hashCode());
		result = prime * result + ((bucketObjectName == null) ? 0 : bucketObjectName.hashCode());
		result = prime * result + ((versionUuid == null) ? 0 : versionUuid.hashCode());
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
		BucketObjectVersionId other = (BucketObjectVersionId) obj;
		if (bucketId == null) {
			if (other.bucketId != null)
				return false;
		} else if (!bucketId.equals(other.bucketId))
			return false;
		if (bucketObjectName == null) {
			if (other.bucketObjectName != null)
				return false;
		} else if (!bucketObjectName.equals(other.bucketObjectName))
			return false;
		if (versionUuid == null) {
			if (other.versionUuid != null)
				return false;
		} else if (!versionUuid.equals(other.versionUuid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BucketObjectVersionId [bucketId=" + bucketId + ", bucketObjectName=" + bucketObjectName
				+ ", versionUuid=" + versionUuid + "]";
	}
}
