package com.blazebit.storage.core.model.service;

import com.blazebit.storage.core.model.jpa.BucketObjectId;

public class BucketObjectDeleteReportItem {
	
	private final BucketObjectId bucketObjectId;
	private final String versionUuid;
	
	private final String code;
	private final String message;
	
	private BucketObjectDeleteReportItem(BucketObjectId bucketObjectId, String versionUuid, String code, String message) {
		this.bucketObjectId = bucketObjectId;
		this.versionUuid = versionUuid;
		this.code = code;
		this.message = message;
	}

	public static BucketObjectDeleteReportItem deleted(BucketObjectId bucketObjectId) {
		return new BucketObjectDeleteReportItem(bucketObjectId, null, null, null);
	}
	
	public static BucketObjectDeleteReportItem deleted(BucketObjectId bucketObjectId, String versionUuid) {
		return new BucketObjectDeleteReportItem(bucketObjectId, versionUuid, null, null);
	}

	public static BucketObjectDeleteReportItem error(BucketObjectId bucketObjectId, String code, String message) {
		return new BucketObjectDeleteReportItem(bucketObjectId, null, code, message);
	}
	
	public static BucketObjectDeleteReportItem error(BucketObjectId bucketObjectId, String versionUuid, String code, String message) {
		return new BucketObjectDeleteReportItem(bucketObjectId, versionUuid, code, message);
	}

	public BucketObjectId getBucketObjectId() {
		return bucketObjectId;
	}

	public String getVersionUuid() {
		return versionUuid;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bucketObjectId == null) ? 0 : bucketObjectId.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((versionUuid == null) ? 0 : versionUuid.hashCode());
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
		BucketObjectDeleteReportItem other = (BucketObjectDeleteReportItem) obj;
		if (bucketObjectId == null) {
			if (other.bucketObjectId != null)
				return false;
		} else if (!bucketObjectId.equals(other.bucketObjectId))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (versionUuid == null) {
			if (other.versionUuid != null)
				return false;
		} else if (!versionUuid.equals(other.versionUuid))
			return false;
		return true;
	}
}
