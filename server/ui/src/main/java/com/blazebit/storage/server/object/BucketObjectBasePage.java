package com.blazebit.storage.server.object;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.blazebit.storage.rest.client.BlazeStorage;
import com.blazebit.storage.rest.model.BucketObjectBaseRepresentation;
import com.blazebit.storage.rest.model.BucketObjectUpdateRepresentation;
import com.blazebit.storage.server.faces.tag.TagEntry;
import com.blazebit.storage.server.faces.tag.TagsHolder;
import com.blazebit.storage.server.util.BucketObjectInputStream;

public class BucketObjectBasePage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(BucketObjectBasePage.class.getName());

	@Inject
	protected BlazeStorage client;
	@Inject
	protected FacesContext facesContext;

	protected String bucket;
	protected String key;
	protected TagsHolder tagsHolder = new TagsHolder();
	protected BucketObjectBaseRepresentation bucketObject = new BucketObjectUpdateRepresentation();

	public String viewAction() {
		try {
			if (bucket != null && !bucket.isEmpty() && key != null && !key.isEmpty()) {
				bucketObject = client.buckets().get(bucket).getFile(key).head();
				if (bucketObject == null) {
					tagsHolder.setTagEntries(new ArrayList<TagEntry>());
					facesContext.addMessage(null, new FacesMessage("No bucket object found for key " + key));
					init();
					return null;
				} else {
					tagsHolder.setTags(bucketObject.getTags());
					init();
					return "";
				}
			}

			facesContext.addMessage(null, new FacesMessage("Invalid empty key!"));
			return null;
		} catch (RuntimeException ex) {
			facesContext.addMessage(null, new FacesMessage("Could not load bucket object"));
			LOG.log(Level.SEVERE, "Could not load bucket object " + key, ex);
			return null;
		}
	}
	
	protected void init() {
	}
	
	public void put() {
		BucketObjectUpdateRepresentation oldBucketObject = (BucketObjectUpdateRepresentation) bucketObject;
		BucketObjectUpdateRepresentation newBucketObject = new BucketObjectUpdateRepresentation();
		newBucketObject.setContent(getContent());
		newBucketObject.setContentDisposition(oldBucketObject.getContentDisposition());
		newBucketObject.setContentMD5(oldBucketObject.getContentMD5());
		newBucketObject.setContentType(oldBucketObject.getContentType());
		newBucketObject.setExternalContentKey(oldBucketObject.getExternalContentKey());
		newBucketObject.setSize(oldBucketObject.getSize());
		newBucketObject.setStorageName(oldBucketObject.getStorageName());
		newBucketObject.setStorageOwner(oldBucketObject.getStorageOwner());
		newBucketObject.setTags(tagsHolder.getTags());
		client.buckets().get(bucket).getFile(key).put(newBucketObject);
		bucketObject = newBucketObject;
	}
	
	protected InputStream getContent() {
		return new BucketObjectInputStream(client, bucket, key);
	}

	public BucketObjectBaseRepresentation getBucketObject() {
		return bucketObject;
	}

	public TagsHolder getTagsHolder() {
		return tagsHolder;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
