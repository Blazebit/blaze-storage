package com.blazebit.storage.testsuite.common.executor;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.inject.Produces;

public class ManagedScheduledExecutorServiceProducer {

	@Produces
	@Resource
	ManagedScheduledExecutorService executorService;
}
