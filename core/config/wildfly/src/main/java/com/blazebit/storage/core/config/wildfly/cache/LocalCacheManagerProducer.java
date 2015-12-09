package com.blazebit.storage.core.config.wildfly.cache;

import java.net.URI;
import java.util.logging.Logger;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

import com.blazebit.storage.core.config.api.cache.LocalCache;

public class LocalCacheManagerProducer {
	
	private static final Logger LOG = Logger.getLogger(LocalCacheManagerProducer.class.getName());
	
	@Produces
	@LocalCache
	@ApplicationScoped
	CacheManager produceCacheManager() {
		String infinispanConfiguration = "META-INF/example-infinispan.xml";
		CacheManager cacheManager = Caching.getCachingProvider().getCacheManager(URI.create(infinispanConfiguration), LocalCacheManagerProducer.class.getClassLoader());
		LOG.info("Using cache configuration: " + infinispanConfiguration);
		return cacheManager;
	}
    
    public void dispose(@Disposes @LocalCache CacheManager cacheManager) {
    	cacheManager.close();
    }
}