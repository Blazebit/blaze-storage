package com.blazebit.storage.nfs.rar;

import com.blazebit.storage.nfs.StorageAccess;
import com.blazebit.storage.nfs.spi.NfsServer;
import com.blazebit.storage.nfs.spi.NfsServerProvider;

import javax.resource.NotSupportedException;
import javax.resource.ResourceException;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.BootstrapContext;
import javax.resource.spi.Connector;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.endpoint.MessageEndpointFactory;
import javax.transaction.xa.XAResource;
import java.util.ServiceLoader;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

@Connector
public class NfsServerResourceAdapter implements ResourceAdapter, NfsServerResourceAdapterConfiguration {

    private static final Logger LOG = Logger.getLogger(NfsServerResourceAdapter.class.getName());
    private NfsServerProvider nfsServerProvider;
    private ExecutorService executorService;
    private NfsServer nfsSvc;

    @Override
    public void start(BootstrapContext ctx) throws ResourceAdapterInternalException {
        try {
            nfsServerProvider = ServiceLoader.load(NfsServerProvider.class).iterator().next();
            executorService = new WorkManagerExecutorService(ctx.getWorkManager());
        } catch (Exception ex) {
            throw new ResourceAdapterInternalException(ex);
        }
    }

    @Override
    public void stop() {
        try {
            nfsSvc.stop();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error while stopping", e);
        }
    }

    @Override
    public void endpointActivation(MessageEndpointFactory endpointFactory, ActivationSpec spec) throws ResourceException {
        if (nfsSvc != null) {
            throw new NotSupportedException("There can only be one NfsStorageAccess implementation! Illegal: " + endpointFactory.getEndpointClass());
        }
        if (!(spec instanceof NfsServerActivationSpec)) {
            throw new NotSupportedException("Invalid spec, Should be a " + NfsServerActivationSpec.class.getName() + " was: " + spec);
        }
        NfsServerActivationSpec nfsServerActivationSpec = (NfsServerActivationSpec) spec;
        nfsSvc = nfsServerProvider.create((StorageAccess) endpointFactory.createEndpoint(null), executorService);
    }

    @Override
    public void endpointDeactivation(MessageEndpointFactory endpointFactory, ActivationSpec spec) {

    }

    @Override
    public XAResource[] getXAResources(ActivationSpec[] specs) throws ResourceException {
        return new XAResource[0];
    }
}
