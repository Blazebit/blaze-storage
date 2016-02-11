package com.blazebit.storage.core.model.jpa;

import java.util.Map;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class BucketTagObjectStatistics extends ObjectStatistics implements IdHolder<BucketTagObjectStatisticsId> {

	private static final long serialVersionUID = 1L;
	
	private BucketTagObjectStatisticsId id;
	
	private Bucket bucket;

	public BucketTagObjectStatistics() {
		super();
	}

	public BucketTagObjectStatistics(Bucket bucket, Map<String, String> tags) {
		this.id = new BucketTagObjectStatisticsId(bucket, tags);
		this.bucket = bucket;
	}

	public BucketTagObjectStatistics(long objectCount, long objectBytes, long objectVersionCount,
			long objectVersionBytes, long pendingObjectCount, long pendingObjectBytes, long pendingObjectVersionCount,
			long pendingObjectVersionBytes, Bucket bucket, Map<String, String> tags) {
		super(objectCount, objectBytes, objectVersionCount, objectVersionBytes, pendingObjectCount, pendingObjectBytes, pendingObjectVersionCount, pendingObjectVersionBytes);
		this.id = new BucketTagObjectStatisticsId(bucket.getId(), tags);
		this.bucket = bucket;
	}

	public BucketTagObjectStatistics(long objectCount, long objectBytes, long objectVersionCount,
			long objectVersionBytes, long pendingObjectCount, long pendingObjectBytes, long pendingObjectVersionCount,
			long pendingObjectVersionBytes, BucketTagObjectStatisticsId id, Bucket bucket) {
		super(objectCount, objectBytes, objectVersionCount, objectVersionBytes, pendingObjectCount, pendingObjectBytes, pendingObjectVersionCount, pendingObjectVersionBytes);
		this.id = id;
		this.bucket = bucket;
	}

	@Override
	public BucketTagObjectStatistics copy() {
		return new BucketTagObjectStatistics(objectCount, objectBytes, objectVersionCount, objectVersionBytes, pendingObjectCount, pendingObjectBytes, pendingObjectVersionCount, pendingObjectVersionBytes, id, bucket);
	}

	@EmbeddedId
	public BucketTagObjectStatisticsId getId() {
		return id;
	}

	public void setId(BucketTagObjectStatisticsId id) {
		this.id = id;
	}

	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "bucket_id", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = RdbmsConstants.PREFIX + "bucket_tag_object_statistics_fk_bucket"))
	public Bucket getBucket() {
		return bucket;
	}

	public void setBucket(Bucket bucket) {
		this.bucket = bucket;
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
		BucketTagObjectStatistics other = (BucketTagObjectStatistics) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
