package com.blazebit.storage.testsuite.common.view;

import com.blazebit.persistence.view.IdMapping;

public interface IdHolderView<T> {

	@IdMapping("id")
	public T getId();
}
