package com.blazebit.storage.core.model.jpa;

import java.net.URI;

import javax.persistence.Convert;
import javax.persistence.Entity;

import com.blazebit.storage.core.model.jpa.converter.URIConverter;

@Entity
public class Storage extends EmbeddedIdBaseEntity<StorageId> {

	private static final long serialVersionUID = 1L;
	
	private URI uri;

	public Storage() {
		super(new StorageId());
	}
	
	public Storage(StorageId id) {
		super(id);
	}

	@Convert(converter = URIConverter.class)
	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

}
