package com.blazebit.storage.core.api.spi;

import java.io.InputStream;


public interface StorageProvider {

	public InputStream getObject(String path);
	
	public void deleteObject(String path);
	
	public long putObject(String path, InputStream content);
	
	public long getTotalSpace();
	
	public long getUsableSpace();
	
	public long getUnallocatedSpace();

    /**
     * Returns whether the storage provider supports the delete operation or not.
     * 
     * @return True if the delete operation is supported, otherwise false.
     */
    public boolean supportsDelete();
    
    /**
     * Returns whether the storage provider supports the put operation or not.
     * 
     * @return True if the put operation is supported, otherwise false.
     */
    public boolean supportsPut();
	
}
