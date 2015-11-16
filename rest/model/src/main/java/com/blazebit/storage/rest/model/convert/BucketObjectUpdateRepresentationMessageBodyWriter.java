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

import com.blazebit.storage.rest.model.BucketObjectUpdateRepresentation;

@Provider
@Produces(MediaType.WILDCARD)
public class BucketObjectUpdateRepresentationMessageBodyWriter implements MessageBodyWriter<BucketObjectUpdateRepresentation> {

	private final BucketObjectRepresentationMessageBodyWriter writer = new BucketObjectRepresentationMessageBodyWriter();

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return BucketObjectUpdateRepresentation.class.isAssignableFrom(type);
	}

	@Override
	public long getSize(BucketObjectUpdateRepresentation t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(BucketObjectUpdateRepresentation t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
		writer.writeTo(t, type, genericType, annotations, mediaType, httpHeaders, entityStream);
	}
	
}
