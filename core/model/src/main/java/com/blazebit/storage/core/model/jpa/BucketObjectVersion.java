package com.blazebit.storage.core.model.jpa;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
public class BucketObjectVersion extends BaseEntity<BucketObjectVersionId> {
	
	private static final long serialVersionUID = 1L;

	public static final String DEFAULT_CONTENT_TYPE = "binary/octet-stream";

	private BucketObjectState state;
	private Calendar creationDate;
	private BucketObject bucketObject;
	private Storage storage;
	
	private String contentKey;
	private long contentLength;
	private String contentType = DEFAULT_CONTENT_TYPE;
	private String contentMD5;
	private String contentDisposition;
	private String entityTag;
	private Long lastModified;
	private Map<String, String> tags = new HashMap<String, String>();

	public BucketObjectVersion() {
		super(new BucketObjectVersionId());
	}

	public BucketObjectVersion(BucketObjectVersionId id) {
		super(id);
	}
	
	@EmbeddedId
	@Override
	public BucketObjectVersionId getId() {
		return id();
	}

	@NotNull
	@Enumerated(EnumType.ORDINAL)
	public BucketObjectState getState() {
		return state;
	}

	public void setState(BucketObjectState state) {
		this.state = state;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creation_date")
	public Calendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumns(
			foreignKey = @ForeignKey(name = RdbmsConstants.PREFIX + "bucket_object_version_fk_bucket_object"),
			value = {
		@JoinColumn(name = "bucket_id", referencedColumnName = "bucket_id", insertable = false, updatable = false),
		@JoinColumn(name = "object_name", referencedColumnName = "name", insertable = false, updatable = false)
	})
	public BucketObject getBucketObject() {
		return bucketObject;
	}

	public void setBucketObject(BucketObject bucketObject) {
		this.bucketObject = bucketObject;
	}

	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumns(
			foreignKey = @ForeignKey(name = RdbmsConstants.PREFIX + "bucket_object_version_fk_storage"),
			value = {
		@JoinColumn(name = "storage_owner_id", referencedColumnName = "owner_id"),
		@JoinColumn(name = "storage_name", referencedColumnName = "name")
	})
	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}

	@NotNull
	@Column(name = "content_key")
	public String getContentKey() {
		return contentKey;
	}

	public void setContentKey(String contentKey) {
		this.contentKey = contentKey;
	}

	@NotNull
	@Column(name = "content_length")
	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	@NotNull
	@Column(name = "content_type")
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Column(name = "content_md5")
	public String getContentMD5() {
		return contentMD5;
	}

	public void setContentMD5(String contentMD5) {
		this.contentMD5 = contentMD5;
	}

	@Column(name = "content_disposition")
	public String getContentDisposition() {
		return contentDisposition;
	}

	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}

	@NotNull
	@Column(name = "entity_tag")
	public String getEntityTag() {
		return entityTag;
	}

	public void setEntityTag(String entityTag) {
		this.entityTag = entityTag;
	}

	@NotNull
	@Column(name = "last_modified")
	public Long getLastModified() {
		return lastModified;
	}

	public void setLastModified(Long lastModified) {
		this.lastModified = lastModified;
	}
	
	@ElementCollection
	@CollectionTable(name = "bucket_object_version_tags", 
		foreignKey = @ForeignKey(name = RdbmsConstants.PREFIX + "bucket_object_version_tags_fk_bucket_object_version"),
		joinColumns = {
			@JoinColumn(name = "bucket_id", referencedColumnName = "bucket_id"),
			@JoinColumn(name = "object_name", referencedColumnName = "object_name"),
			@JoinColumn(name = "version_uuid", referencedColumnName = "version_uuid")
	})
	@MapKeyColumn(name = "tag", nullable = false)
	@Column(name = "value", nullable = false)
	public Map<String, String> getTags() {
		return tags;
	}

	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}

	@PrePersist
	private void prePersist() {
		if (creationDate == null) {
			creationDate = Calendar.getInstance();
		}
		if (lastModified == null) {
			lastModified = System.currentTimeMillis();
		}
	}
	
	@PreUpdate
	private void preUpdate() {
		lastModified = System.currentTimeMillis();
	}

	@Override
	public String toString() {
		return "BucketObjectVersion [getId()=" + getId() + "]";
	}
}
