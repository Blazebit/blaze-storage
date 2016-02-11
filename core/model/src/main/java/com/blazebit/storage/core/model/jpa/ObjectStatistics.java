package com.blazebit.storage.core.model.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

/**
 * Pending statistics include counters for "deleted" objects.
 * Statistics for versions include counters for all versions, object statistics just for the latest version.
 *
 */
@Embeddable
@MappedSuperclass
public class ObjectStatistics implements Serializable {

	private static final long serialVersionUID = 1L;

	protected long objectCount;
	protected long objectBytes;
	protected long objectVersionCount;
	protected long objectVersionBytes;
	
	protected long pendingObjectCount;
	protected long pendingObjectBytes;
	protected long pendingObjectVersionCount;
	protected long pendingObjectVersionBytes;

	public ObjectStatistics() {
	}

	public ObjectStatistics(long objectCount, long objectBytes, long objectVersionCount, long objectVersionBytes, long pendingObjectCount, long pendingObjectBytes, long pendingObjectVersionCount, long pendingObjectVersionBytes) {
		this.objectCount = objectCount;
		this.objectBytes = objectBytes;
		this.objectVersionCount = objectVersionCount;
		this.objectVersionBytes = objectVersionBytes;
		this.pendingObjectCount = pendingObjectCount;
		this.pendingObjectBytes = pendingObjectBytes;
		this.pendingObjectVersionCount = pendingObjectVersionCount;
		this.pendingObjectVersionBytes = pendingObjectVersionBytes;
	}
	
	public ObjectStatistics copy() {
		return new ObjectStatistics(objectCount, objectBytes, objectVersionCount, objectVersionBytes, pendingObjectCount, pendingObjectBytes, pendingObjectVersionCount, pendingObjectVersionBytes);
	}

	public ObjectStatistics plus(ObjectStatistics statistics) {
		long newObjectCount = this.objectCount + statistics.getObjectCount();
		long newObjectBytes = this.objectBytes + statistics.getObjectBytes();
		long newObjectVersionCount = this.objectVersionCount + statistics.getObjectVersionCount();
		long newObjectVersionBytes = this.objectVersionBytes + statistics.getObjectVersionBytes();
		long newPendingObjectCount = this.pendingObjectCount + statistics.getPendingObjectCount();
		long newPendingObjectBytes = this.pendingObjectBytes + statistics.getPendingObjectBytes();
		long newPendingObjectVersionCount = this.pendingObjectVersionCount + statistics.getPendingObjectVersionCount();
		long newPendingObjectVersionBytes = this.pendingObjectVersionBytes + statistics.getPendingObjectVersionBytes();
		return new ObjectStatistics(newObjectCount, newObjectBytes, newObjectVersionCount, newObjectVersionBytes, newPendingObjectCount, newPendingObjectBytes, newPendingObjectVersionCount, newPendingObjectVersionBytes);
	}
	
	public ObjectStatistics minus(ObjectStatistics statistics) {
		long newObjectCount = this.objectCount - statistics.getObjectCount();
		long newObjectBytes = this.objectBytes - statistics.getObjectBytes();
		long newObjectVersionCount = this.objectVersionCount - statistics.getObjectVersionCount();
		long newObjectVersionBytes = this.objectVersionBytes - statistics.getObjectVersionBytes();
		long newPendingObjectCount = this.pendingObjectCount - statistics.getPendingObjectCount();
		long newPendingObjectBytes = this.pendingObjectBytes - statistics.getPendingObjectBytes();
		long newPendingObjectVersionCount = this.pendingObjectVersionCount - statistics.getPendingObjectVersionCount();
		long newPendingObjectVersionBytes = this.pendingObjectVersionBytes - statistics.getPendingObjectVersionBytes();
		return new ObjectStatistics(newObjectCount, newObjectBytes, newObjectVersionCount, newObjectVersionBytes, newPendingObjectCount, newPendingObjectBytes, newPendingObjectVersionCount, newPendingObjectVersionBytes);
	}

