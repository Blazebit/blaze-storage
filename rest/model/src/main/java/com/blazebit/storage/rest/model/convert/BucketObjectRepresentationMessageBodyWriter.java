package com.blazebit.storage.rest.model.convert;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.blazebit.storage.rest.model.BlazeStorageHeaders;
import com.blazebit.storage.rest.model.BucketObjectBaseRepresentation;
import com.blazebit.storage.rest.model.BucketObjectHeadRepresentation;
import com.blazebit.storage.rest.model.BucketObjectRepresentation;
import com.blazebit.storage.rest.model.BucketObjectUpdateRepresentation;

public class BucketObjectRepresentationMessageBodyWriter implements ResponseObjectAwareMessageBodyWriter<BucketObjectBaseRepresentation> {

	private static final int BUFFER_SIZE = 4096;
	
	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return BucketObjectBaseRepresentation.class.isAssignableFrom(type);
	}

	@Override
	public long getSize(BucketObjectBaseRepresentation t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public int getStatusCode(BucketObjectBaseRepresentation t, Class<?> type) {
		if (t instanceof BucketObjectRepresentation || t instanceof BucketObjectUpdateRepresentation) {
			return Response.Status.OK.getStatusCode();
		}
		
		return Response.Status.NO_CONTENT.getStatusCode();
	}

	@Override
	public void writeTo(BucketObjectBaseRepresentation t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
		put(httpHeaders, HttpHeaders.CONTENT_TYPE, t.getContentType());
		
		// This is required for Undertow, otherwise it won't allow actually writing to the output stream 
		if (t.getSize() > 0) {
			put(httpHeaders, HttpHeaders.CONTENT_LENGTH, t.getSize());
		}
		
		put(httpHeaders, HttpHeaders.CONTENT_DISPOSITION, t.getContentDisposition());
		put(httpHeaders, BlazeStorageHeaders.STORAGE_NAME, t.getStorageName());
		put(httpHeaders, BlazeStorageHeaders.STORAGE_OWNER, t.getStorageOwner());
		
		for (Map.Entry<String, String> tagEntry : t.getTags().entrySet()) {
			put(httpHeaders, BlazeStorageHeaders.TAG_PREFIX + tagEntry.getKey(), tagEntry.getValue());
		}
		
		if (t instanceof BucketObjectUpdateRepresentation) {
			BucketObjectUpdateRepresentation update = (BucketObjectUpdateRepresentation) t;
			put(httpHeaders, BlazeStorageHeaders.CONTENT_MD5, update.getContentMD5());
			put(httpHeaders, BlazeStorageHeaders.CONTENT_KEY, update.getExternalContentKey());

			InputStream input = update.getContent();
			if (input != null) {
				byte[] buffer = new byte[BUFFER_SIZE];
			    int bytesRead;
			    while ((bytesRead = input.read(buffer)) != -1) {
			    	entityStream.write(buffer, 0, bytesRead);
			    }
			}
		} else {
			BucketObjectHeadRepresentation head = (BucketObjectHeadRepresentation) t;
			put(httpHeaders, HttpHeaders.LAST_MODIFIED, head.getLastModified() == null ? null : head.getLastModified().getTime());
			
			if (t instanceof BucketObjectRepresentation) {
				InputStream input = ((BucketObjectRepresentation) t).getContent();
				byte[] buffer = new byte[BUFFER_SIZE];
			    int bytesRead;
			    while ((bytesRead = input.read(buffer)) != -1) {
			    	entityStream.write(buffer, 0, bytesRead);
			    }
			}
		}
	}
	
	private void put(MultivaluedMap<String, Object> httpHeaders, String key, Object value) {
		if (value != null) {
			httpHeaders.putSingle(key, value);
		}
	}

}
