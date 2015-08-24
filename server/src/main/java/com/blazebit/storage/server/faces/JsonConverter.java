package com.blazebit.storage.server.faces;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.codehaus.jackson.map.ObjectMapper;

public class JsonConverter implements Converter {

	private static final ObjectMapper jsonMapper = new ObjectMapper();
	
	private final Class<?> clazz;
	
	public JsonConverter(Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null) {
			return null;
		}
		
		try {
			return jsonMapper.writeValueAsString(value);
		} catch (Exception e) {
			throw new ConverterException(e);
		}
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value == null) {
			return null;
		}

		try {
			return jsonMapper.readValue(value, clazz);
		} catch (Exception e) {
			throw new ConverterException(e);
		}
	}
}
