package com.blazebit.storage.rest.model.convert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

@Provider
public class TagMapParamConverterProvider implements ParamConverterProvider {

	private final TagMapParamConverter converter = new TagMapParamConverter();
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
		if (rawType.equals(Map.class)) {
			return (ParamConverter<T>) converter;
		}
		
		return null;
	}

}
