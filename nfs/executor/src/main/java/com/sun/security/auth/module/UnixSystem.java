package com.sun.security.auth.module;

public class UnixSystem {
    public Long getUid() {
        return 0L;
    }
    public Long getGid() {
        return 0L;
    }
    public long[] getGroups() {
        return new long[]{0};
    }
}
