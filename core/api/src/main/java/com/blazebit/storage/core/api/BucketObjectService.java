package com.blazebit.storage.core.api;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import com.blazebit.storage.core.api.spi.StorageResult;
import com.blazebit.storage.core.model.jpa.BucketObject;
import com.blazebit.storage.core.model.jpa.BucketObjectId;
import com.blazebit.storage.core.model.jpa.BucketObjectVersionId;
import com.blazebit.storage.core.model.service.BucketObjectDeleteReport;

public interface BucketObjectService {

	/**
	 * Creates a file in the storage specified by the given URI with the content
	 * provided by the input stream. Returns the external key under which the
	 * file was created.
	 * 
	 * NOTE: The input stream will not be closed by this method!
	 * 
	 * @param storageUri the URI to the storage
	 * @param inputStream the input stream for reading the content
	 * @return the external key for the storage
	 * @throws StorageException when the file could not be created or some error occurred during reading
	 */
	public StorageResult createContent(URI storageUri, InputStream inputStream);

	public StorageResult copyContent(URI sourceStorageUri, String sourceContentKey, URI targetStorageUri);
	
	public void deleteContent(URI storageUri, String contentKey);

	public void put(BucketObject bucketObject);
	
	public void delete(BucketObjectId bucketObjectId);
	
	public BucketObjectDeleteReport delete(List<BucketObjectId> bucketObjectIds);
	
	public void deleteVersion(BucketObjectVersionId bucketObjectVersionId);
	
	public BucketObjectDeleteReport deleteVersions(List<BucketObjectVersionId> bucketObjectVersionIds);
}
