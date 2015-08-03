package com.blazebit.storage.core.impl.actor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.blazebit.storage.core.api.event.BucketObjectDeletedEvent;
import com.blazebit.storage.core.model.jpa.BucketObjectId;

@Singleton
public class BucketObjectDeleterActor extends AbstractActor {
	
	@Inject
	private ManagedScheduledExecutorService executorService;
	
	private BlockingQueue<BucketObjectId> bucketObjectsToDelete = new LinkedBlockingQueue<>();
	
	public void onBucketObjectDeleted(@Observes(during = TransactionPhase.AFTER_COMPLETION) BucketObjectDeletedEvent event) {
		addBucketObject(event.getBucketObjectId());
	}
	
	public void addBucketObject(BucketObjectId bucketObjectId) {
		bucketObjectsToDelete.add(bucketObjectId);
	}

	@Override
	protected ManagedScheduledExecutorService getExecutorService() {
		return executorService;
	}

	@Override
	protected long getInitialDelay() {
		return 0L;
	}
	
	@Override
	protected long getPeriod() {
		return 10L;
	}
	
	@Override
	protected TimeUnit getTimeUnit() {
		return TimeUnit.SECONDS;
	}

	@Override
	protected void work() {
		BucketObjectId bucketId = bucketObjectsToDelete.poll();

		if (bucketId == null) {
			// TODO: Load bucket objects to delete
			return;
		}
		
		// TODO: delete bucket objects
	}
}
