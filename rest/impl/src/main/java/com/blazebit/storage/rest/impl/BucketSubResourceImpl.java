package com.blazebit.storage.rest.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;

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
import com.blazebit.storage.rest.model.BucketObjectUpdateRepresentation;
import com.blazebit.storage.rest.model.BucketRepresentation;
import com.blazebit.storage.rest.model.BucketUpdateRepresentation;
import com.blazebit.storage.rest.model.MultipartUploadErrorRepresentation;
import com.blazebit.storage.rest.model.MultipartUploadRepresentation;
import com.blazebit.storage.rest.model.MultipartUploadResultRepresentation;

public class BucketSubResourceImpl extends AbstractResource implements BucketSubResource {

	private static final Logger LOG = Logger.getLogger(BucketSubResourceImpl.class.getName());
	
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
			throw new WebApplicationException(Response.status(Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Not allowed to access bucket").build());
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
	public MultipartUploadResultRepresentation uploadMultiple(MultipartUploadRepresentation upload) {
		List<String> uploaded = new ArrayList<String>(upload.getUploads().size());
		List<MultipartUploadErrorRepresentation> errors = new ArrayList<>();
		MultipartUploadResultRepresentation result = new MultipartUploadResultRepresentation(uploaded, errors);
		
		List<Throwable> exceptions = null;
		
		try (MultipartUploadRepresentation u = upload) {
			for (Map.Entry<String, BucketObjectUpdateRepresentation> entry : upload.getUploads().entrySet()) {
				// TODO: maybe do some sanity checks on the file key?
				String fileKey = entry.getKey();
				
				Response r;
				
				try (InputStream is = entry.getValue().getContent()) {
					r = getFile(fileKey).put(entry.getValue());
				} catch (WebApplicationException ex) {
					r = ex.getResponse();
				} catch (Throwable t) {
					r = null;
					
					if (exceptions == null) {
						exceptions = new ArrayList<>();
					}
					
					exceptions.add(t);
				}
				
				if (r != null) {
					if (r.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
						uploaded.add(fileKey);
					} else {
						String code = r.getHeaderString(BlazeStorageHeaders.ERROR_CODE);
						String message = r.readEntity(String.class);
						errors.add(new MultipartUploadErrorRepresentation(fileKey, code, message));
					}
				}
			}
			
			if (exceptions != null) {
				int size = exceptions.size();
				LOG.log(Level.SEVERE, size + " error(s) happened during multipart processing!");
				
				for (int i = 0; i < size; i++) {
					LOG.log(Level.SEVERE, " - Error " + i, exceptions.get(i));
				}
			}
		}
		
		return result;
	}

	@Override
	public FileSubResource getFile(String key) {
		return inject(new FileSubResourceImpl(accountId, bucketId, key));
	}

}
