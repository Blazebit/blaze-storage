package com.blazebit.storage.rest.impl.aop;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import com.blazebit.storage.rest.model.convert.BucketObjectRepresentationMessageBodyWriter;
import com.blazebit.storage.rest.model.convert.BucketRepresentationMessageBodyWriter;
import com.blazebit.storage.rest.model.convert.ResponseObjectAwareMessageBodyWriter;

public class ResponseObjectResponseFilter implements ContainerResponseFilter {
	
	private static final List<ResponseObjectAwareMessageBodyWriter<?>> responseObjectMessageWriter = Arrays.asList(
			(ResponseObjectAwareMessageBodyWriter<?>) new BucketRepresentationMessageBodyWriter(), (ResponseObjectAwareMessageBodyWriter<?>) new BucketObjectRepresentationMessageBodyWriter()
	);

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		@SuppressWarnings("unchecked")
		ResponseObjectAwareMessageBodyWriter<Object> writer = (ResponseObjectAwareMessageBodyWriter<Object>) getMessageWriter(responseContext.getEntityClass());
		if (writer != null) {
			Object entity = responseContext.getEntity();
			Class<?> entityClass = responseContext.getEntityClass();
			responseContext.setStatus(writer.getStatusCode(entity, entityClass));
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> ResponseObjectAwareMessageBodyWriter<T> getMessageWriter(Class<T> responseType) {
		for (ResponseObjectAwareMessageBodyWriter<?> writer : responseObjectMessageWriter) {
			if (writer.isWriteable(responseType, null, null, null)) {
				return (ResponseObjectAwareMessageBodyWriter<T>) writer;
			}
		}
		
		return null;
	}
	
}