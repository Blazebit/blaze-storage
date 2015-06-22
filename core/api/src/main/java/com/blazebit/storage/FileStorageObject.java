package com.blazebit.storage;

import java.io.InputStream;

public interface FileStorageObject {

	public String getName();
	
	public InputStream getInputStream();
}
