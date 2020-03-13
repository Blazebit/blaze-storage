package com.blazebit.storage.core.api.spi;

public class StorageResult {

    private final String externalKey;
    private final byte[] md5Checksum;
    private final long size;

    public StorageResult(String externalKey, byte[] md5Checksum, long size) {
        this.externalKey = externalKey;
        this.md5Checksum = md5Checksum;
        this.size = size;
    }

    public String getExternalKey() {
        return externalKey;
    }

    public byte[] getMd5Checksum() {
        return md5Checksum;
    }

    public long getSize() {
        return size;
    }

    public StorageResult withExternalKey(String externalKey) {
        return new StorageResult(externalKey, md5Checksum, size);
    }
}
