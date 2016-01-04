package com.blazebit.storage.rest.model;

import java.io.Serializable;

public class ErrorRepresentation implements Serializable {

	private static final long serialVersionUID = 1L;

	private String key;
	private String code;
	private String message;

	public ErrorRepresentation() {
	}

	public ErrorRepresentation(String key, String code, String message) {
		this.key = key;
		this.code = code;
		this.message = message;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
