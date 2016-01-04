package com.blazebit.storage.core.api.spi;

import java.io.InputStream;


public interface StorageProvider {
	
	public Object getStorageIdentifier();

	public InputStream getObject(String externalKey);
	
	public void deleteObject(String externalKey);
	
	public String createObject(InputStream content);
	
	public long putObject(String externalKey, InputStream content);

	public String copyObject(StorageProvider sourceStorageProvider, String contentKey);
	
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
