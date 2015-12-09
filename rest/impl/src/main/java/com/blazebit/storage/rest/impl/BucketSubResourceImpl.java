package com.blazebit.storage.rest.impl;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.AccountDataAccess;
import com.blazebit.storage.core.api.BucketDataAccess;
import com.blazebit.storage.core.api.BucketService;
import com.blazebit.storage.core.model.jpa.Account;
import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.jpa.StorageId;
import com.blazebit.storage.core.model.security.Role;
import com.blazebit.storage.rest.api.BucketSubResource;
import com.blazebit.storage.rest.api.FileSubResource;
import com.blazebit.storage.rest.impl.view.BucketHeadRepresentationView;
import com.blazebit.storage.rest.impl.view.BucketRepresentationView;
import com.blazebit.storage.rest.model.BlazeStorageHeaders;
import com.blazebit.storage.rest.model.BucketHeadRepresentation;
import com.blazebit.storage.rest.model.BucketRepresentation;
import com.blazebit.storage.rest.model.BucketUpdateRepresentation;

public class BucketSubResourceImpl extends AbstractResource implements BucketSubResource {

	private final long accountId;
	private final String bucketId;
	
	@Inject
	private AccountDataAccess accountDataAccess;
	@Inject
	private BucketDataAccess bucketDataAccess;
	@Inject
	private BucketService bucketService;

	public BucketSubResourceImpl(long accountId, String bucketId) {
		this.accountId = accountId;
		this.bucketId = bucketId;
	}

	@Override
	public BucketRepresentation get(String prefix, Integer limit, String marker) {
		EntityViewSetting<BucketRepresentationView, CriteriaBuilder<BucketRepresentationView>> setting;
		setting = EntityViewSetting.create(BucketRepresentationView.class);
		BucketRepresentationView result = bucketDataAccess.findByName(bucketId, prefix, limit, marker, setting);

		if (result == null) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).type(MediaType.TEXT_PLAIN).entity("Bucket not found").build());
		}
		if (!result.getOwnerId().equals(accountId) && !userContext.getAccountRoles().contains(Role.ADMIN)) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("No allowed to access bucket").build());
		}
		
		return result;
	}

	@Override
	public BucketHeadRepresentation head() {
		EntityViewSetting<BucketHeadRepresentationView, CriteriaBuilder<BucketHeadRepresentationView>> setting;
		setting = EntityViewSetting.create(BucketHeadRepresentationView.class);
		BucketHeadRepresentationView result = bucketDataAccess.findByName(bucketId, setting);
		if (result == null) {
			throw new WebApplicationException(Response.status(Status.NOT_FOUND).build());
		}
		if (!result.getOwnerId().equals(accountId) && !userContext.getAccountRoles().contains(Role.ADMIN)) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).build());
		}
		
		return result;
	}

	@Override
	public Response delete() {
		throw new WebApplicationException(Response.status(Status.NOT_IMPLEMENTED).type(MediaType.TEXT_PLAIN).entity("Not yet implemented").build());
	}

//	@Override
//	public Response put(BucketUpdateRepresentation bucketUpdate) {
//		return put(bucketUpdate, userContext.getAccountKey());
//	}

	@Override
	public Response put(BucketUpdateRepresentation bucketUpdate, String ownerKey) {
		Bucket bucket = new Bucket(bucketId);
		
		if (ownerKey == null || ownerKey.isEmpty() || ownerKey.equals(userContext.getAccountKey())) {
			bucket.setOwner(new Account(accountId));
		} else if (userContext.getAccountRoles().contains(Role.ADMIN)) {
			Account owner = accountDataAccess.findByKey(ownerKey);
			
			if (owner == null) {
				throw new WebApplicationException(Response.status(Status.BAD_REQUEST).header(BlazeStorageHeaders.ERROR_CODE, "AccountNotFound").type(MediaType.TEXT_PLAIN_TYPE).entity("Account does not exist").build());
			}
			
			bucket.setOwner(owner);
		} else {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN_TYPE).entity("Only admins may change the owner").build());
		}
		
		String storageOwnerKey = bucketUpdate.getDefaultStorageOwner();
		Account storageOwner;

		if (storageOwnerKey == null || storageOwnerKey.isEmpty() || storageOwnerKey.equals(userContext.getAccountKey())) {
			storageOwner = new Account(userContext.getAccountId());
		} else if (userContext.getAccountRoles().contains(Role.ADMIN)) {
			storageOwner = accountDataAccess.findByKey(storageOwnerKey);
			
			if (storageOwner == null) {
				throw new WebApplicationException(Response.status(Status.BAD_REQUEST).header(BlazeStorageHeaders.ERROR_CODE, "AccountNotFound").type(MediaType.TEXT_PLAIN_TYPE).entity("Account does not exist").build());
			}
		} else {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN_TYPE).entity("Only admins may change the storage owner").build());
		}
		
		bucket.setStorage(new Storage(new StorageId(storageOwner.getId(), bucketUpdate.getDefaultStorageName())));
		bucketService.put(bucket);
		return Response.ok().build();
	}

	@Override
	public FileSubResource getFile(String key) {
		return inject(new FileSubResourceImpl(accountId, bucketId, key));
	}

}
