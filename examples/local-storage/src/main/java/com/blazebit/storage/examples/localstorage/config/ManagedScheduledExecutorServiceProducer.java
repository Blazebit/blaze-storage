package com.blazebit.storage.examples.localstorage.config;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.inject.Produces;

public class ManagedScheduledExecutorServiceProducer {

	@Produces
	@Resource
	ManagedScheduledExecutorService executorService;
}
