package com.blazebit.storage.testsuite.common.persistence;

import javax.enterprise.event.Observes;

import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import com.blazebit.persistence.view.spi.EntityViewConfiguration;

public class BlazePersistenceConfigurator {

    public void init(@Observes CriteriaBuilderConfiguration config) {
//    	config.setProperty("com.blazebit.persistence.returning_clause_case_sensitive", "false");
    }

    public void init(@Observes EntityViewConfiguration config) {
    	config.setProperty("com.blazebit.persistence.view.proxy.eager_loading", "true");
    }
}