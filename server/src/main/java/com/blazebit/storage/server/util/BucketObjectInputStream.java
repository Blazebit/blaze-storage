package com.blazebit.storage.server.util;

import java.io.InputStream;

import com.blazebit.storage.rest.client.BlazeStorage;

public class BucketObjectInputStream extends LazyInputStream {

	private static final long serialVersionUID = 1L;
	
	private final BlazeStorage client;
	private final String bucket;
	private final String key;
	
	public BucketObjectInputStream(BlazeStorage client, String bucket, String key) {
		this.client = client;
		this.bucket = bucket;
		this.key = key;
	}

	@Override
	protected InputStream createInputStream() {
		return client.buckets().get(bucket).getFile(key).get().getContent();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bucket == null) ? 0 : bucket.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BucketObjectInputStream other = (BucketObjectInputStream) obj;
		if (bucket == null) {
			if (other.bucket != null)
				return false;
		} else if (!bucket.equals(other.bucket))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BucketObjectInputStream [bucket=" + bucket + ", key=" + key + "]";
	}
}
