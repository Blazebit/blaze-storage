package com.blazebit.storage.rest.impl;

import java.io.InputStream;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
import com.blazebit.storage.core.model.security.Role;
import com.blazebit.storage.rest.api.FileSubResource;
import com.blazebit.storage.rest.impl.view.BucketObjectRepresentationView;
import com.blazebit.storage.rest.impl.view.BucketObjectVersionRepresentationView;
import com.blazebit.storage.rest.model.BucketObjectHeadRepresentation;
import com.blazebit.storage.rest.model.BucketObjectRepresentation;
import com.blazebit.storage.rest.model.BucketObjectUpdateRepresentation;
import com.blazebit.storage.rest.model.rs.ContentDisposition;

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
		// Needed so we can use this id for loading with em.find + locking
		this.bucketObjectId.getBucket().setObjects(null);
	}

	@Override
	public BucketObjectRepresentation get() {
		return (BucketObjectRepresentation) getOrHead(true);
	}

	@Override
	public BucketObjectHeadRepresentation head() {
		return getOrHead(false);
	}
	
	private BucketObjectHeadRepresentation getOrHead(boolean isGet) {
		EntityViewSetting<BucketObjectRepresentationView, CriteriaBuilder<BucketObjectRepresentationView>> setting;
		setting = EntityViewSetting.create(BucketObjectRepresentationView.class);
		BucketObjectRepresentationView result = bucketObjectDataAccess.findById(bucketObjectId, setting);

		if (result == null) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("Bucket object not found").build());
		}
		if (!result.getOwnerId().equals(accountId) && !userContext.getAccountRoles().contains(Role.ADMIN)) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("No allowed to access bucket object").build());
		}
		
		// TODO: implement range parameter?
		// TODO: implement If-Modified-Since, If-Unmodified-Since, If-Match, If-None-Match
		BucketObjectVersionRepresentationView version = result.getContentVersion();
		BucketObjectHeadRepresentation response;
		if (isGet) {
			response = new BucketObjectRepresentation();
		} else {
			response = new BucketObjectHeadRepresentation();
		}
		
		response.setContentType(version.getContentType());
		response.setContentDisposition(ContentDisposition.fromString(version.getContentDisposition()));
		Calendar lastModified = Calendar.getInstance();
		lastModified.setTime(new Date(version.getLastModified()));
		response.setLastModified(lastModified);
		response.setEntityTag(version.getEntityTag());
		response.setSize(version.getContentLength());
		response.setStorageName(version.getStorageName());
		response.setStorageOwner(version.getStorageOwnerKey());
		response.setTags(version.getTags());
		
		if (isGet) {
			InputStream is = bucketObjectDataAccess.getContent(version.getStorageUri(), version.getContentKey());
			((BucketObjectRepresentation) response).setContent(is);
		}
		
		return response;
	}

	@Override
	public Response delete() {
		throw new WebApplicationException(Response.status(Status.NOT_IMPLEMENTED).type(MediaType.TEXT_PLAIN).entity("Not yet implemented").build());
	}

	@Override
	public Response put(BucketObjectUpdateRepresentation bucketObjectUpdate, InputStream content) {
		Storage storage = getStorage(accountId, bucketObjectId.getBucket().getId(), bucketObjectUpdate.getStorageName());
		URI storageUri = storage.getUri();
		String externalContentKey = bucketObjectUpdate.getExternalContentKey();
		String contentKey;
		
		if (externalContentKey != null && !externalContentKey.isEmpty()) {
			contentKey = externalContentKey;
			// TODO: Maybe check if the key is valid?
		} else {
			// TODO: Check content md5
			contentKey = bucketObjectService.createContent(storageUri, content);
		}
		
		BucketObject bucketObject = new BucketObject(bucketObjectId);
		BucketObjectVersion version = new BucketObjectVersion();
		bucketObject.setContentVersion(version);
		
		if (bucketObjectUpdate.getContentDisposition() != null) {
			version.setContentDisposition(bucketObjectUpdate.getContentDisposition().toString());
		}
		
		version.setContentLength(bucketObjectUpdate.getSize());
		// TODO: for what?
		version.setContentMD5(bucketObjectUpdate.getContentMD5());
		version.setContentType(bucketObjectUpdate.getContentType());
		version.setContentKey(contentKey);
		
		// TODO: implement right
//		version.setEntityTag(bucketObjectUpdate.getEntityTag());
		version.setEntityTag("");
		version.setStorage(storage);
		version.setTags(bucketObjectUpdate.getTags());
		
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
		if (!storage.getOwnerId().equals(accountId) && !userContext.getAccountRoles().contains(Role.ADMIN)) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("No allowed to access storage").build());
		}
		
		return storage;
	}

}
