package com.blazebit.storage.core.model.jpa;

import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.blazebit.storage.core.model.jpa.converter.URIConverter;

@Entity
public class BucketObjectVersion extends EmbeddedIdBaseEntity<BucketObjectVersionId> {
	
	private static final long serialVersionUID = 1L;

	public static final String DEFAULT_CONTENT_TYPE = "binary/octet-stream";

	private BucketObjectState state;
	private Calendar creationDate;
	private Storage storage;
	
	private URI contentUri;
	private long contentLength;
	private String contentType = DEFAULT_CONTENT_TYPE;
	private String contentMD5;
	private String contentDisposition;
	private String eTag;
	private long lastModified;
	private Map<String, String> tags = new HashMap<String, String>();

	public BucketObjectVersion() {
		super(new BucketObjectVersionId());
	}

	public BucketObjectVersion(BucketObjectVersionId id) {
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
	@Convert(converter = URIConverter.class)
	@Column(name = "content_uri")
	public URI getContentUri() {
		return contentUri;
	}

	public void setContentUri(URI contentUri) {
		this.contentUri = contentUri;
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

	@NotNull
	@Column(name = "content_md5")
	public String getContentMD5() {
		return contentMD5;
	}

	public void setContentMD5(String contentMD5) {
		this.contentMD5 = contentMD5;
	}

	@NotNull
	@Column(name = "content_disposition")
	public String getContentDisposition() {
		return contentDisposition;
	}

	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}

	@NotNull
	@Column(name = "etag")
	public String getETag() {
		return eTag;
	}

	public void setETag(String eTag) {
		this.eTag = eTag;
	}

	@NotNull
	@Column(name = "last_modified")
	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
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
	}
}
