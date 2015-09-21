package com.blazebit.storage.rest.client;

import java.util.List;
import java.util.Locale;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyReader;

public class ResponseObjectBuilder implements Builder {

	private final List<MessageBodyReader<?>> responseObjectMessageReader;
	private Builder delegate;
	
	public ResponseObjectBuilder(Builder delegate, List<MessageBodyReader<?>> responseObjectMessageReader) {
		this.delegate = delegate;
		this.responseObjectMessageReader = responseObjectMessageReader;
	}

	private Builder getOrCreateDelegate(Builder newBuilder) {
		if (delegate != newBuilder) {
			return new ResponseObjectBuilder(newBuilder, responseObjectMessageReader);
		}
		
		return this;
	}
	
	/*
	 * Wrap for response object support
	 */

	public Invocation build(String method) {
		return new ResponseObjectInvocation(delegate.build(method), responseObjectMessageReader);
	}

	public Invocation build(String method, Entity<?> entity) {
		return new ResponseObjectInvocation(delegate.build(method, entity), responseObjectMessageReader);
	}

	public Invocation buildGet() {
		return new ResponseObjectInvocation(delegate.buildGet(), responseObjectMessageReader);
	}

	public Invocation buildDelete() {
		return new ResponseObjectInvocation(delegate.buildDelete(), responseObjectMessageReader);
	}

	public Invocation buildPost(Entity<?> entity) {
		return new ResponseObjectInvocation(delegate.buildPost(entity), responseObjectMessageReader);
	}

	public Invocation buildPut(Entity<?> entity) {
		return new ResponseObjectInvocation(delegate.buildPut(entity), responseObjectMessageReader);
	}

	public AsyncInvoker async() {
		// TODO: implement wrapping
		return delegate.async();
	}
	
	/*
	 * Delegate and wrap under the hood
	 */

	public Response get() {
		return buildGet().invoke();
	}

	public <T> T get(Class<T> responseType) {
		return buildGet().invoke(responseType);
	}

	public <T> T get(GenericType<T> responseType) {
		return buildGet().invoke(responseType);
	}

	public Response put(Entity<?> entity) {
		return buildPut(entity).invoke();
	}

	public <T> T put(Entity<?> entity, Class<T> responseType) {
		return buildPut(entity).invoke(responseType);
	}

	public <T> T put(Entity<?> entity, GenericType<T> responseType) {
		return buildPut(entity).invoke(responseType);
	}

	public Response post(Entity<?> entity) {
		return buildPost(entity).invoke();
	}

	public <T> T post(Entity<?> entity, Class<T> responseType) {
		return buildPost(entity).invoke(responseType);
	}

	public <T> T post(Entity<?> entity, GenericType<T> responseType) {
		return buildPost(entity).invoke(responseType);
	}

	public Response delete() {
		return buildDelete().invoke();
	}

	public <T> T delete(Class<T> responseType) {
		return buildDelete().invoke(responseType);
	}

	public <T> T delete(GenericType<T> responseType) {
		return buildDelete().invoke(responseType);
	}

	public Response head() {
		return build(HttpMethod.HEAD).invoke();
	}

	public Response options() {
		return build(HttpMethod.OPTIONS).invoke();
	}

	public <T> T options(Class<T> responseType) {
		return build(HttpMethod.OPTIONS).invoke(responseType);
	}

	public <T> T options(GenericType<T> responseType) {
		return build(HttpMethod.OPTIONS).invoke(responseType);
	}

	public Response trace() {
		return build("TRACE").invoke();
	}

	public <T> T trace(Class<T> responseType) {
		return build("TRACE").invoke(responseType);
	}

	public <T> T trace(GenericType<T> responseType) {
		return build("TRACE").invoke(responseType);
	}

	public Response method(String name) {
		return build(name).invoke();
	}

	public <T> T method(String name, Class<T> responseType) {
		return build(name).invoke(responseType);
	}

	public <T> T method(String name, GenericType<T> responseType) {
		return build(name).invoke(responseType);
	}

	public Response method(String name, Entity<?> entity) {
		return build(name, entity).invoke();
	}

	public <T> T method(String name, Entity<?> entity, Class<T> responseType) {
		return build(name, entity).invoke(responseType);
	}

	public <T> T method(String name, Entity<?> entity, GenericType<T> responseType) {
		return build(name, entity).invoke(responseType);
	}
	
	/*
	 * Wrapping delegates 
	 */

	public Builder accept(String... mediaTypes) {
		return getOrCreateDelegate(delegate.accept(mediaTypes));
	}

	public Builder accept(MediaType... mediaTypes) {
		return getOrCreateDelegate(delegate.accept(mediaTypes));
	}

	public Builder acceptLanguage(Locale... locales) {
		return getOrCreateDelegate(delegate.acceptLanguage(locales));
	}

	public Builder acceptLanguage(String... locales) {
		return getOrCreateDelegate(delegate.acceptLanguage(locales));
	}

	public Builder acceptEncoding(String... encodings) {
		return getOrCreateDelegate(delegate.acceptEncoding(encodings));
	}

	public Builder cookie(Cookie cookie) {
		return getOrCreateDelegate(delegate.cookie(cookie));
	}

	public Builder cookie(String name, String value) {
		return getOrCreateDelegate(delegate.cookie(name, value));
	}

	public Builder cacheControl(CacheControl cacheControl) {
		return getOrCreateDelegate(delegate.cacheControl(cacheControl));
	}

	public Builder header(String name, Object value) {
		return getOrCreateDelegate(delegate.header(name, value));
	}

	public Builder headers(MultivaluedMap<String, Object> headers) {
		return getOrCreateDelegate(delegate.headers(headers));
	}

	public Builder property(String name, Object value) {
		return getOrCreateDelegate(delegate.property(name, value));
	}

}
