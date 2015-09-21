package com.blazebit.storage.rest.client;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.MessageBodyReader;

public class ResponseObjectWebTarget implements WebTarget {

	private final List<MessageBodyReader<?>> responseObjectMessageReader;
	private WebTarget delegate;
	
	public ResponseObjectWebTarget(WebTarget delegate, List<MessageBodyReader<?>> responseObjectMessageReader) {
		this.delegate = delegate;
		this.responseObjectMessageReader = responseObjectMessageReader;
	}

	private WebTarget getOrCreateDelegate(WebTarget newTarget) {
		if (delegate != newTarget) {
			return new ResponseObjectWebTarget(newTarget, responseObjectMessageReader);
		}
		
		// The client proxy code kind of requires new instances...
		return new ResponseObjectWebTarget(newTarget, responseObjectMessageReader);
//		return this;
	}
	
	/*
	 * Wrap for response object support
	 */

	public Builder request() {
		return new ResponseObjectBuilder(delegate.request(), responseObjectMessageReader);
	}

	public Builder request(String... acceptedResponseTypes) {
		return new ResponseObjectBuilder(delegate.request(acceptedResponseTypes), responseObjectMessageReader);
	}

	public Builder request(MediaType... acceptedResponseTypes) {
		return new ResponseObjectBuilder(delegate.request(acceptedResponseTypes), responseObjectMessageReader);
	}
	
	/*
	 * Simple delegate
	 */

	public URI getUri() {
		return delegate.getUri();
	}

	public UriBuilder getUriBuilder() {
		return delegate.getUriBuilder();
	}

	public Configuration getConfiguration() {
		return delegate.getConfiguration();
	}

	/*
	 * Wrapping delegates 
	 */

	public WebTarget path(String path) {
		return getOrCreateDelegate(delegate.path(path));
	}

	public WebTarget resolveTemplate(String name, Object value) {
		return getOrCreateDelegate(delegate.resolveTemplate(name, value));
	}

	public WebTarget resolveTemplate(String name, Object value, boolean encodeSlashInPath) {
		return getOrCreateDelegate(delegate.resolveTemplate(name, value, encodeSlashInPath));
	}

	public WebTarget resolveTemplateFromEncoded(String name, Object value) {
		return getOrCreateDelegate(delegate.resolveTemplateFromEncoded(name, value));
	}

	public WebTarget resolveTemplates(Map<String, Object> templateValues) {
		return getOrCreateDelegate(delegate.resolveTemplates(templateValues));
	}

	public WebTarget resolveTemplates(Map<String, Object> templateValues, boolean encodeSlashInPath) {
		return getOrCreateDelegate(delegate.resolveTemplates(templateValues, encodeSlashInPath));
	}

	public WebTarget property(String name, Object value) {
		return getOrCreateDelegate(delegate.property(name, value));
	}

	public WebTarget register(Class<?> componentClass) {
		return getOrCreateDelegate(delegate.register(componentClass));
	}

	public WebTarget resolveTemplatesFromEncoded(Map<String, Object> templateValues) {
		return getOrCreateDelegate(delegate.resolveTemplatesFromEncoded(templateValues));
	}

	public WebTarget register(Class<?> componentClass, int priority) {
		return getOrCreateDelegate(delegate.register(componentClass, priority));
	}

	public WebTarget matrixParam(String name, Object... values) {
		return getOrCreateDelegate(delegate.matrixParam(name, values));
	}

	public WebTarget register(Class<?> componentClass, Class<?>... contracts) {
		return getOrCreateDelegate(delegate.register(componentClass, contracts));
	}

	public WebTarget queryParam(String name, Object... values) {
		return getOrCreateDelegate(delegate.queryParam(name, values));
	}

	public WebTarget register(Class<?> componentClass, Map<Class<?>, Integer> contracts) {
		return getOrCreateDelegate(delegate.register(componentClass, contracts));
	}

	public WebTarget register(Object component) {
		return getOrCreateDelegate(delegate.register(component));
	}

	public WebTarget register(Object component, int priority) {
		return getOrCreateDelegate(delegate.register(component, priority));
	}

	public WebTarget register(Object component, Class<?>... contracts) {
		return getOrCreateDelegate(delegate.register(component, contracts));
	}

	public WebTarget register(Object component, Map<Class<?>, Integer> contracts) {
		return getOrCreateDelegate(delegate.register(component, contracts));
	}

}
