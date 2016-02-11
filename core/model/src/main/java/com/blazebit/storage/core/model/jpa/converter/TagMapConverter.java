package com.blazebit.storage.core.model.jpa.converter;

import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;

@Converter
public class TagMapConverter implements AttributeConverter<Map/*<String, String>*/, String> {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final MapType mapType = mapper.getTypeFactory().constructMapType(TreeMap.class, String.class, String.class);

	@Override
	@SuppressWarnings("unchecked")
	public String convertToDatabaseColumn(Map/*<String, String>*/ attribute) {
		if (attribute == null) {
			return null;
		}
		
		if (attribute.isEmpty()) {
			return "";
		}
		
		if (!(attribute instanceof SortedMap)) {
			attribute = new TreeMap<>(attribute);
		}
		
		try {
			return mapper.writeValueAsString(attribute);
		} catch (JsonProcessingException e) {
			// Should never happen
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public Map/*<String, String>*/ convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return null;
		}
		
		if (dbData.isEmpty()) {
			return new TreeMap<>();
		}
		
		try {
			return mapper.readValue(dbData, mapType);
		} catch (IOException e) {
			// Should never happen
			throw new IllegalArgumentException(e);
		}
	}

}
