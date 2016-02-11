package com.blazebit.storage.core.model.jpa.converter;

import java.net.URI;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class URIConverter implements AttributeConverter<URI, String> {

	@Override
	public String convertToDatabaseColumn(URI attribute) {
		if (attribute == null) {
			return null;
		}
		
		return attribute.toString();
	}

	@Override
	public URI convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return null;
		}
		
		return URI.create(dbData);
	}

}
