package com.blazebit.storage.rest.model.convert;

import java.util.Map;

import javax.ws.rs.ext.ParamConverter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TagMapParamConverter implements ParamConverter<Map<String, String>> {

	private final ObjectMapper mapper = new ObjectMapper();
	
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, String> fromString(String value) {
		try {
			return mapper.readValue(value, Map.class);
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	@Override
	public String toString(Map<String, String> value) {
		try {
			return mapper.writeValueAsString(value);
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex);
		}
	}

}
