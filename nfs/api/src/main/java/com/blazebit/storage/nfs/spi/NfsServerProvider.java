package com.blazebit.storage.nfs.spi;

import com.blazebit.storage.nfs.StorageAccess;

import java.util.concurrent.ExecutorService;

public interface NfsServerProvider {
    NfsServer create(StorageAccess storageAccess, ExecutorService executorService);
}
