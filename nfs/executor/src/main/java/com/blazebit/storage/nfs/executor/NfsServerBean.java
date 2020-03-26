package com.blazebit.storage.nfs.executor;

import com.blazebit.storage.nfs.StorageAccess;
import com.blazebit.storage.nfs.spi.NfsServer;
import com.blazebit.storage.nfs.spi.NfsServerProvider;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.inject.Inject;
import java.util.ServiceLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Startup
public class NfsServerBean {

    private static final Logger LOG = Logger.getLogger(NfsServerBean.class.getName());

    @Resource
    private ManagedThreadFactory managedThreadFactory;
    @Inject
    private StorageAccess storageAccess;

    private NfsServer nfsServer;

    @PostConstruct
    void init() {
        ExecutorService executorService = Executors.newFixedThreadPool(4, managedThreadFactory);
        nfsServer = ServiceLoader.load(NfsServerProvider.class).iterator().next().create(storageAccess, executorService);
        try {
            nfsServer.start();
        } catch (Exception e) {
            throw new RuntimeException("Couldn't start NFS Server", e);
        }
    }

    @PreDestroy
    void destroy() {
        try {
            nfsServer.stop();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error while stopping NFS server", e);
        }
    }
}
