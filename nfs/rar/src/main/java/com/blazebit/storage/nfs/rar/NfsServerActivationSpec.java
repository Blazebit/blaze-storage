package com.blazebit.storage.nfs.rar;

import com.blazebit.storage.nfs.StorageAccess;

import javax.resource.ResourceException;
import javax.resource.spi.Activation;
import javax.resource.spi.ActivationSpec;
import javax.resource.spi.InvalidPropertyException;
import javax.resource.spi.ResourceAdapter;
import java.io.Serializable;

@Activation(messageListeners = StorageAccess.class)
public class NfsServerActivationSpec implements ActivationSpec, NfsServerResourceAdapterConfiguration, Serializable {

    private NfsServerResourceAdapter ra;

    @Override
    public void validate() throws InvalidPropertyException {

    }

    @Override
    public ResourceAdapter getResourceAdapter() {
        return ra;
    }

    @Override
    public void setResourceAdapter(ResourceAdapter ra) throws ResourceException {
        this.ra = (NfsServerResourceAdapter) ra;
    }
}
