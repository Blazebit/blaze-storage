package com.blazebit.storage.testsuite.core.common.view;

import com.blazebit.storage.core.model.jpa.Bucket;
import com.blazebit.storage.testsuite.common.view.IdHolderView;
import com.blazebit.persistence.view.EntityView;

@EntityView(Bucket.class)
public interface BucketView extends IdHolderView<String> {

}
