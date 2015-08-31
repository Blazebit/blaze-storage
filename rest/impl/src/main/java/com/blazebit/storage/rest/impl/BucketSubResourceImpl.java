package com.blazebit.storage.rest.impl;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.storage.core.api.BucketDataAccess;
import com.blazebit.storage.core.api.BucketService;
import com.blazebit.storage.core.model.jpa.Account;
import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.jpa.StorageId;
import com.blazebit.storage.rest.api.BucketSubResource;
import com.blazebit.storage.rest.api.FileSubResource;
import com.blazebit.storage.rest.impl.view.BucketRepresentationView;
import com.blazebit.storage.rest.model.BucketRepresentation;
import com.blazebit.storage.rest.model.BucketUpdateRepresentation;

public class BucketSubResourceImpl extends AbstractResource implements BucketSubResource {

	private final long accountId;
	private final String bucketId;
	
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
		if (!result.getOwnerId().equals(accountId)) {
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("No allowed to access bucket").build());
		}
		
		return result;
	}

	@Override
	public Response head() {
		Bucket result = bucketDataAccess.findByName(bucketId);
		if (result == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		if (!result.getOwner().getId().equals(accountId)) {
			return Response.status(Status.FORBIDDEN).build();
		}
		
		return Response.ok().build();
	}

	@Override
	public Response delete() {
		throw new WebApplicationException(Response.status(Status.NOT_IMPLEMENTED).type(MediaType.TEXT_PLAIN).entity("Not yet implemented").build());
	}

	@Override
	public Response put(BucketUpdateRepresentation bucketUpdate) {
		Bucket bucket = new Bucket(bucketId);
		bucket.setOwner(new Account(accountId));
		bucket.setStorage(new Storage(new StorageId(bucket.getOwner(), bucketUpdate.getDefaultStorageName())));
		bucketService.create(bucket);
		return Response.ok().build();
	}

	@Override
	public FileSubResource getFile(String key) {
		return inject(new FileSubResourceImpl(accountId, bucketId, key));
	}

}