	@NotNull
	@Column(name = "object_count")
	public long getObjectCount() {
		return objectCount;
	}

	public void setObjectCount(long objectCount) {
		this.objectCount = objectCount;
	}

	@NotNull
	@Column(name = "object_bytes")
	public long getObjectBytes() {
		return objectBytes;
	}

	public void setObjectBytes(long objectBytes) {
		this.objectBytes = objectBytes;
	}

	@NotNull
	@Column(name = "object_version_count")
	public long getObjectVersionCount() {
		return objectVersionCount;
	}

	public void setObjectVersionCount(long objectVersionCount) {
		this.objectVersionCount = objectVersionCount;
	}

	@NotNull
	@Column(name = "object_version_bytes")
	public long getObjectVersionBytes() {
		return objectVersionBytes;
	}

	public void setObjectVersionBytes(long objectVersionBytes) {
		this.objectVersionBytes = objectVersionBytes;
	}

	@NotNull
	@Column(name = "pending_object_count")
	public long getPendingObjectCount() {
		return pendingObjectCount;
	}

	public void setPendingObjectCount(long pendingObjectCount) {
		this.pendingObjectCount = pendingObjectCount;
	}

	@NotNull
	@Column(name = "pending_object_bytes")
	public long getPendingObjectBytes() {
		return pendingObjectBytes;
	}

	public void setPendingObjectBytes(long pendingObjectBytes) {
		this.pendingObjectBytes = pendingObjectBytes;
	}

	@NotNull
	@Column(name = "pending_object_version_count")
	public long getPendingObjectVersionCount() {
		return pendingObjectVersionCount;
	}

	public void setPendingObjectVersionCount(long pendingObjectVersionCount) {
		this.pendingObjectVersionCount = pendingObjectVersionCount;
	}

	@NotNull
	@Column(name = "pending_object_version_bytes")
	public long getPendingObjectVersionBytes() {
		return pendingObjectVersionBytes;
	}

	public void setPendingObjectVersionBytes(long pendingObjectVersionBytes) {
		this.pendingObjectVersionBytes = pendingObjectVersionBytes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (objectBytes ^ (objectBytes >>> 32));
		result = prime * result + (int) (objectCount ^ (objectCount >>> 32));
		result = prime * result + (int) (objectVersionBytes ^ (objectVersionBytes >>> 32));
		result = prime * result + (int) (objectVersionCount ^ (objectVersionCount >>> 32));
		result = prime * result + (int) (pendingObjectBytes ^ (pendingObjectBytes >>> 32));
		result = prime * result + (int) (pendingObjectCount ^ (pendingObjectCount >>> 32));
		result = prime * result + (int) (pendingObjectVersionBytes ^ (pendingObjectVersionBytes >>> 32));
		result = prime * result + (int) (pendingObjectVersionCount ^ (pendingObjectVersionCount >>> 32));
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
		ObjectStatistics other = (ObjectStatistics) obj;
		if (objectBytes != other.objectBytes)
			return false;
		if (objectCount != other.objectCount)
			return false;
		if (objectVersionBytes != other.objectVersionBytes)
			return false;
		if (objectVersionCount != other.objectVersionCount)
			return false;
		if (pendingObjectBytes != other.pendingObjectBytes)
			return false;
		if (pendingObjectCount != other.pendingObjectCount)
			return false;
		if (pendingObjectVersionBytes != other.pendingObjectVersionBytes)
			return false;
		if (pendingObjectVersionCount != other.pendingObjectVersionCount)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ObjectStatistics [objectCount=" + objectCount + ", objectBytes=" + objectBytes + ", objectVersionCount="
				+ objectVersionCount + ", objectVersionBytes=" + objectVersionBytes + ", pendingObjectCount="
				+ pendingObjectCount + ", pendingObjectBytes=" + pendingObjectBytes + ", pendingObjectVersionCount="
				+ pendingObjectVersionCount + ", pendingObjectVersionBytes=" + pendingObjectVersionBytes + "]";
	}

}
