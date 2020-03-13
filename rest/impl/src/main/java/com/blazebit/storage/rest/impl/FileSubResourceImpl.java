package com.blazebit.storage.rest.impl;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.AccountDataAccess;
import com.blazebit.storage.core.api.BucketObjectDataAccess;
import com.blazebit.storage.core.api.BucketObjectNotFoundException;
import com.blazebit.storage.core.api.BucketObjectService;
import com.blazebit.storage.core.api.StorageDataAccess;
import com.blazebit.storage.core.api.StorageException;
import com.blazebit.storage.core.api.spi.StorageResult;
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
import com.blazebit.storage.rest.model.BlazeStorageHeaders;
import com.blazebit.storage.rest.model.BucketObjectHeadRepresentation;
import com.blazebit.storage.rest.model.BucketObjectRepresentation;
import com.blazebit.storage.rest.model.BucketObjectUpdateRepresentation;
import com.blazebit.storage.rest.model.rs.ContentDisposition;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

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
		byte[] md5Bytes;
		if (bucketObjectUpdate.getContentMD5() == null) {
			md5Bytes = null;
		} else {
			md5Bytes = HexUtils.hexStringToByteArray(bucketObjectUpdate.getContentMD5());
		}
		Storage storage = getStorage(accountId, bucketObjectId.getBucketId(), bucketObjectUpdate.getStorageOwner(), bucketObjectUpdate.getStorageName());
		URI storageUri = storage.getUri();
		String externalContentKey = bucketObjectUpdate.getExternalContentKey();
		String contentKey;
		String contentMd5;
		long contentLength;
		
		if (externalContentKey != null && !externalContentKey.isEmpty()) {
			if (bucketObjectUpdate.getCopySource() != null) {
				return Response.status(Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("Copy source not allowed with external content key!").build();
			}
			
			contentKey = externalContentKey;
			contentMd5 = bucketObjectUpdate.getContentMD5();
			contentLength = bucketObjectUpdate.getSize();
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
			StorageResult storageResult = bucketObjectService.copyContent(sourceStorageUri, sourceContentKey, storageUri);
			contentKey = storageResult.getExternalKey();
			contentMd5 = HexUtils.bytesToHex(storageResult.getMd5Checksum());
			if (md5Bytes != null && storageResult.getMd5Checksum() != null) {
				if (!Arrays.equals(md5Bytes, storageResult.getMd5Checksum())) {
					bucketObjectService.deleteContent(storageUri, storageResult.getExternalKey());
					return Response.status(Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("The expected MD5 checksum " + bucketObjectUpdate.getContentMD5() + " did not match the actual MD5 checksum " + contentMd5).build();
				}
			}
			contentLength = contentVersion.getContentLength();

			if (bucketObjectUpdate.getContentDisposition() == null) {
				bucketObjectUpdate.setContentDisposition(ContentDisposition.fromString(contentVersion.getContentDisposition()));
			}
			if (bucketObjectUpdate.getTags() == null || bucketObjectUpdate.getTags().isEmpty()) {
				bucketObjectUpdate.setTags(contentVersion.getTags());
			}
			
			bucketObjectUpdate.setContentType(contentVersion.getContentType());
		} else {
			InputStream inputStream;
			try {
				inputStream = bucketObjectUpdate.getContent().getInputStream();
			} catch (IOException e) {
				throw new StorageException(e);
			}
			StorageResult storageResult = bucketObjectService.createContent(storageUri, inputStream);
			contentKey = storageResult.getExternalKey();
			contentMd5 = HexUtils.bytesToHex(storageResult.getMd5Checksum());
			if (md5Bytes != null && storageResult.getMd5Checksum() != null) {
				if (!Arrays.equals(md5Bytes, storageResult.getMd5Checksum())) {
					bucketObjectService.deleteContent(storageUri, storageResult.getExternalKey());
					return Response.status(Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("The expected MD5 checksum " + bucketObjectUpdate.getContentMD5() + " did not match the actual MD5 checksum " + contentMd5).build();
				}
			}
			long expectedContentLength = bucketObjectUpdate.getSize();
			if (expectedContentLength >= 0 && expectedContentLength != storageResult.getSize()) {
				bucketObjectService.deleteContent(storageUri, storageResult.getExternalKey());
				return Response.status(Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("The expected content length " + expectedContentLength + " did not match the actual content length " + storageResult.getSize()).build();
			}
			contentLength = storageResult.getSize();
		}
		
		BucketObject bucketObject = new BucketObject(bucketObjectId);
		BucketObjectVersion version = new BucketObjectVersion();
		bucketObject.setContentVersion(version);
		
		if (bucketObjectUpdate.getContentDisposition() != null) {
			version.setContentDisposition(bucketObjectUpdate.getContentDisposition().toString());
		}
		
		version.setContentLength(contentLength);
		version.setContentMD5(contentMd5);
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
		if (storageOwner != null && !storageOwner.isEmpty() && userContext.getAccountRoles().contains(Role.ADMIN) && !userContext.getAccountKey().equals(storageOwner)) {
			Account storageOwnerAccount = accountDataAccess.findByKey(storageOwner);
			if (storageOwnerAccount == null) {
				throw new WebApplicationException(Response.status(Status.NOT_FOUND).header(BlazeStorageHeaders.ERROR_CODE, "StorageOwnerNotFound").type(MediaType.TEXT_PLAIN).entity("Storage owner '" + storageOwner + "' not found").build());
			}
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
