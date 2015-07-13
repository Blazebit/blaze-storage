package com.blazebit.storage.core.model.jpa;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Embeddable
public class BucketObjectId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Bucket bucket;
	private String name;
	
	public BucketObjectId() {
	}
	
	public BucketObjectId(Bucket bucket, String name) {
		this.bucket = bucket;
		this.name = name;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	public Bucket getBucket() {
		return bucket;
	}
	
	public void setBucket(Bucket bucket) {
		this.bucket = bucket;
	}
	
	@Basic(optional = false)
	@Column(length = RdbmsConstants.FILE_NAME_MAX_LENGTH)
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
		result = prime * result + ((bucket == null) ? 0 : bucket.hashCode());
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
		if (bucket == null) {
			if (other.bucket != null)
				return false;
		} else if (!bucket.equals(other.bucket))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
