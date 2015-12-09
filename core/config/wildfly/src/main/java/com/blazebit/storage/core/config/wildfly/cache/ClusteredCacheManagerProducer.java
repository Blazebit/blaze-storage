package com.blazebit.storage.core.config.wildfly.cache;

import java.net.URI;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.infinispan.manager.EmbeddedCacheManager;

import com.blazebit.storage.core.config.api.cache.ClusteredCache;

/**
 * This is a singleton because it seems the cache manager can't be injected into CDI beans.
 * It's a startup singleton because we need to eagerly start the cache so that the synchronization starts.
 *   
 * @author Christian Beikov
 */
@Startup
@Singleton
public class ClusteredCacheManagerProducer {

    @Resource(lookup = "java:jboss/infinispan/container/storage")
    private EmbeddedCacheManager cacheManager;
    
    @PostConstruct
    public void init() {
    	// Start caches on startup
    	for (String name : cacheManager.getCacheNames()) {
    		cacheManager.getCache(name);
    	}
    }
    
    @Produces
    @ClusteredCache
    @ApplicationScoped
    public CacheManager produceJcacheCacheManager() {
    	// Note that we don't close the cache manager because the underlying resource is container managed
    	return new org.infinispan.jcache.JCacheManager(URI.create("storage"), cacheManager, Caching.getCachingProvider());
    }
}