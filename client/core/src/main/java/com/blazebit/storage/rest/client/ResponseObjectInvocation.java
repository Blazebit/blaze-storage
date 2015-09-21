package com.blazebit.storage.rest.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Future;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyReader;

public class ResponseObjectInvocation implements Invocation {

	private final List<MessageBodyReader<?>> responseObjectMessageReader;
	private Invocation delegate;
	
	public ResponseObjectInvocation(Invocation delegate, List<MessageBodyReader<?>> responseObjectMessageReader) {
		this.delegate = delegate;
		this.responseObjectMessageReader = responseObjectMessageReader;
	}

	private Invocation getOrCreateDelegate(Invocation newInvocation) {
		if (delegate != newInvocation) {
			return new ResponseObjectInvocation(newInvocation, responseObjectMessageReader);
		}
		
		return this;
	}
	
	@SuppressWarnings("unchecked")
	private <T> MessageBodyReader<T> getMessageReader(Class<T> responseType) {
		for (MessageBodyReader<?> reader : responseObjectMessageReader) {
			if (reader.isReadable(responseType, null, null, null)) {
				return (MessageBodyReader<T>) reader;
			}
		}
		
		return null;
	}

	private <T> T getResponseObject(MessageBodyReader<T> reader, Class<T> responseType, Response response) {
		try {
			return reader.readFrom(responseType, null, null, null, response.getStringHeaders(), response.readEntity(InputStream.class));
		} catch (IOException e) {
			throw new WebApplicationException(e);
		}
	}
	
	/*
	 * Wrap for response object support
	 */

	public Response invoke() {
		return delegate.invoke();
	}

	public <T> T invoke(Class<T> responseType) {
		MessageBodyReader<T> reader = getMessageReader(responseType);
		
		if (reader != null) {
			return getResponseObject(reader, responseType, delegate.invoke());
		}
		
		return delegate.invoke(responseType);
	}

	public <T> T invoke(GenericType<T> responseType) {
		@SuppressWarnings("unchecked")
		Class<T> responseClass = (Class<T>) responseType.getRawType();
		MessageBodyReader<T> reader = getMessageReader(responseClass);
		
		if (reader != null) {
			return getResponseObject(reader, responseClass, delegate.invoke());
		}
		
		return delegate.invoke(responseType);
	}

	public Future<Response> submit() {
		return delegate.submit();
	}

	public <T> Future<T> submit(Class<T> responseType) {
		// TODO: implement wrapping
		return delegate.submit(responseType);
	}

	public <T> Future<T> submit(GenericType<T> responseType) {
		// TODO: implement wrapping
		return delegate.submit(responseType);
	}

	public <T> Future<T> submit(InvocationCallback<T> callback) {
		// TODO: implement wrapping
		return delegate.submit(callback);
	}

	/*
	 * Wrapping delegates 
	 */
	
	public Invocation property(String name, Object value) {
		return getOrCreateDelegate(delegate.property(name, value));
	}
}
