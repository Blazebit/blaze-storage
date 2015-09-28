package com.blazebit.storage.rest.impl.view;

import java.net.URI;
import java.util.Map;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.storage.core.model.jpa.BucketObjectVersion;
import com.blazebit.storage.core.model.jpa.BucketObjectVersionId;

@EntityView(BucketObjectVersion.class)
public interface BucketObjectVersionRepresentationView {
	
	@IdMapping("id")
	public BucketObjectVersionId getId();

	@Mapping("storage.id.name")
	public String getStorageName();

	@Mapping("storage.owner.key")
	public String getStorageOwnerKey();

	@Mapping("storage.uri")
	public URI getStorageUri();
	
	public String getContentKey();
	
	public Long getContentLength();
	
	public String getContentType();
	
	public String getContentMD5();
	
	public String getContentDisposition();
	
	public String getEntityTag();
	
	public Long getLastModified();
	
	public Map<String, String> getTags();
	
}
