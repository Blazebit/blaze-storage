package com.blazebit.storage.testsuite.core.common.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.storage.core.model.jpa.Storage;
import com.blazebit.storage.core.model.jpa.StorageId;
import com.blazebit.storage.testsuite.common.view.IdHolderView;

@EntityView(Storage.class)
public interface StorageIdHolderView extends IdHolderView<StorageId> {

}
