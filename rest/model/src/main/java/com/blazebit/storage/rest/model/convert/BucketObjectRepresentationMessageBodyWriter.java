package com.blazebit.storage.rest.model.convert;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.blazebit.storage.rest.model.BucketObjectRepresentation;

@Provider
@Produces(MediaType.WILDCARD)
public class BucketObjectRepresentationMessageBodyWriter implements MessageBodyWriter<BucketObjectRepresentation> {

	private static final int BUFFER_SIZE = 4096;
	
	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return BucketObjectRepresentation.class.equals(type);
	}

	@Override
	public long getSize(BucketObjectRepresentation t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(BucketObjectRepresentation t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
		put(httpHeaders, HttpHeaders.CONTENT_TYPE, t.getContentType());
		put(httpHeaders, HttpHeaders.CONTENT_DISPOSITION, t.getContentDisposition());
		put(httpHeaders, HttpHeaders.LAST_MODIFIED, t.getLastModified() == null ? null : t.getLastModified().getTime());
		
		InputStream input = t.getContent();
		byte[] buffer = new byte[BUFFER_SIZE];
	    int bytesRead;
	    while ((bytesRead = input.read(buffer)) != -1) {
	    	entityStream.write(buffer, 0, bytesRead);
	    }
	}
	
	private void put(MultivaluedMap<String, Object> httpHeaders, String key, Object value) {
		if (value != null) {
			httpHeaders.putSingle(key, value);
		}
	}

}
