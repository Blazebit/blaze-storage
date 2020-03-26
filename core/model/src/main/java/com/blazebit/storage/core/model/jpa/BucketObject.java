package com.blazebit.storage.core.model.jpa;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class BucketObject extends BaseEntity<BucketObjectId> {

	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULT_CONTENT_TYPE = "binary/octet-stream";

	private Bucket bucket;
	private BucketObjectState state;
	private String contentVersionUuid;
	private BucketObjectVersion contentVersion;
	private Set<BucketObjectVersion> versions = new HashSet<>(0);
	
	public BucketObject() {
		super(new BucketObjectId());
	}
	
	public BucketObject(BucketObjectId id) {
		super(id);
	}
	
	@EmbeddedId
	@Override
	public BucketObjectId getId() {
		return id();
	}
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "bucket_id", foreignKey = @ForeignKey(name = RdbmsConstants.PREFIX + "bucket_object_fk_bucket"), insertable = false, updatable = false)
	public Bucket getBucket() {
		return bucket;
	}
	
	public void setBucket(Bucket bucket) {
		this.bucket = bucket;
	}

	@NotNull
	@Enumerated(EnumType.ORDINAL)
	public BucketObjectState getState() {
		return state;
	}

	public void setState(BucketObjectState state) {
		this.state = state;
	}
	
	@Column(name = "content_version_uuid")
	public String getContentVersionUuid() {
		return contentVersionUuid;
	}

	public void setContentVersionUuid(String contentVersionUuid) {
		this.contentVersionUuid = contentVersionUuid;
	}

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumns(
			foreignKey = @ForeignKey(name = RdbmsConstants.PREFIX + "bucket_object_fk_content_version"),
			value = {
		@JoinColumn(name = "bucket_id", referencedColumnName = "bucket_id", insertable = false, updatable = false),
		@JoinColumn(name = "name", referencedColumnName = "object_name", insertable = false, updatable = false),
		@JoinColumn(name = "content_version_uuid", referencedColumnName = "version_uuid", insertable = false, updatable = false)
	})
	public BucketObjectVersion getContentVersion() {
		return contentVersion;
	}

	public void setContentVersion(BucketObjectVersion contentVersion) {
		this.contentVersion = contentVersion;
	}

	@OneToMany(mappedBy = "bucketObject")
	public Set<BucketObjectVersion> getVersions() {
		return versions;
	}

	public void setVersions(Set<BucketObjectVersion> versions) {
		this.versions = versions;
	}

	@Override
	public String toString() {
		return "BucketObject [getId()=" + getId() + "]";
	}
}
