package com.blazebit.storage.rest.impl.aop;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

import com.blazebit.annotation.AnnotationUtils;
import com.blazebit.storage.rest.api.aop.Authenticated;
import com.blazebit.storage.rest.api.aop.ResponseObject;

@Provider
public class AopDynamicFeature implements DynamicFeature {
	
	private static final Map<Class<? extends Annotation>, Class<?>> annotationProviders = new HashMap<Class<? extends Annotation>, Class<?>>();
	
	static {
		annotationProviders.put(Authenticated.class, AuthorizationRequestFilter.class);
		annotationProviders.put(ResponseObject.class, ResponseObjectResponseFilter.class);
	}

	@Override
	public void configure(ResourceInfo resourceInfo, FeatureContext context) {
		for (Map.Entry<Class<? extends Annotation>, Class<?>> annotationProviderEntry : annotationProviders.entrySet()) {
			Annotation annotation = AnnotationUtils.findAnnotation(resourceInfo.getResourceMethod(), resourceInfo.getResourceClass(), annotationProviderEntry.getKey());
			
			if (annotation != null) {
				context.register(annotationProviderEntry.getValue());
			}
		}
	}

}
