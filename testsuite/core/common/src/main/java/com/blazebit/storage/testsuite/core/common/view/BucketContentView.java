package com.blazebit.storage.testsuite.core.common.view;

import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.testsuite.common.view.IdHolderView;

import java.util.List;

import com.blazebit.persistence.view.CollectionMapping;
import com.blazebit.persistence.view.EntityView;

@EntityView(Bucket.class)
public interface BucketContentView extends IdHolderView<String> {

	@CollectionMapping(ignoreIndex = true)
	public List<BucketObjectView> getObjects();
}
