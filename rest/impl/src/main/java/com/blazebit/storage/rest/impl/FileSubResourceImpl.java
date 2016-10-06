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
import com.blazebit.storage.core.api.*;
import com.blazebit.storage.core.model.jpa.*;
import com.blazebit.storage.core.model.security.Role;
import com.blazebit.storage.rest.api.FileSubResource;
import com.blazebit.storage.rest.impl.view.BucketObjectRepresentationView;
import com.blazebit.storage.rest.impl.view.BucketObjectVersionRepresentationView;
import com.blazebit.storage.rest.model.BlazeStorageHeaders;
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
	@Inject
	private AccountDataAccess accountDataAccess;
	
	public FileSubResourceImpl(long accountId, String bucketId, String key) {
		this.accountId = accountId;
		this.bucketObjectId = new BucketObjectId(new Bucket(bucketId), key);
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
		BucketObjectRepresentationView result = getBucketObject(bucketObjectId);
		
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
	
	private BucketObjectRepresentationView getBucketObject(BucketObjectId objectId) {
		EntityViewSetting<BucketObjectRepresentationView, CriteriaBuilder<BucketObjectRepresentationView>> setting;
		setting = EntityViewSetting.create(BucketObjectRepresentationView.class);
		BucketObjectRepresentationView result = bucketObjectDataAccess.findById(objectId, setting);

		if (result == null) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("Bucket object not found").build());
		}
		if (!result.getOwnerId().equals(accountId) && !userContext.getAccountRoles().contains(Role.ADMIN)) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Not allowed to access bucket object").build());
		}
		
		return result;
	}

	@Override
	public Response delete() {
		try {
			bucketObjectService.delete(bucketObjectId);
			return Response.noContent().build();
		} catch (BucketObjectNotFoundException ex) {
			return Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("Bucket object not found").build();
		}
	}

	@Override
	public Response put(BucketObjectUpdateRepresentation bucketObjectUpdate) {
		Storage storage = getStorage(accountId, bucketObjectId.getBucketId(), bucketObjectUpdate.getStorageOwner(), bucketObjectUpdate.getStorageName());
		URI storageUri = storage.getUri();
		String externalContentKey = bucketObjectUpdate.getExternalContentKey();
		String contentKey;
		
		if (externalContentKey != null && !externalContentKey.isEmpty()) {
			if (bucketObjectUpdate.getCopySource() != null) {
				return Response.status(Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("Copy source not allowed with external content key!").build();
			}
			
			contentKey = externalContentKey;
		} else if (bucketObjectUpdate.getCopySource() != null) {
			String source = bucketObjectUpdate.getCopySource();
			int idx;
			// Format is /bucketName/key
			if (source.charAt(0) != '/' || (idx = source.indexOf('/', 1)) < 2) {
				return Response.status(Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("Invalid copy source format. Expected '/bucketName/key'").build();
			}
			
			String sourceBucket = source.substring(1, idx);
			String sourceKey = source.substring(idx + 1);

			BucketObjectRepresentationView result = getBucketObject(new BucketObjectId(sourceBucket, sourceKey));
			BucketObjectVersionRepresentationView contentVersion = result.getContentVersion();
			URI sourceStorageUri = contentVersion.getStorageUri();
			String sourceContentKey = contentVersion.getContentKey();
			contentKey = bucketObjectService.copyContent(sourceStorageUri, sourceContentKey, storageUri);
			
			if (bucketObjectUpdate.getContentDisposition() == null) {
				bucketObjectUpdate.setContentDisposition(ContentDisposition.fromString(contentVersion.getContentDisposition()));
			}
			if (bucketObjectUpdate.getTags() == null || bucketObjectUpdate.getTags().isEmpty()) {
				bucketObjectUpdate.setTags(contentVersion.getTags());
			}
			
			bucketObjectUpdate.setContentType(contentVersion.getContentType());
		} else {
			// TODO: Check content md5
			contentKey = bucketObjectService.createContent(storageUri, bucketObjectUpdate.getContent());
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
		
		boolean success = false;
		try {
			bucketObjectService.put(bucketObject);
			success = true;
		} finally {
			if (!success && !contentKey.equals(externalContentKey)) {
				// Delete the file
				bucketObjectService.deleteContent(storageUri, contentKey);
			}
		}
		
		return Response.ok().build();
	}

	private Storage getStorage(long accountId, String bucketId, String storageOwner, String storageName) {
		Storage storage;
		if (userContext.getAccountRoles().contains(Role.ADMIN) && !userContext.getAccountKey().equals(storageOwner)) {
			Account storageOwnerAccount = accountDataAccess.findByKey(storageOwner);
			accountId = storageOwnerAccount.getId();
		}
		if (storageName != null && !storageName.isEmpty()) {
			storage = storageDataAccess.findById(new StorageId(accountId, storageName));
		} else {
			storage = storageDataAccess.findByBucketId(bucketId);
		}

		if (storage == null) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).header(BlazeStorageHeaders.ERROR_CODE, "StorageNotFound").type(MediaType.TEXT_PLAIN).entity("Storage not found").build());
		}
		if (!storage.getId().getOwnerId().equals(accountId) && !userContext.getAccountRoles().contains(Role.ADMIN)) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("No allowed to access storage").build());
		}
		
		return storage;
	}

}
