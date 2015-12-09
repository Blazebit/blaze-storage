package com.blazebit.storage.testsuite.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ejb.EJBException;
import javax.transaction.RollbackException;

import com.blazebit.exception.ExceptionUtils;
import com.blazebit.storage.core.api.StorageException;
import com.googlecode.catchexception.internal.ExceptionProcessingInterceptor;

public class EjbAwareThrowableProcessingInterceptor<E extends Exception> extends ExceptionProcessingInterceptor<E> {

    @SuppressWarnings("unchecked")
    private static final Class<? extends Throwable>[] UNWRAP_WITH_CORE = (Class<? extends Throwable>[]) new Class<?>[]{InvocationTargetException.class, EJBException.class, RollbackException.class, StorageException.class};
    @SuppressWarnings("unchecked")
    private static final Class<? extends Throwable>[] UNWRAP_WITHOUT_CORE = (Class<? extends Throwable>[]) new Class<?>[]{InvocationTargetException.class, EJBException.class, RollbackException.class};
    
    private final Class<? extends Throwable>[] unwraps;
    
    /**
     * @param target
     * @param clazz
     * @param assertThrowable
     */
    public EjbAwareThrowableProcessingInterceptor(Object target, Class<E> clazz) {
        super(target, clazz, true);
        unwraps = StorageException.class.isAssignableFrom(clazz) ? UNWRAP_WITHOUT_CORE : UNWRAP_WITH_CORE;
    }
    
    @Override
    protected Object afterInvocationThrowsException(Exception e, Method method)
    		throws Error, Exception {
    	Exception realException = (Exception) ExceptionUtils.unwrap(e, unwraps);
        
        if (realException == null) {
            realException = e;
        }
        
        return super.afterInvocationThrowsException(realException, method);
    }

}