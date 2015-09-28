package com.blazebit.storage.rest.model.convert;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import com.blazebit.storage.rest.model.BlazeStorageHeaders;
import com.blazebit.storage.rest.model.BucketHeadRepresentation;
import com.blazebit.storage.rest.model.BucketRepresentation;
import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
@Produces(MediaType.WILDCARD)
public class BucketRepresentationMessageBodyWriter implements MessageBodyWriter<BucketHeadRepresentation> {

	private final ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return BucketHeadRepresentation.class.isAssignableFrom(type);
	}

	@Override
	public long getSize(BucketHeadRepresentation t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(BucketHeadRepresentation t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
		put(httpHeaders, BlazeStorageHeaders.BUCKET_NAME, t.getName());
		put(httpHeaders, BlazeStorageHeaders.OWNER_KEY, t.getOwnerKey());
		put(httpHeaders, BlazeStorageHeaders.DEFAULT_STORAGE_NAME, t.getDefaultStorageName());
		put(httpHeaders, BlazeStorageHeaders.DEFAULT_STORAGE_OWNER, t.getDefaultStorageOwner());
		put(httpHeaders, BlazeStorageHeaders.TIMESTAMP, t.getCreationDate().getTimeInMillis());
		put(httpHeaders, BlazeStorageHeaders.NEXT_MARKER, t.getNextMarker());
		put(httpHeaders, BlazeStorageHeaders.OBJECT_BYTES, t.getStatistics().getObjectBytes());
		put(httpHeaders, BlazeStorageHeaders.OBJECT_COUNT, t.getStatistics().getObjectCount());
		
		if (t instanceof BucketRepresentation) {
			mapper.writeValue(entityStream, ((BucketRepresentation) t).getContents());
		}
	}
	
	private void put(MultivaluedMap<String, Object> httpHeaders, String key, Object value) {
		if (value != null) {
			httpHeaders.putSingle(key, value);
		}
	}

}
