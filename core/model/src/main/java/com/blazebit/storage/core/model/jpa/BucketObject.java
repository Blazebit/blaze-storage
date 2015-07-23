package com.blazebit.storage.core.model.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class BucketObject extends EmbeddedIdBaseEntity<BucketObjectId> {

	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULT_CONTENT_TYPE = "binary/octet-stream";
	
	private BucketObjectState state;
	private String contentVersionUuid;
	private BucketObjectVersion contentVersion;
	
	public BucketObject() {
		super(new BucketObjectId());
	}
	
	public BucketObject(BucketObjectId id) {
		super(id);
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
}
