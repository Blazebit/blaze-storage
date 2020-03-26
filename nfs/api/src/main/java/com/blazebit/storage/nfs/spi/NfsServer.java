package com.blazebit.storage.nfs.spi;

public interface NfsServer {
    void start() throws Exception;
    void stop() throws Exception;
}
