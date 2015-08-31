package com.blazebit.storage.rest.impl;

import java.io.InputStream;
import java.net.URI;
import java.util.Date;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.BucketObjectDataAccess;
import com.blazebit.storage.core.api.BucketObjectService;
import com.blazebit.storage.core.api.StorageDataAccess;
import com.blazebit.storage.core.model.jpa.Account;
import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.BucketObject;
import com.blazebit.storage.core.model.jpa.BucketObjectId;
import com.blazebit.storage.core.model.jpa.BucketObjectVersion;
import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.jpa.StorageId;
import com.blazebit.storage.rest.api.FileSubResource;
import com.blazebit.storage.rest.impl.view.BucketObjectRepresentationView;
import com.blazebit.storage.rest.impl.view.BucketObjectVersionRepresentationView;

public class FileSubResourceImpl extends AbstractResource implements FileSubResource {

	private final long accountId;
	private final BucketObjectId bucketObjectId;
	
	@Context
	private HttpServletRequest request;
	
	@Inject
	private BucketObjectDataAccess bucketObjectDataAccess;
	@Inject
	private BucketObjectService bucketObjectService;
	@Inject
	private StorageDataAccess storageDataAccess;
	
	public FileSubResourceImpl(long accountId, String bucketId, String key) {
		this.accountId = accountId;
		this.bucketObjectId = new BucketObjectId(new Bucket(bucketId), key);
	}

	@Override
	public Response get() {
		return getOrHead(true);
	}

	@Override
	public Response head() {
		return getOrHead(false);
	}
	
	private Response getOrHead(boolean isGet) {
		EntityViewSetting<BucketObjectRepresentationView, CriteriaBuilder<BucketObjectRepresentationView>> setting;
		setting = EntityViewSetting.create(BucketObjectRepresentationView.class);
		BucketObjectRepresentationView result = bucketObjectDataAccess.findById(bucketObjectId, setting);

		if (result == null) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("Bucket object not found").build());
		}
		if (!result.getOwnerId().equals(accountId)) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("No allowed to access bucket object").build());
		}
		
		// TODO: implement range parameter?
		// TODO: implement If-Modified-Since, If-Unmodified-Since, If-Match, If-None-Match
		BucketObjectVersionRepresentationView version = result.getContentVersion();
		ResponseBuilder resonpose = Response.ok()
			.type(version.getContentType())
			.lastModified(new Date(version.getLastModified()))
			.tag(version.getETag())
			.header(HttpHeaders.CONTENT_LENGTH, version.getContentLength())
			.header(HttpHeaders.CONTENT_DISPOSITION, version.getContentDisposition());

		if (isGet) {
			InputStream is = bucketObjectDataAccess.getContent(version.getStorageUri(), version.getContentUri());
			resonpose.entity(is);
		}
		
		return resonpose.build();
	}

	@Override
	public Response delete() {
		throw new WebApplicationException(Response.status(Status.NOT_IMPLEMENTED).type(MediaType.TEXT_PLAIN).entity("Not yet implemented").build());
	}

	@Override
	public Response put(String contentType, String contentDisposition, long contentLength, String contentMD5, String storageName, 
//			Map<String, String> tags, 
			InputStream inputStream) {
		Storage storage = getStorage(accountId, bucketObjectId.getBucket().getId(), storageName);
		URI storageUri = null; // Either from the given storage name or the bucket default
		String key = bucketObjectId.getName();
		// TODO: Check content md5
		
		// TODO: implement content uri retrieval without storage
		URI contentUri = bucketObjectService.createContent(storageUri, key, inputStream);
		
		BucketObject bucketObject = new BucketObject(bucketObjectId);
		BucketObjectVersion version = new BucketObjectVersion();
		bucketObject.setContentVersion(version);
		
		version.setContentDisposition(contentDisposition);
		version.setContentLength(contentLength);
		version.setContentMD5(contentMD5);
		version.setContentType(contentType);
		version.setContentUri(contentUri);
		
//		version.setETag(eTag);
//		version.setLastModified(lastModified);
		version.setStorage(storage);
//		version.setTags(tags);
		
		bucketObjectService.put(bucketObject);
		
		return null;
	}

	private Storage getStorage(long accountId, String bucketId, String storageName) {
		Storage storage;
		if (storageName != null && !storageName.isEmpty()) {
			storage = storageDataAccess.findById(new StorageId(new Account(accountId), storageName));
		} else {
			storage = storageDataAccess.findByBucketId(bucketId);
		}

		if (storage == null) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("Storage not found").build());
		}
		if (!storage.getOwnerId().equals(accountId)) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("No allowed to access storage").build());
		}
		
		return storage;
	}

}
