package com.blazebit.storage.nfs;

import java.util.List;

public interface FileStats {
    public boolean isFile();
    public boolean isDirectory();
    public long getSize();
}
