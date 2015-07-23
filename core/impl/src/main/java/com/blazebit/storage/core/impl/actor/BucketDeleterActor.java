package com.blazebit.storage.core.impl.actor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.blazebit.storage.core.api.actor.BucketDeleter;
import com.blazebit.storage.core.api.event.BucketDeletedEvent;

@Singleton
public class BucketDeleterActor extends AbstractActor implements BucketDeleter {
	
	@Inject
	private ManagedScheduledExecutorService executorService;
	
	private BlockingQueue<String> bucketsToDelete = new LinkedBlockingQueue<>();
	
	public void onBucketObjectDeleted(@Observes(during = TransactionPhase.AFTER_COMPLETION) BucketDeletedEvent event) {
		addBucket(event.getBucketId());
	}
	
	@Override
	public void addBucket(String bucketId) {
		bucketsToDelete.add(bucketId);
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
		String bucketId = bucketsToDelete.poll();

		if (bucketId == null) {
			// TODO: Load buckets to delete
			return;
		}
		
		// TODO: delete bucket contents
	}
}
