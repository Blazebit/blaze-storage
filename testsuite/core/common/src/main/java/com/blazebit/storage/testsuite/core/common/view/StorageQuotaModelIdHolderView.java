package com.blazebit.storage.testsuite.core.common.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.storage.core.model.jpa.StringBaseEntity;
import com.blazebit.storage.testsuite.common.view.IdHolderView;

@EntityView(StringBaseEntity.class)
public interface StorageQuotaModelIdHolderView extends IdHolderView<String> {

}
