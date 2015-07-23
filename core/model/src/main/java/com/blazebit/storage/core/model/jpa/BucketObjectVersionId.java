package com.blazebit.storage.core.model.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Embeddable
public class BucketObjectVersionId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private BucketObject bucketObject;
	private String versionUuid;
	
	public BucketObjectVersionId() {
	}

	public BucketObjectVersionId(BucketObject bucketObject, String versionUuid) {
		this.bucketObject = bucketObject;
		this.versionUuid = versionUuid;
	}

	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumns(
			foreignKey = @ForeignKey(name = RdbmsConstants.PREFIX + "bucket_object_version_fk_bucket_object"),
			value = {
		@JoinColumn(name = "bucket_id", referencedColumnName = "bucket_id"),
		@JoinColumn(name = "object_name", referencedColumnName = "name")
	})
	public BucketObject getBucketObject() {
		return bucketObject;
	}

	public void setBucketObject(BucketObject bucketObject) {
		this.bucketObject = bucketObject;
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
		result = prime * result + ((bucketObject == null) ? 0 : bucketObject.hashCode());
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
		if (bucketObject == null) {
			if (other.bucketObject != null)
				return false;
		} else if (!bucketObject.equals(other.bucketObject))
			return false;
		if (versionUuid == null) {
			if (other.versionUuid != null)
				return false;
		} else if (!versionUuid.equals(other.versionUuid))
			return false;
		return true;
	}
}
