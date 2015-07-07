package com.blazebit.storage.core.api;

import java.io.InputStream;

public interface FileStorageObject {

	public String getName();
	
	public InputStream getInputStream();
}
