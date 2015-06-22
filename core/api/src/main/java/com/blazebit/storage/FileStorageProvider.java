package com.blazebit.storage;

import java.io.InputStream;


public interface FileStorageProvider {

	public FileStorageObject getObject(String path);
	
	public FileStorageObject putObject(String path, InputStream content);
	
	public String getScheme();
	
}
