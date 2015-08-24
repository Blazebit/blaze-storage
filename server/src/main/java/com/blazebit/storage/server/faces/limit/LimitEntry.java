package com.blazebit.storage.server.faces.limit;

import java.io.Serializable;

public class LimitEntry implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer value;

	public LimitEntry() {
	}

	public LimitEntry(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
}
