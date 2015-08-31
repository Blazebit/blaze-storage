package com.blazebit.storage.rest.model.convert;

import java.util.Map;

import javax.ws.rs.ext.ParamConverter;

public class TagMapParamConverter implements ParamConverter<Map<String, String>> {

	@Override
	public Map<String, String> fromString(String value) {
		return null;
//		char[] chars = value.toCharArray();
//		int i = 0;
//		
//		i = readSpaces(chars, i);
//		i = readOpen(chars, i);
//
//		i = readSpaces(chars, i);
//		i = readValue(chars, i);
//		i = readSpaces(chars, i);
//		i = readKeyValueSeparator(chars, i);
//		i = readSpaces(chars, i);
//		i = readValue(chars, i);
//		i = readSpaces(chars, i);
//		i = readElementSeparator(chars, i);
//		i = readSpaces(chars, i);
//		
//		i = readClose(chars, i);
//		for (; i < chars.length; i++) {
//			
//		}
	}
	
	private int readSpaces(char[] chars, int i) {
		for (; i < chars.length; i++) {
			if (!Character.isWhitespace(chars[i])) {
				return i;
			}
		}
		
		return i;
	}

	@Override
	public String toString(Map<String, String> value) {
		// TODO Auto-generated method stub
		return null;
	}

}
