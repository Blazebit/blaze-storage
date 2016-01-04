package com.blazebit.storage.rest.model.multipart;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

public interface InputPart {

	public InputStream getInputStream() throws IOException;
	
	public MediaType getMediaType();
	
	public void delete() throws IOException;
	
	public MultivaluedMap<String, String> getHeaders();
}
