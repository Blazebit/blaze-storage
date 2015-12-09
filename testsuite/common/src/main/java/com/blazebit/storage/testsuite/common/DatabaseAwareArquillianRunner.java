package com.blazebit.storage.testsuite.common;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.TransactionRolledbackException;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.blazebit.annotation.AnnotationUtils;
import com.blazebit.exception.ExceptionUtils;

public class DatabaseAwareArquillianRunner extends Arquillian {

	private static int testRun = 1;
	
	public DatabaseAwareArquillianRunner(Class<?> klass)
			throws InitializationError {
		super(klass);
	}

	/**
     * Manages CDI-Contexts to be started before entering end stopped after exiting a Statement.
     * Additionally it is aware of the DatabaseAware, DatabaseUnaware and BeforeDatabaseAware annotations.
     * It creates an EntityManagerFactory before entering the test method and closes it after exiting
     * to always have a clean state of the database.
     */
    @Override
    protected List<TestRule> getTestRules(final Object target) {
        List<TestRule> rules = new ArrayList<TestRule>(super.getTestRules(target));
        rules.add(new TestRule() {

            @Override
            public Statement apply(final Statement base, final Description description) {
                final DatabaseAware databaseAwareAnnotation;

                if (description.getAnnotation(DatabaseUnaware.class) != null) {
                    databaseAwareAnnotation = null;
                } else if (description.getAnnotation(DatabaseAware.class) != null) {
                    databaseAwareAnnotation = description.getAnnotation(DatabaseAware.class);
                } else {
                    databaseAwareAnnotation = AnnotationUtils.findAnnotation(target.getClass(), DatabaseAware.class);
                }

                List<FrameworkMethod> befores = new ArrayList<FrameworkMethod>(0);

                if (databaseAwareAnnotation != null) {
                    for (FrameworkMethod m : getTestClass().getAnnotatedMethods(BeforeDatabaseAware.class)) {
                        if (databaseAwareAnnotation.unitName().equals(m.getAnnotation(BeforeDatabaseAware.class).unitName())) {
                            befores.add(m);
                        }
                    }
                }

                final Statement statement = befores.isEmpty() ? base : new RunBefores(base, befores, target);

                return new Statement() {

                    @Override
                    public void evaluate() throws Throwable {
                        EntityManagerFactory emf = null;

                        try {

                            if (databaseAwareAnnotation != null) {
                                Map<String, String> properties = new HashMap<String, String>();
                                properties.put("hibernate.hbm2ddl.auto", "create-drop");
                                properties.put("hibernate.ejb.entitymanager_factory_name", databaseAwareAnnotation.unitName()
                                    + testRun++);
                                
                                emf = Persistence.createEntityManagerFactory(databaseAwareAnnotation.unitName(), properties);
                            }

                            statement.evaluate();
                        } catch (Throwable e) {
                            // We do some exception unwrapping here to get the real exception
                            @SuppressWarnings("unchecked")
                            Throwable ex = ExceptionUtils.unwrap(e, InvocationTargetException.class, EJBException.class,
                                                                 TransactionRolledbackException.class);
                            throw ex;
                        } finally {
                            if (emf != null && emf.isOpen()) {
                                emf.close();
                            }

                        }
                    }
                };
            }
        });
        return rules;
    }
}