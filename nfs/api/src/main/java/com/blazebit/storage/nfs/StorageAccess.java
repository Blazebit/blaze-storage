package com.blazebit.storage.nfs;

import java.util.Collection;

public interface StorageAccess {
    public int read(String key, byte[] data, long offset, int count);
    public Collection<String> list(String key);
    public void remove(String key);
    public int write(String key, byte[] data, long offset, int count);
    public void move(String oldKey, String newKey);
    public FileStats stat(String key);
}
