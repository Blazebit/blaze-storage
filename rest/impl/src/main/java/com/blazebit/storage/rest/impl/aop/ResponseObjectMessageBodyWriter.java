package com.blazebit.storage.rest.impl.aop;

import com.blazebit.storage.rest.api.aop.ResponseObject;
import com.blazebit.storage.rest.model.convert.ResponseObjectAwareMessageBodyWriter;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces(MediaType.WILDCARD)
public class ResponseObjectMessageBodyWriter implements MessageBodyWriter<Object> {

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		for (int i = 0; i < annotations.length; i++) {
			if (ResponseObject.class == annotations[i].annotationType()) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public long getSize(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public void writeTo(Object t, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
		@SuppressWarnings("unchecked")
		ResponseObjectAwareMessageBodyWriter<Object> writer = (ResponseObjectAwareMessageBodyWriter<Object>) ResponseObjectResponseFilter.getMessageWriter(type);
		writer.writeTo(t, type, genericType, annotations, mediaType, httpHeaders, entityStream);
	}

}
