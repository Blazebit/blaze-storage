package com.blazebit.storage.testsuite.common.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.storage.core.model.jpa.SequenceBaseEntity;

@EntityView(SequenceBaseEntity.class)
public interface LongIdHolderView extends IdHolderView<Long> {

}
