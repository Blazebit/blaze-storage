package com.blazebit.storage.rest.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Future;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.RedirectionException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.ServiceUnavailableException;
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
		int status = response.getStatus();
		if (status >= 200 && status < 300) {
			boolean success = false;
			try {
				// this wrapping serves as a workaround to prevent the GC from closing the response and with it the underlying inputstream
				InputStream responseBody = new ResponseInputStreamWrapper(response.readEntity(InputStream.class), response);
				T result = reader.readFrom(responseType, null, null, null, response.getStringHeaders(), responseBody);
				success = true;
				return result;
			} catch (IOException e) {
				throw new WebApplicationException(e);
			} catch (RuntimeException e) {
				throw new WebApplicationException(e);
			} finally {
				if (!success) {
					response.close();
				}
			}
		}
		try {
			if (status >= 300 && status < 400) {
				throw new RedirectionException(response);
			}

			return handleErrorStatus(response);
		} finally {
			response.close();
		}
	}

	private static <T> T handleErrorStatus(Response response) {
		final int status = response.getStatus();
		switch (status) {
		case 400:
			throw new BadRequestException(response);
		case 401:
			throw new NotAuthorizedException(response);
		case 404:
			throw new NotFoundException(response);
		case 405:
			throw new NotAllowedException(response);
		case 406:
			throw new NotAcceptableException(response);
		case 415:
			throw new NotSupportedException(response);
		case 500:
			throw new InternalServerErrorException(response);
		case 503:
			throw new ServiceUnavailableException(response);
		default:
			break;
		}

		if (status >= 400 && status < 500){
			throw new ClientErrorException(response);
		} else if (status >= 500) {
			throw new ServerErrorException(response);
		}

		throw new WebApplicationException(response);
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
			Response response = delegate.invoke();
			return getResponseObject(reader, responseClass, response);
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
