package com.blazebit.storage.nfs.executor;

import com.blazebit.storage.nfs.StorageAccess;
import com.blazebit.storage.nfs.spi.NfsServer;
import com.blazebit.storage.nfs.spi.NfsServerProvider;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

public class NfsServerProviderImpl implements NfsServerProvider {

    @Override
    public NfsServer create(StorageAccess storageAccess, ExecutorService executorService) {
        try {
            return new NfsServerImpl(storageAccess, executorService);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
