package com.blazebit.storage.rest.model.convert;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import com.blazebit.storage.rest.model.BucketObjectBaseRepresentation;
import com.blazebit.storage.rest.model.BucketObjectUpdateRepresentation;

@Provider
@Consumes(MediaType.WILDCARD)
public class BucketObjectUpdateRepresentationMessageBodyReader implements MessageBodyReader<BucketObjectUpdateRepresentation> {

	private final BucketObjectRepresentationMessageBodyReader reader = new BucketObjectRepresentationMessageBodyReader();

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return BucketObjectUpdateRepresentation.class.isAssignableFrom(type);
	}

	@Override
	@SuppressWarnings("unchecked")
	public BucketObjectUpdateRepresentation readFrom(Class<BucketObjectUpdateRepresentation> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
		return (BucketObjectUpdateRepresentation) reader.readFrom((Class<BucketObjectBaseRepresentation>) (Class<?>) type, genericType, annotations, mediaType, httpHeaders, entityStream);
	}
	
}
