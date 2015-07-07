package com.blazebit.storage.core.model.jpa;

import java.io.Serializable;

public interface IdHolder<I extends Serializable> {

	public I getId();
	
	public void setId(I id);
}
