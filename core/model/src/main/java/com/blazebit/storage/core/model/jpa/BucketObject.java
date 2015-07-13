package com.blazebit.storage.core.model.jpa;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Entity
public class BucketObject extends EmbeddedIdBaseEntity<BucketObjectId> {

	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULT_CONTENT_TYPE = "binary/octet-stream";
	
	private BucketObjectState state;
	private BucketObjectVersion contentVersion;
	
	public BucketObject() {
		super(new BucketObjectId());
	}
	
	public BucketObject(BucketObjectId id) {
		super(id);
	}

	@Enumerated(EnumType.ORDINAL)
	public BucketObjectState getState() {
		return state;
	}

	public void setState(BucketObjectState state) {
		this.state = state;
	}

	@ManyToOne(optional = true)
	public BucketObjectVersion getContentVersion() {
		return contentVersion;
	}

	public void setContentVersion(BucketObjectVersion contentVersion) {
		this.contentVersion = contentVersion;
	}
}
