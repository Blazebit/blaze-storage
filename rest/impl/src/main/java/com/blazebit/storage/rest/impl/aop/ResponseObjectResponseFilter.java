package com.blazebit.storage.rest.impl.aop;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyWriter;

import com.blazebit.storage.rest.model.convert.BucketRepresentationMessageBodyWriter;

public class ResponseObjectResponseFilter implements ContainerResponseFilter {
	
	private static final List<MessageBodyWriter<?>> responseObjectMessageWriter = Arrays.asList(
			(MessageBodyWriter<?>) new BucketRepresentationMessageBodyWriter()
	);

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		@SuppressWarnings("unchecked")
		MessageBodyWriter<Object> writer = (MessageBodyWriter<Object>) getMessageWriter(responseContext.getEntityClass());
		if (writer != null) {
			responseContext.setStatus(Response.Status.NO_CONTENT.getStatusCode());
			writer.writeTo(responseContext.getEntity(), responseContext.getEntityClass(), null, null, null, responseContext.getHeaders(), responseContext.getEntityStream());
		}
	}

	@SuppressWarnings("unchecked")
	private <T> MessageBodyWriter<T> getMessageWriter(Class<T> responseType) {
		for (MessageBodyWriter<?> writer : responseObjectMessageWriter) {
			if (writer.isWriteable(responseType, null, null, null)) {
				return (MessageBodyWriter<T>) writer;
			}
		}
		
		return null;
	}
	
}