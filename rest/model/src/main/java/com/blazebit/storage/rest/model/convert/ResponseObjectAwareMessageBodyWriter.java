package com.blazebit.storage.rest.model.convert;

import javax.ws.rs.ext.MessageBodyWriter;

public interface ResponseObjectAwareMessageBodyWriter<T> extends MessageBodyWriter<T> {

	public int getStatusCode(T t, Class<?> type);
}
