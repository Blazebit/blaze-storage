package com.blazebit.storage.testsuite.core.common.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.storage.core.model.jpa.BucketObject;
import com.blazebit.storage.core.model.jpa.BucketObjectId;
import com.blazebit.storage.testsuite.common.view.IdHolderView;

@EntityView(BucketObject.class)
public interface BucketObjectView extends IdHolderView<BucketObjectId> {

}
