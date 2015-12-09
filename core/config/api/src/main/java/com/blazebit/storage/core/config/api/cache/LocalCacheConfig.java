package com.blazebit.storage.core.config.api.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR })
public @interface LocalCacheConfig {

	@Nonbinding
	String name();
	
	@Nonbinding
	int expiryAmount() default -1;
	
	@Nonbinding
	TimeUnit expiryUnit() default TimeUnit.SECONDS;
}